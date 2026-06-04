import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * An abstract class representing a base expression in a boolean expression tree.
 * It provides common functionality for evaluating the expression, getting the variables in the expression,
 * and replacing variables with other expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public abstract class BaseExpression implements Expression {
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
     * Get the operator symbol for the binary expression
     * (e.g., {@code &} for and, | for or, ^ for xor, ~ for not, etc.).
     * @return the operator symbol for the binary expression
     */
    protected abstract char getOperatorSymbol();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Expression assign(String var, Expression expression);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Nand nandify();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Nor norify();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Expression simplify();

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression applyNot() {
        return new Not(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final InternalValue getInternalValue() {
        if (getVariables().isEmpty()) {
            try {
                Boolean value = this.evaluate();
                return value ? InternalValue.T : InternalValue.F;
            } catch (Exception e) {
                return InternalValue.UNASSIGNED;
            }
        }
        return InternalValue.UNASSIGNED;
    }
}
