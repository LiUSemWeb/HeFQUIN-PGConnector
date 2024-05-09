package se.liu.ida.hefquin.connectors.pg.data.impl;

import java.util.Map;

import se.liu.ida.hefquin.connectors.pg.data.PropertyMap;
import se.liu.ida.hefquin.connectors.pg.data.Value;

public class PropertyMapImpl implements PropertyMap {

    protected final Map<String, Value> properties;

    public PropertyMapImpl(final Map<String, Value> properties) {
        assert properties != null;
        this.properties = properties;
    }

    @Override
    public Value getValueFor(final String key) {
        return properties.get(key);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public Iterable<Value> getAllValues() {
        return properties.values();
    }
}
