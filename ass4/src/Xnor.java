/**
 * A class representing a logical XNOR operation between two boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Xnor extends BinaryExpression {
    /**
     * Constructor for Xnor class.
     * @param first the first expression in the xnor expression
     * @param second the second expression in the xnor expression
     */
    public Xnor(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean binaryOperation(Boolean firstValue, Boolean secondValue) {
        return !(firstValue ^ secondValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected char getOperatorSymbol() {
        return '#';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryExpression createNewInstance(Expression first, Expression second) {
        return new Xnor(first, second);
    }

    /**
     * {@inheritDoc}
     * We could simplify the XNOR nandification by building a XOR, nandifying it and negating it,
     * what would look like having only 2 logic gates.
     * The way was chosen is superior due to having less layers of Nands (even though both has 5 nands in total),
     * What may make a little bit more readable output (using toString) later.
     */
    @Override
    protected Nand nandifySelf(Expression first, Expression second) {
        Nand nandOriginals = new Nand(first, second);
        Nand negatedFirst = new Nand(first, first);
        Nand negatedSecond = new Nand(second, second);
        Nand nandNegated = new Nand(negatedFirst, negatedSecond);
        Nand nandCombined = new Nand(nandOriginals, nandNegated);
        return nandCombined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Nor norifySelf(Expression first, Expression second) {
        Nor norOriginals = new Nor(first, second);
        Nor norWithFirst = new Nor(first, norOriginals);
        Nor norWithSecond = new Nor(second, norOriginals);
        Nor norCombined = new Nor(norWithFirst, norWithSecond);
        return norCombined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Expression simplifySelf(InternalValue firstValue, InternalValue secondValue,
            Expression simplifiedFirst, Expression simplifiedSecond) {
        if (simplifiedFirst.equals(simplifiedSecond)) {
            return new Val(true);
        }
        return this.createNewInstance(simplifiedFirst, simplifiedSecond);
    }
}
