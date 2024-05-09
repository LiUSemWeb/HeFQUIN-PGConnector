package se.liu.ida.hefquin.connectors.pg.utils;

import java.util.ArrayList;
import java.util.List;

import se.liu.ida.hefquin.connectors.pg.query.*;
import se.liu.ida.hefquin.connectors.pg.query.impl.CypherMatchQueryImpl;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.AliasedExpression;
import se.liu.ida.hefquin.connectors.pg.query.impl.expression.BooleanCypherExpression;

public class CypherQueryBuilder {

    private final List<MatchClause> matches;
    private final List<BooleanCypherExpression> conditions;
    private final List<UnwindIterator> iterators;
    private final List<AliasedExpression> returns;

    public CypherQueryBuilder() {
        this.matches = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.iterators = new ArrayList<>();
        this.returns = new ArrayList<>();
    }

    public CypherQueryBuilder addMatch(final MatchClause match) {
        if (!matches.contains(match))
            matches.add(match);
        return this;
    }

    public CypherQueryBuilder addCondition(final BooleanCypherExpression condition) {
        if (!conditions.contains(condition))
            conditions.add(condition);
        return this;
    }

    public CypherQueryBuilder addIterator(final UnwindIterator iterator) {
        if (!iterators.contains(iterator))
            iterators.add(iterator);
        return this;
    }

    public CypherQueryBuilder addReturn(final AliasedExpression ret) {
        if (!returns.contains(ret))
            returns.add(ret);
        return this;
    }

    public CypherQueryBuilder add(final Object clause) {
        if (clause instanceof MatchClause) {
            this.addMatch((MatchClause) clause);
        } else if (clause instanceof BooleanCypherExpression) {
            this.addCondition((BooleanCypherExpression) clause);
        } else if (clause instanceof UnwindIterator) {
            this.addIterator((UnwindIterator) clause);
        } else if (clause instanceof AliasedExpression) {
            this.addReturn((AliasedExpression) clause);
        } else {
            throw new IllegalArgumentException("Provided object is not a CypherQuery Clause: " + clause.getClass());
        }
        return this;
    }

    public CypherMatchQuery build() {
        return new CypherMatchQueryImpl(matches, conditions, iterators, returns);
    }

    public CypherQueryBuilder addAll(final CypherMatchQuery q) {
        for (final MatchClause m : q.getMatches()){
            this.addMatch(m);
        }
        for (final BooleanCypherExpression c : q.getConditions()) {
            this.addCondition(c);
        }
        for (final UnwindIterator i : q.getIterators()) {
            this.addIterator(i);
        }
        for (final AliasedExpression r : q.getReturnExprs()) {
            this.addReturn(r);
        }
        return this;
    }

    public CypherQueryBuilder addAll(final List<? extends Object> objects) {
        for (final Object o : objects)
            this.add(o);
        return this;
    }
}
