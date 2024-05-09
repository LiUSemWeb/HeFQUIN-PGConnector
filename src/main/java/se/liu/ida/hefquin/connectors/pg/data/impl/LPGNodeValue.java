package se.liu.ida.hefquin.connectors.pg.data.impl;

import se.liu.ida.hefquin.connectors.pg.data.Value;

public class LPGNodeValue implements Value {

    protected final LPGNode node;

    public LPGNodeValue(final LPGNode node) {
        this.node = node;
    }

    public LPGNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
