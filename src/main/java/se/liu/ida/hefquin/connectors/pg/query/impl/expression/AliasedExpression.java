package se.liu.ida.hefquin.connectors.pg.query.impl.expression;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import se.liu.ida.hefquin.connectors.pg.query.CypherExpression;
import se.liu.ida.hefquin.connectors.pg.utils.CypherExpressionVisitor;

public class AliasedExpression implements CypherExpression {

    final CypherExpression expression;
    final CypherVar alias;

    public AliasedExpression(final CypherExpression expression, final CypherVar alias) {
        assert expression != null;
        assert alias != null;

        this.expression = expression;
        this.alias = alias;
    }

    @Override
    public Set<CypherVar> getVars() {
        final Set<CypherVar> res = new HashSet<>(expression.getVars());
        res.add(alias);
        return res;
    }

    @Override
    public void visit(final CypherExpressionVisitor visitor) {
        expression.visit(visitor);
        alias.visit(visitor);
        visitor.visitAliasedExpression(this);
    }

    public CypherVar getAlias() {
        return alias;
    }

    public CypherExpression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasedExpression that = (AliasedExpression) o;
        return expression.equals(that.expression) && alias.equals(that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, alias);
    }

    @Override
    public String toString() {
        return expression + " AS " + alias;
    }
}
