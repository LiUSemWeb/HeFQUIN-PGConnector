package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;

import se.liu.ida.hefquin.connectors.pg.conf.LPG2RDFConfiguration;
import se.liu.ida.hefquin.connectors.pg.data.impl.LPGNode;

public class LPG2RDFConfigurationImpl implements LPG2RDFConfiguration
{
    protected final NodeMapping nm;
    protected final NodeLabelMapping nlm;
    protected final EdgeLabelMapping elm;
    protected final PropertyNameMapping pm;
    protected final Node labelPredicate;

    public LPG2RDFConfigurationImpl( final NodeMapping nm,
                                     final NodeLabelMapping nlm,
                                     final EdgeLabelMapping elm,
                                     final PropertyNameMapping pm,
                                     final Node labelPredicate ) {
        this.labelPredicate = labelPredicate;
        this.nm  = nm;
        this.nlm = nlm;
        this.elm = elm;
        this.pm  = pm;
    }

    @Override
    public Node getRDFTermForLPGNode(final LPGNode node) {
        return nm.map(node);
    }

    @Override
    public LPGNode getLPGNodeForRDFTerm(final Node node) {
        return nm.unmap(node);
    }

    @Override
    public Node getRDFTermForNodeLabel(final String label) {
        return nlm.map(label);
    }

    @Override
    public String getNodeLabelForRDFTerm(final Node node) {
        return nlm.unmap(node);
    }

    @Override
    public Node getIRIForEdgeLabel(final String label) {
        return elm.map(label);
    }

    @Override
    public String getEdgeLabelForIRI(final Node node) {
        return elm.unmap(node);
    }

    @Override
    public Node getIRIForPropertyName(final String propertyName) {
        return pm.map(propertyName);
    }

    @Override
    public String getPropertyNameForIRI(final Node node) {
        return pm.unmap(node);
    }

    @Override
    public Node getLabelPredicate() {
        return this.labelPredicate;
    }

    @Override
    public boolean isIRIForPropertyName(final Node n) {
        return pm.isPossibleResult(n);
    }

    @Override
    public boolean isRDFTermForNodeLabel(final Node n) {
        return nlm.isPossibleResult(n);
    }

    @Override
    public boolean isIRIForEdgeLabel(final Node n) {
        return elm.isPossibleResult(n);
    }

    @Override
    public boolean isRDFTermForLPGNode(final Node n) {
        return nm.isPossibleResult(n);
    }
}
