package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import se.liu.ida.hefquin.connectors.pg.impl.exceptions.UnSupportedEdgeLabelException;

public class EdgeLabelMappingImpl_SingleMatchToURI implements EdgeLabelMapping {

    protected final String label;
    protected final Node node;

    public EdgeLabelMappingImpl_SingleMatchToURI(final String label, final String iri){
        this.label=label;
        this.node = NodeFactory.createURI(iri);
    }

    @Override
    public Node map(final String label) {
        if (label.equals(this.label)) {
            return this.node;
        }
        else {
            throw new UnSupportedEdgeLabelException("The given edge label (" + label + ") is not a supported label in the image of this edge label mapping.");
        }
    }

    public String unmap(final Node node) {
        if (!isPossibleResult(node))
            throw new UnSupportedEdgeLabelException("The given RDF term (" + node.toString() + ") is not an URI node or not in the image of this edge label mapping.");
        return this.label;
    }

    @Override
    public boolean isPossibleResult(final Node node) {
        return node.isURI() && node.getURI().equals(this.node.getURI());
    }

}
