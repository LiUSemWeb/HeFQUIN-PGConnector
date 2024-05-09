package se.liu.ida.hefquin.connectors.pg.data.impl;

import se.liu.ida.hefquin.connectors.pg.data.Value;

public class LiteralValue implements Value {

    protected final Object internalValue;

    public LiteralValue(final Object internalValue) {
        this.internalValue = internalValue;
    }

    public Object getValue() {
        return internalValue;
    }

    @Override
    public String toString() {
        return internalValue.toString();
    }
}
