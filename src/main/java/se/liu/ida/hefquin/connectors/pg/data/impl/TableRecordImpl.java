package se.liu.ida.hefquin.connectors.pg.data.impl;

import java.util.List;

import se.liu.ida.hefquin.connectors.pg.data.RecordEntry;
import se.liu.ida.hefquin.connectors.pg.data.TableRecord;

public class TableRecordImpl implements TableRecord {

    final List<RecordEntry> entries;

    public TableRecordImpl(final List<RecordEntry> entries) {
        this.entries = entries;
    }

    @Override
    public Iterable<RecordEntry> getRecordEntries() {
        return entries;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public RecordEntry getEntry(int i) {
        return entries.get(i);
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}
