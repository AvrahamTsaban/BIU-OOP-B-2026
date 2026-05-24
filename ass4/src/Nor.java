/**
 * A class representing a logical NOR operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Nor extends BinaryExpression {
    /**
     * Constructor for Nor class.
     * @param first the first expression in the nor expression
     * @param second the second expression in the nor expression
     */
    public Nor(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return !(firstValue || secondValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return 'V';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new Nor(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        Nand negatedFirst = new Nand(first, first);
        Nand negatedSecond = new Nand(second, second);
        Nand nandCombined = new Nand(negatedFirst, negatedSecond);
        Nand negatedNand = new Nand(nandCombined, nandCombined);
        return negatedNand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        return new Nor(first, second);
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
        if (firstValue == InternalValue.T || secondValue == InternalValue.T) {
            return new Val(false);
        }
        if (firstValue == InternalValue.F) {
            return new Not(simplifiedSecond);
        }
        if (secondValue == InternalValue.F) {
            return new Not(simplifiedFirst);
        }
        return this.createNewInstance(simplifiedFirst, simplifiedSecond);
    }
}
