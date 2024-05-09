package se.liu.ida.hefquin.connectors.pg.conf.impl;

import org.apache.jena.graph.Node;

import se.liu.ida.hefquin.connectors.pg.data.impl.LPGNode;

/**
 * This interface captures the notion of a node mapping that is part of the notion of an LPG-to-RDF configuration,
 * and that such a node mapping is an injective function from nodes in LPGs to IRIs or blank nodes as
 * can occur in RDF graphs.
 * This interface contains the functions:
 *  -map: map nodes to IRIs/BNodes
 *  -unmap: map IRIs/BNodes to nodes
 */
public interface NodeMapping {


    /**
     * Returns a URI or a blank node (in the form of a Jena {@link Node} object) for the given LPG node.
     * It applies this node mapping to the given LPG node
     */
    Node map(LPGNode node);

    /**
     * Returns the LPG node that corresponds to the given RDF term.
     * It applies the inverse of this node mapping to the given RDF term (which is assumed to be a blank node or an IRI) in order to obtain the corresponding LPG node.
     */
    LPGNode unmap(Node node);


    /**
     * Check if the given RDF term is in the image of this node mapping and,
     * thus, may be one of the RDF terms returned by the {@link #map(LPGNode)}
     * function for some LPG node.
     */
    boolean isPossibleResult(Node node);
}
