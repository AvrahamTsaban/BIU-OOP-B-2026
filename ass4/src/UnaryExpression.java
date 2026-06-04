import java.util.List;
import java.util.Map;

/**
 * An abstract class representing a unary expression in a boolean expression tree.
 * It contains one sub-expression (exp) and provides common functionality for evaluating the expression,
 * getting the variables in the expression, and replacing variables with other expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public abstract class UnaryExpression extends BaseExpression {
    /** The expression inside the unary expression. */
    private final Expression input;

    /**
     * Constructor for UnaryExpression class.
     * @param input the expression in the unary expression
     */
    public UnaryExpression(Expression input) {
        this.input = input;
    }

    /**
     * Get the expression in the unary expression.
     * @return the expression in the unary expression
     */
    protected final Expression getInput() {
        return this.input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        Boolean inputValue = this.input.evaluate(assignment);
        return this.unaryOperation(inputValue);
    }

    /**
     * Evaluate the unary expression with the given boolean value for the inside expression.
     * @param inputValue the boolean value of the expression in the unary expression
     * @return the result of evaluating the unary expression with the given boolean value
     */
    protected abstract Boolean unaryOperation(Boolean inputValue);

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<String> getVariables() {
        return this.input.getVariables();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        String exp = this.input.toString();
        char op = this.getOperatorSymbol();
        return "(" + op + "(" + exp + "))";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression assign(String var, Expression expression) {
        Expression newInput = this.input.assign(var, expression);
        return this.createNewInstance(newInput);
    }

    /**
     * Create a new instance of the unary expression with the given expression as the inside expression.
     * @param input the expression to use as the inside expression in the new instance of the unary expression
     * @return a new instance of the unary expression with the given expression as the inside expression
     */
    protected abstract UnaryExpression createNewInstance(Expression input);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Nand nandify() {
        Expression nandFormInput = this.input.nandify();
        return this.nandifySelf(nandFormInput);
    }

    /**
     * Nandify the expression itself, with the given nandified input expression.
     * @param nandFormInput the nandified input expression
     * @return brand new, fully nandified expression
     */
    protected abstract Nand nandifySelf(Expression nandFormInput);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Nor norify() {
        Expression norFormInput = this.input.norify();
        return this.norifySelf(norFormInput);
    }

    /**
     * Norify the expression itself, with the given norified input expression.
     * @param norFormInput the norified input expression
     * @return brand new, fully norified expression
     */
    protected abstract Nor norifySelf(Expression norFormInput);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Expression simplify() {
        Expression simplifiedInput = this.input.simplify();
        InternalValue inputValue = simplifiedInput.getInternalValue();
        return this.simplifySelf(inputValue, simplifiedInput);
    }

    /**
     * Simplify the expression itself, with the given simplified input expression.
     * @param inputValue the internal value of the input expression (T, F, or UNASSIGNED)
     * @param simplifiedInput the simplified input expression
     * @return a simplified version of the expression
     */
    protected abstract Expression simplifySelf(InternalValue inputValue, Expression simplifiedInput);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        UnaryExpression otherUnary = (UnaryExpression) other;
        if (this.input.equals(otherUnary.input)) {
            return true;
        }
        return false;
    }
}