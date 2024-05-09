package se.liu.ida.hefquin.connectors.pg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.liu.ida.hefquin.connectors.pg.Neo4jException;
import se.liu.ida.hefquin.connectors.pg.data.RecordEntry;
import se.liu.ida.hefquin.connectors.pg.data.TableRecord;
import se.liu.ida.hefquin.connectors.pg.data.impl.TableRecordImpl;
import se.liu.ida.hefquin.connectors.pg.query.*;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.*;

import java.util.*;

public class CypherUtils {

    /**
     * This method parses a JSON response obtained from a Neo4j HTTP server into a list of POJOs
     * @throws JsonProcessingException if the received body is not valid JSON
     * @throws Neo4jException if the server responds with an error object
     */
    public static List<TableRecord> parse(final String body) throws JsonProcessingException, Neo4jException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root = mapper.readTree(body);
        final JsonNode errors = root.get("errors");
        if ( errors != null && errors.isArray() && !errors.isEmpty() ) {
            throw new Neo4jException( errors.get(0).textValue() );
        }

        final List<TableRecord> records = new LinkedList<>();

        final JsonNode results = root.get("results");
        if ( results == null )
            return records;

        for (final JsonNode r : results) {
            final JsonNode columns = r.get("columns");
            final JsonNode data = r.get("data");
            final List<CypherVar> names = new ArrayList<>();
            for (final JsonNode c : columns) {
                names.add(new CypherVar(c.asText()));
            }
            for (final JsonNode e : data) {
                final JsonNode row = e.get("row");
                final JsonNode meta = e.get("meta");
                final Iterator<JsonNode> metaIterator = meta.iterator();
                final List<RecordEntry> entries = new LinkedList<>();
                int counter = 0;
                for (final JsonNode col : row) {
                    final RecordEntry entry = RecordEntryFactory.create(col, metaIterator, names.get(counter));
                    entries.add(entry);
                    counter++;
                }
                records.add(new TableRecordImpl(entries));
            }
        }
        return records;
    }

    public static Object replaceVariable(final Map<CypherVar, CypherVar> equivalences, final CypherExpression ex) {
        if (Collections.disjoint(equivalences.keySet(), ex.getVars())) return ex;
        final VariableReplacementVisitor visitor = new VariableReplacementVisitor(equivalences);
        visitor.visit(ex);
        return visitor.getResult();
    }
}
