/**
 * A class representing a logical NOT operation on a boolean expression.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Not extends UnaryExpression {
    /**
     * Constructor for Not class.
     * @param expression the expression to negate
     */
    public Not(Expression expression) {
        super(expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean unaryOperation(Boolean value) {
        return !value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return '~';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UnaryExpression createNewInstance(Expression expression) {
        return new Not(expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression expression) {
        return new Nand(expression, expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression expression) {
        return new Nor(expression, expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Expression simplifySelf(InternalValue inputValue, Expression simplifiedInput) {
        return simplifiedInput.applyNot();
    }

    /**
     * {@inheritDoc}
     * Special simplification for a Not expression: applying Not again should return the original input expression.
     */
    @Override
    public Expression applyNot() {
        return this.getInput();
    }
}
