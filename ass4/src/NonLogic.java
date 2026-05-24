import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An abstract class representing a non-logical expression (a variable or a boolean value).
 * <p>Implements the default behavior for nandify, norify, and simplify (returns itself),
 * and applyNot (returns a new Not expression with this as the operand).</p>
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public abstract class NonLogic implements Expression {
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Boolean evaluate(Map<String, Boolean> assignment) throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    public final Boolean evaluate() throws Exception {
        Map<String, Boolean> emptyAssignment = Collections.emptyMap();
        return this.evaluate(emptyAssignment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<String> getVariables();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String toString();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Expression assign(String var, Expression expression);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression nandify() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression norify() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression simplify() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression applyNot() {
        return new Not(this);
    }
}
