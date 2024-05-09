package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import se.liu.ida.hefquin.connectors.pg.impl.exceptions.UnSupportedEdgeLabelException;

public class EdgeLabelMappingImpl_AllToURIs implements EdgeLabelMapping {

    protected final String NSRELATIONSHIP;


    public EdgeLabelMappingImpl_AllToURIs(final String NSRELATIONSHIP){
        this.NSRELATIONSHIP=NSRELATIONSHIP ;
    }

    @Override
    public Node map(final String label) {
        return NodeFactory.createURI(NSRELATIONSHIP + label);
    }

    @Override
    public String unmap(final Node node) {
        if (!isPossibleResult(node))
            throw new UnSupportedEdgeLabelException("The given RDF term (" + node.toString() + ") is not an URI node or not in the image of this edge label mapping.");
        return node.getURI().replaceAll(NSRELATIONSHIP, "");
    }

    @Override
    public boolean isPossibleResult(final Node node) {
        return node.isURI() && node.getURI().startsWith(NSRELATIONSHIP);
    }
}
