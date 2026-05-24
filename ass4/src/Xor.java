/**
 * A class representing a logical XOR operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Xor extends BinaryExpression {
    /**
     * Constructor for Xor class.
     * @param first the first expression in the xor expression
     * @param second the second expression in the xor expression
     */
    public Xor(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return firstValue ^ secondValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return '^';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new Xor(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        Nand nandBoth = new Nand(first, second);
        Nand nandWithFirst = new Nand(first, nandBoth);
        Nand nandWithSecond = new Nand(second, nandBoth);
        Nand nandCombined = new Nand(nandWithFirst, nandWithSecond);
        return nandCombined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        Nor norOriginals = new Nor(first, second);
        Nor negatedFirst = new Nor(first, first);
        Nor negatedSecond = new Nor(second, second);
        Nor norNegated = new Nor(negatedFirst, negatedSecond);
        Nor norCombined = new Nor(norOriginals, norNegated);
        return norCombined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Expression simplifySelf(InternalValue firstValue, InternalValue secondValue,
            Expression simplifiedFirst, Expression simplifiedSecond) {
        if (simplifiedFirst.equals(simplifiedSecond)) {
            return new Val(false);
        }
        if (firstValue == InternalValue.F) {
            return simplifiedSecond;
        }
        if (secondValue == InternalValue.F) {
            return simplifiedFirst;
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
