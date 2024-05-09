package se.liu.ida.hefquin.connectors.pg.query.impl;

import java.util.*;
import java.util.stream.Collectors;

import se.liu.ida.hefquin.connectors.pg.query.CypherMatchQuery;
import se.liu.ida.hefquin.connectors.pg.query.CypherQuery;
import se.liu.ida.hefquin.connectors.pg.query.CypherUnionQuery;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.CypherVar;

public class CypherUnionQueryImpl implements CypherUnionQuery {

    final List<CypherMatchQuery> union;

    public CypherUnionQueryImpl() {
        union = new ArrayList<>();
    }

    public CypherUnionQueryImpl(CypherMatchQuery... queries) {
        this();
        union.addAll(Arrays.asList(queries));
    }

    public CypherUnionQueryImpl(List<CypherMatchQuery> result) {
        this.union = result;
    }

    @Override
    public List<CypherMatchQuery> getSubqueries() {
        return union;
    }

    @Override
    public Set<CypherVar> getMatchVars() {
        return union.stream().map(CypherQuery::getMatchVars).flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return union.stream().map(CypherQuery::toString).collect(Collectors.joining(" UNION "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CypherUnionQueryImpl)) return false;
        CypherUnionQueryImpl that = (CypherUnionQueryImpl) o;
        return union.equals(that.union);
    }

    @Override
    public int hashCode() {
        return Objects.hash(union);
    }
}
