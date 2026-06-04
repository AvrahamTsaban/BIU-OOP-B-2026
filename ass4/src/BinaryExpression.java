import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

/**
 * An abstract class representing a binary expression in a boolean expression tree.
 * It contains two sub-expressions (first and second) and provides common functionality for evaluating the expression,
 * getting the variables in the expression, and replacing variables with other expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public abstract class BinaryExpression extends BaseExpression {
    /** The first expression in the binary expression. */
    private final Expression first;
    /** The second expression in the binary expression. */
    private final Expression second;

    /**
     * Constructor for BinaryExpression class.
     * @param first the first expression in the binary expression
     * @param second the second expression in the binary expression
     */
    public BinaryExpression(Expression first, Expression second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first expression in the binary expression.
     * @return the first expression in the binary expression
     */
    protected final Expression getFirst() {
        return this.first;
    }

    /**
     * Get the second expression in the binary expression.
     * @return the second expression in the binary expression
     */
    protected final Expression getSecond() {
        return this.second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        Boolean firstValue = this.first.evaluate(assignment);
        Boolean secondValue = this.second.evaluate(assignment);
        return this.binaryOperation(firstValue, secondValue);
    }

    /**
     * Evaluate the binary expression with the given boolean values for the first and second expressions.
     * @param firstValue the boolean value of the first expression
     * @param secondValue the boolean value of the second expression
     * @return the result of evaluating the binary expression with the given boolean values
     */
    protected abstract Boolean binaryOperation(Boolean firstValue, Boolean secondValue);

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<String> getVariables() {
        Set<String> variablesSet = new HashSet<>();
        variablesSet.addAll(this.first.getVariables());
        variablesSet.addAll(this.second.getVariables());
        return new ArrayList<>(variablesSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        String a = first.toString();
        String b = second.toString();
        char op = this.getOperatorSymbol();
        return "(" + a + " " + op + " " + b + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression assign(String var, Expression expression) {
        Expression newFirst = this.first.assign(var, expression);
        Expression newSecond = this.second.assign(var, expression);
        return this.createNewInstance(newFirst, newSecond);
    }

    /**
     * Create a new instance of the same binary expression with the given first and second expressions.
     * @param first the first expression for the new instance
     * @param second the second expression for the new instance
     * @return a new instance of the binary expression with the given first and second expressions
     */
    protected abstract BinaryExpression createNewInstance(Expression first, Expression second);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Nand nandify() {
        Expression nandFormFirst = this.first.nandify();
        Expression nandFormSecond = this.second.nandify();
        return this.nandifySelf(nandFormFirst, nandFormSecond);
    }

    /**
     * Nandify the expression itself, with the given nandified first and second expressions.
     * @param first first nandified expression
     * @param second second nandified expression
     * @return brand new, fully nandified expression
     */
    protected abstract Nand nandifySelf(Expression first, Expression second);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Nor norify() {
        Expression norFormFirst = this.first.norify();
        Expression norFormSecond = this.second.norify();
        return this.norifySelf(norFormFirst, norFormSecond);
    }

    /**
     * Norify the expression itself, with the given norified first and second expressions.
     * @param first first norified expression
     * @param second second norified expression
     * @return brand new, fully norified expression
     */
    protected abstract Nor norifySelf(Expression first, Expression second);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression simplify() {
        Expression simplifiedFirst = this.first.simplify();
        Expression simplifiedSecond = this.second.simplify();
        InternalValue firstValue = simplifiedFirst.getInternalValue();
        InternalValue secondValue = simplifiedSecond.getInternalValue();
        return this.simplifySelf(firstValue, secondValue, simplifiedFirst, simplifiedSecond);
    }


    /**
     * Simplify the expression itself, with the given simplified first and second expressions.
     * @param firstValue the internal value of the first expression (T, F, or UNASSIGNED)
     * @param secondValue the internal value of the second expression (T, F, or UNASSIGNED)
     * @param simplifiedFirst the simplified first expression
     * @param simplifiedSecond the simplified second expression
     * @return a simplified version of the expression
     */
    protected abstract Expression simplifySelf(InternalValue firstValue, InternalValue secondValue,
        Expression simplifiedFirst, Expression simplifiedSecond);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        BinaryExpression otherBinary = (BinaryExpression) other;
        if (this.first.equals(otherBinary.first)) {
            return this.second.equals(otherBinary.second);
        }
        if (this.first.equals(otherBinary.second)) {
            return this.second.equals(otherBinary.first);
        }
        return false;
    }
}
