package se.liu.ida.hefquin.connectors.pg.utils;

import java.util.*;
import java.util.stream.Collectors;

import se.liu.ida.hefquin.connectors.pg.query.impl.expression.CypherVar;

/**
 * This class models graphs with labeled edges. Nodes and edge labels are modeled as {@link CypherVar} objects.
 * Edges can be directed or not. A pair of nodes can have an arbitrary number of edges between them. Nodes can
 * have edges that start and end on themselves.
 */
public class LabeledGraph {

    /**
     * Enum for the possible edge directions
     */
    public enum Direction {LEFT2RIGHT, RIGHT2LEFT, UNDIRECTED}

    /**
     * An edge of a labeled graph has an id, an edge label, a target node and a direction
     */
    public static class Edge {
        protected final int id;
        protected final CypherVar edge;
        protected final CypherVar target;
        protected final Direction direction;

        public Edge(final int id, final CypherVar edge, final CypherVar target, final Direction direction) {
            this.id = id;
            this.edge = edge;
            this.target = target;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "(" + id + ", " + edge + ", " + target + ", " + direction + ')';
        }

        public CypherVar getEdge() {
            return edge;
        }

        public CypherVar getTarget() {
            return target;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    /**
     * Represents a path in a graph, starting from a given node and following a sequence of edges.
     */
    public static class Path {
        protected final CypherVar start;
        protected final List<Edge> path;

        public Path(final CypherVar start, final Edge e) {
            this.start = start;
            this.path = new LinkedList<>();
            this.path.add(e);
        }

        public Path(final CypherVar start, final List<Edge> path) {
            this.start = start;
            this.path = path;
        }

        public void addEdge(final Edge e) {
            this.path.add(e);
        }

        public int size() {
            return path.size();
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("(").append(start).append(")");
            for (final LabeledGraph.Edge e : path) {
                if (e.direction.equals(LabeledGraph.Direction.RIGHT2LEFT)) {
                    builder.append("<");
                }
                builder.append("-").append("[").append(e.edge).append("]").append("-");
                if (e.direction.equals(LabeledGraph.Direction.LEFT2RIGHT)) {
                    builder.append(">");
                }
                builder.append("(").append(e.target).append(")");
            }
            return builder.toString();
        }

        public void removeLast() {
            this.path.remove(size()-1);
        }

        public Path copy() {
            return new Path(start, new LinkedList<>(path));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path1 = (Path) o;
            return start.equals(path1.start) && path.equals(path1.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, path);
        }

        public CypherVar getStart() {
            return start;
        }

        public List<Edge> getEdges() {
            return path;
        }

        public CypherVar lastTarget() {
            return path.get(path.size()-1).target;
        }
    }

    protected final Map<CypherVar, List<Edge>> adjacencyLists;

    public LabeledGraph(final Map<CypherVar, List<Edge>> adjacencyLists) {
        assert adjacencyLists != null;
        this.adjacencyLists = adjacencyLists;
    }

    /**
     * removes the edges contained in path
     */
    public void removePath(final Path path) {
        if (adjacencyLists.get(path.start).isEmpty()) {
            adjacencyLists.remove(path.start);
            return;
        }
        CypherVar current = path.start;
        for (final Edge e : path.path) {
            //removes LEFT2RIGHT
            List<Edge> toRemove = adjacencyLists.get(current).stream().filter(x->x.edge.equals(e.edge)).collect(Collectors.toList());
            adjacencyLists.get(current).removeAll(toRemove);
            if (adjacencyLists.get(current).isEmpty()) {
                adjacencyLists.remove(current);
            }
            //removes RIGHT2LEFT
            if (!adjacencyLists.containsKey(e.target)) continue;;
            toRemove = adjacencyLists.get(e.target).stream().filter(x->x.id==e.id).collect(Collectors.toList());
            adjacencyLists.get(e.target).removeAll(toRemove);
            if (adjacencyLists.get(e.target).isEmpty()) {
                adjacencyLists.remove(e.target);
            }
            current = e.target;
        }
    }

    /**
     * Enumerates all possible paths and returns the longest found. In case of ties, it returns the first
     * longest path found.
     */
    public Path getLongestPath() {
        Path longest = null;
        for (final CypherVar start : adjacencyLists.keySet()) {
            final Path candidate = longestStartingFrom(start);
            if (longest == null || candidate.size() > longest.size()) {
                longest = candidate;
            }
        }
        return longest;
    }

    private Path longestStartingFrom(final CypherVar start) {
        if (adjacencyLists.get(start).isEmpty()) return new Path(start, new ArrayList<>());
        final Set<Integer> visitedEdges = new HashSet<>();
        final Deque<Edge> toVisit = new ArrayDeque<>( adjacencyLists.get(start) );
        Path candidate = null;
        Path longest = null;
        while (!toVisit.isEmpty()) {
            final Edge currentEdge = toVisit.pop();
            if (candidate == null || candidate.size()==0) {
                candidate = new Path(start, currentEdge);
            }
            else if (! visitedEdges.contains(currentEdge.id)) {
                candidate.addEdge(currentEdge);
            }
            visitedEdges.add(currentEdge.id);
            if (!allVisited(adjacencyLists.get(currentEdge.target), visitedEdges)) {
                for (final Edge e : adjacencyLists.get(currentEdge.target)) {
                    if (!visitedEdges.contains(e.id)) {
                        toVisit.push(e);
                    }
                }
            } else {
                if ( longest == null || candidate.size() > longest.size() ) {
                    longest = candidate.copy();
                }
                while (true) {
                    candidate.removeLast();
                    if (candidate.size()==0 || !visitedEdges.containsAll(adjacencyLists.get(candidate.lastTarget()).stream().map(e->e.id).collect(Collectors.toList()))) {
                        break;
                    }
                }
            }
        }
        return longest;
    }

    private boolean allVisited(final List<Edge> edges, Set<Integer> visitedEdges) {
        return edges == null || visitedEdges.containsAll(edges.stream().map(x->x.id).collect(Collectors.toSet()));
    }

    //checks if there's any nodes or edges in the graph
    public boolean isEmpty() {
        return adjacencyLists.isEmpty();
    }

    @Override
    public String toString() {
        return adjacencyLists.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabeledGraph graph = (LabeledGraph) o;
        return adjacencyLists.equals(graph.adjacencyLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyLists);
    }
}
