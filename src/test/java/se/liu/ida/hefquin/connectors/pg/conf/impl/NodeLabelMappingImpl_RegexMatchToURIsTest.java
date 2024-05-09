package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.junit.Test;

import se.liu.ida.hefquin.connectors.pg.impl.exceptions.UnSupportedNodeLabelException;

import static org.junit.Assert.*;

public class NodeLabelMappingImpl_RegexMatchToURIsTest {
    protected final String NODELABEL = "https://example2.org/test/";
    protected final String regex = "^[0-9]+";

    protected final NodeLabelMapping nodeLabelMapping = new NodeLabelMappingImpl_RegexMatchToURIs(regex, NODELABEL);

    @Test
    public void mapNodeLabel() {
        final String label = "0";
        final Node resultNode = nodeLabelMapping.map(label);
        assertNotNull(resultNode);
        assertTrue(resultNode.isURI());
        assertEquals(resultNode.getURI(), NODELABEL + "0");
    }

    @Test
    public void unmapURINodeLabel(){
        final Node node = NodeFactory.createURI(NODELABEL + "0");
        final String resultString = nodeLabelMapping.unmap(node);
        assertNotNull(resultString);
        assertEquals(resultString, "0");
    }

    @Test
    public void nodeLabelIsPossibleResult(){
        final Node IRINode = NodeFactory.createURI(NODELABEL + "0");
        final boolean IRIIsPossible = nodeLabelMapping.isPossibleResult(IRINode);
        assertTrue(IRIIsPossible);
    }


    /*
     * In this test case, a node with an invalid URI is provided as an argument to the RegexBasedNodeLabelMappingToURIsImpl.
     */
    @Test(expected = UnSupportedNodeLabelException.class)
    public void unmapNodeLabelWithInvalidURI(){
        final Node node = NodeFactory.createURI("https://example.org/3");
        nodeLabelMapping.unmap(node);
    }

    /*
     * In this test case, a label which is not match with provided regex in the RegexBasedNodeLabelMappingToURIsImpl.
     */
    @Test(expected = UnSupportedNodeLabelException.class)
    public void mapNodeLabelWithUnmatchedLabel(){
        final String label = "test";
        nodeLabelMapping.map(label);
    }
}
