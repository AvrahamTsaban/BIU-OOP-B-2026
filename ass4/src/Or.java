/**
 * A class representing a logical OR operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Or extends BinaryExpression {
    /**
     * Constructor for Or class.
     * @param first the first expression in the or expression
     * @param second the second expression in the or expression
     */
    public Or(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return firstValue || secondValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return '|';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new Or(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        Nand negatedFirst = new Nand(first, first);
        Nand negatedSecond = new Nand(second, second);
        Nand nandCombined = new Nand(negatedFirst, negatedSecond);
        return nandCombined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        Nor norExpressions = new Nor(first, second);
        Nor negatedNor = new Nor(norExpressions, norExpressions);
        return negatedNor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Expression simplifySelf(InternalValue firstValue, InternalValue secondValue,
            Expression simplifiedFirst, Expression simplifiedSecond) {
        if (simplifiedFirst.equals(simplifiedSecond)) {
            return simplifiedFirst;
        }
        if (firstValue == InternalValue.T || secondValue == InternalValue.T) {
            return new Val(true);
        }
        if (firstValue == InternalValue.F) {
            return simplifiedSecond;
        }
        if (secondValue == InternalValue.F) {
            return simplifiedFirst;
        }
        return this.createNewInstance(simplifiedFirst, simplifiedSecond);
    }
}
