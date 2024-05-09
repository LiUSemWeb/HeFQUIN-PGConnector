package se.liu.ida.hefquin.connectors.pg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import se.liu.ida.hefquin.connectors.pg.Neo4jException;
import se.liu.ida.hefquin.connectors.pg.data.RecordEntry;
import se.liu.ida.hefquin.connectors.pg.data.TableRecord;
import se.liu.ida.hefquin.connectors.pg.data.impl.*;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.CypherVar;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CypherUtilsTest
{
    @Test
    public void parseNodesFromStringTest() throws JsonProcessingException, Neo4jException {
        final String response = "{\"results\": [" +
                "{\"columns\": [\"x\", \"y\", \"z\"]," +
                "\"data\": [{" +
                    "\"row\": [" +
                        "{\"p1\": \"v1\", \"p2\": \"v2\"}," +
                        "{\"p1\": \"v3\", \"p2\": \"v4\"}," +
                        "{\"p1\": \"v5\", \"p2\": \"v6\"}" +
                    "]," +
                    "\"meta\": [" +
                        "{\"id\": 1, \"type\": \"node\", \"deleted\": false}," +
                        "{\"id\": 2, \"type\": \"node\", \"deleted\": false}," +
                        "{\"id\": 3, \"type\": \"node\", \"deleted\": false}" +
                    "]}" +
                "]}], \"errors\": []}";
        List<TableRecord> records = CypherUtils.parse(response);
        assertEquals(1, records.size());
        final Iterator<RecordEntry> entries =  records.get(0).getRecordEntries().iterator();
        assertTrue(entries.hasNext());
        final RecordEntry x = entries.next();
        assertEquals(new CypherVar("x"), x.getName());
        assertTrue(x.getValue() instanceof LPGNodeValue);
        assertTrue(entries.hasNext());
        final RecordEntry y = entries.next();
        assertEquals(new CypherVar("y"), y.getName());
        assertTrue(y.getValue() instanceof LPGNodeValue);
        assertTrue(entries.hasNext());
        final RecordEntry z = entries.next();
        assertEquals(new CypherVar("z"), z.getName());
        assertTrue(z.getValue() instanceof LPGNodeValue);
    }

    @Test
    public void parseMiscTest() throws JsonProcessingException, Neo4jException {
        final String response = "{\"results\": [" +
                "{\"columns\": [\"x\", \"y\", \"z\"]," +
                    "\"data\": [{" +
                        "\"row\": [" +
                            "{\"p1\": \"v1\", \"p2\": \"v2\"}," +
                            "{\"p1\": \"v3\", \"p2\": \"v4\"}," +
                            "4" +
                        "]," +
                        "\"meta\": [" +
                            "{\"id\": 1, \"type\": \"node\", \"deleted\": false}," +
                            "{\"id\": 2, \"type\": \"relationship\", \"deleted\": false}," +
                            "{}" +
                        "]}" +
                "]}], \"errors\": []}";
        List<TableRecord> records = CypherUtils.parse(response);
        assertEquals(1, records.size());
        final Iterator<RecordEntry> entries =  records.get(0).getRecordEntries().iterator();
        assertTrue(entries.hasNext());
        final RecordEntry x = entries.next();
        assertEquals(new CypherVar("x"), x.getName());
        assertTrue(x.getValue() instanceof LPGNodeValue);
        assertTrue(entries.hasNext());
        final RecordEntry y = entries.next();
        assertEquals(new CypherVar("y"), y.getName());
        assertTrue(y.getValue() instanceof LPGEdgeValue);
        assertTrue(entries.hasNext());
        final RecordEntry z = entries.next();
        assertEquals(new CypherVar("z"), z.getName());
        assertTrue(z.getValue() instanceof LiteralValue);
    }

    @Test(expected = JsonProcessingException.class)
    public void malformedJSONparseTest() throws JsonProcessingException, Neo4jException {
        CypherUtils.parse("{\"notResults\"}");
    }

}
