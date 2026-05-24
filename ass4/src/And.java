/**
 * A class representing a logical AND operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class And extends BinaryExpression {
    /**
     * Constructor for And class.
     * @param first the first expression in the and expression
     * @param second the second expression in the and expression
     */
    public And(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return firstValue && secondValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return '&';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new And(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        Nand nandExpressions = new Nand(first, second);
        Nand negatedNand = new Nand(nandExpressions, nandExpressions);
        return negatedNand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        Nor negatedFirst = new Nor(first, first);
        Nor negatedSecond = new Nor(second, second);
        Nor norExpressions = new Nor(negatedFirst, negatedSecond);
        return norExpressions;
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
        if (firstValue == InternalValue.F || secondValue == InternalValue.F) {
            return new Val(false);
        }
        if (firstValue == InternalValue.T) {
            return simplifiedSecond;
        }
        if (secondValue == InternalValue.T) {
            return simplifiedFirst;
        }
        return this.createNewInstance(simplifiedFirst, simplifiedSecond);
    }
}
