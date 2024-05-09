package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import se.liu.ida.hefquin.connectors.pg.data.impl.LPGNode;

public class NodeMappingImpl_AllToURIs implements NodeMapping{

    protected final String NSNODE;

    public NodeMappingImpl_AllToURIs (final String NSNODE){
        this.NSNODE= NSNODE;
    }

    @Override
    public Node map(final LPGNode node) {
        return NodeFactory.createURI(NSNODE + node.getId());
    }

    @Override
    public LPGNode unmap(final Node node) {
        if (!isPossibleResult(node))
            throw new IllegalArgumentException("The given RDF term (" + node.toString() + ") is not an URI node or not in the image of this node mapping.");
        final String id = node.getURI().replaceAll(NSNODE, "");
        return new LPGNode(id, "", null);
    }

    @Override
    public boolean isPossibleResult(final Node node) {
        return node.isURI() && node.getURI().startsWith(NSNODE);
    }
}
