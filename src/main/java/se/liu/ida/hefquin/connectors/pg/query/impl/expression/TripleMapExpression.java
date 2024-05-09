package se.liu.ida.hefquin.connectors.pg.query.impl.expression;

import java.util.Objects;
import java.util.Set;

import se.liu.ida.hefquin.connectors.pg.query.CypherExpression;
import se.liu.ida.hefquin.connectors.pg.utils.CypherExpressionVisitor;

public class TripleMapExpression implements CypherExpression {

    protected final CypherVar source;
    protected final CypherVar edge;
    protected final CypherVar target;

    public TripleMapExpression(final CypherVar source, final CypherVar edge, final CypherVar target) {
        assert source != null;
        assert edge !=  null;
        assert target != null;

        this.source = source;
        this.edge = edge;
        this.target = target;
    }

    @Override
    public Set<CypherVar> getVars() {
        return Set.of(source, edge, target);
    }

    @Override
    public void visit(final CypherExpressionVisitor visitor) {
        source.visit(visitor);
        edge.visit(visitor);
        target.visit(visitor);
        visitor.visitTripleMap(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripleMapExpression that = (TripleMapExpression) o;
        return source.equals(that.source) && edge.equals(that.edge) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, edge, target);
    }

    @Override
    public String toString() {
        return "{" +
                "s: " + source +
                " , e: TYPE(" + edge +
                "), t: " + target +
                '}';
    }
}
