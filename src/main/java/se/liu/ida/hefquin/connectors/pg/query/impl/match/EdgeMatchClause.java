package se.liu.ida.hefquin.connectors.pg.query.impl.match;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import se.liu.ida.hefquin.connectors.pg.query.MatchClause;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.CypherVar;
import se.liu.ida.hefquin.connectors.pg.utils.CypherExpressionVisitor;

/**
 * Represents a directed path match statement. Conditions on the labels of the nodes or edges, as well as
 * over property values of nodes or edges are managed through the WHERE clause.
 * For example MATCH (x)-[e]->(y)
 */
public class EdgeMatchClause implements MatchClause {
    protected final CypherVar sourceNode;
    protected final CypherVar targetNode;
    protected final CypherVar edge;

    public EdgeMatchClause(final CypherVar sourceNode, final CypherVar edge, final CypherVar targetNode) {
        assert sourceNode != null;
        assert edge != null;
        assert targetNode != null;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.edge = edge;
    }

    public CypherVar getSourceNode() {
        return sourceNode;
    }

    public CypherVar getTargetNode() {
        return targetNode;
    }

    public CypherVar getEdge() {
        return edge;
    }

    @Override
    public String toString() {
        return "MATCH (" +
                sourceNode.getName() +
                ")-[" +
                edge.getName() +
                "]->(" +
                targetNode.getName() +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeMatchClause)) return false;
        EdgeMatchClause that = (EdgeMatchClause) o;
        return sourceNode.equals(that.sourceNode) && targetNode.equals(that.targetNode)
                && Objects.equals(edge, that.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceNode, targetNode, edge);
    }

    @Override
    public boolean isRedundantWith(final MatchClause match) {
        if (match instanceof EdgeMatchClause)
            return this.equals(match);
        else if (match instanceof NodeMatchClause)
            return ((NodeMatchClause) match).getNode().equals(sourceNode) ||
                    ((NodeMatchClause) match).getNode().equals(targetNode);
        else return false;
    }

    @Override
    public Set<CypherVar> getVars() {
        final Set<CypherVar> vars = new HashSet<>();
        vars.add(sourceNode);
        vars.add(targetNode);
        vars.add(edge);
        return vars;
    }

    @Override
    public void visit(final CypherExpressionVisitor visitor) {
        sourceNode.visit(visitor);
        edge.visit(visitor);
        targetNode.visit(visitor);
        visitor.visitEdgeMatch(this);
    }
}
