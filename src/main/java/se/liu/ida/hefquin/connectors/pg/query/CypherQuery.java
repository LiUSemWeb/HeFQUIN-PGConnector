package se.liu.ida.hefquin.connectors.pg.query;

import se.liu.ida.hefquin.connectors.pg.query.impl.expression.CypherVar;

import java.util.Set;

/**
 * Represents a query written in the Cypher query language.
 */
public interface CypherQuery 
{
	/**
	 * Returns a Cypher expression representing this query.
	 */
	String toString();

	/**
	 * Returns the set of variables that are defined in the
	 * MATCH clauses of this query.
	 */
	Set<CypherVar> getMatchVars();

}
