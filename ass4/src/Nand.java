/**
 * A class representing a logical NAND operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Nand extends BinaryExpression {
    /**
     * Constructor for Nand class.
     * @param first the first expression in the nand expression
     * @param second the second expression in the nand expression
     */
    public Nand(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return !(firstValue && secondValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return 'A';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new Nand(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        return new Nand(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        Nor negatedFirst = new Nor(first, first);
        Nor negatedSecond = new Nor(second, second);
        Nor norNegatedExpressions = new Nor(negatedFirst, negatedSecond);
        Nor negatedNor = new Nor(norNegatedExpressions, norNegatedExpressions);
        return negatedNor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Expression simplifySelf(InternalValue firstValue, InternalValue secondValue,
            Expression simplifiedFirst, Expression simplifiedSecond) {
        if (simplifiedFirst.equals(simplifiedSecond)) {
            return new Not(simplifiedFirst);
        }
        if (firstValue == InternalValue.F || secondValue == InternalValue.F) {
            return new Val(true);
        }
        if (firstValue == InternalValue.T) {
            return new Not(simplifiedSecond);
        }
        if (secondValue == InternalValue.T) {
            return new Not(simplifiedFirst);
        }
        return this.createNewInstance(simplifiedFirst, simplifiedSecond);
    }
}
