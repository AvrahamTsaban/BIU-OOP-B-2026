import java.util.List;
import java.util.Map;

/**
 * The Expression interface represents a boolean expression that can be evaluated and manipulated.
 * It provides methods for evaluating the expression with variable assignments, getting the variables in the expression,
 * and replacing variables with other expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public interface Expression {
    /**
     * Evaluate the expression using the variable values provided in the assignment, and return the result.
     * If the expression contains a variable which is not in the assignment, an exception is thrown.
     * @param assignment a map of variable names to their boolean values
     * @return the result of evaluating the expression with the given variable assignments
     * @throws Exception if the expression contains a variable that is not in the assignment
     */
    Boolean evaluate(Map<String, Boolean> assignment) throws Exception;

    /**
     * Like the `evaluate(assignment)` method above, but uses an empty assignment.
     * @return the result of evaluating the expression with an empty assignment
     * @throws Exception if the expression contains a variable so it needs an assignment
     */
    Boolean evaluate() throws Exception;

    /**
     * Get a list of the variables in the expression.
     * @return a list of the variables in the expression
     */
    List<String> getVariables();

    /**
     * Get a string representation of the expression.
     * @return a string representation of the expression
     */
    String toString();

    /**
     * Returns a new expression in which all occurrences of the variable
     * var are replaced with the provided expression (Does not modify the
     * current expression).
     * @param var the variable to be replaced
     * @param expression the expression to replace the variable with
     * @return a new expression with the variable replaced
     */
    Expression assign(String var, Expression expression);

    /**
     * Returns the expression tree resulting from converting all the operations to the logical Nand operation.
     * @return the expression converted to use only the Nand operation
     */
    Expression nandify();

    /**
     * Returns the expression tree resulting from converting all the operations to the logical Nor operation.
     * @return the expression converted to use only the Nor operation
     */
    Expression norify();

    /**
     * Returns a simplified version of the current expression.
     * @return a simplified version of the expression
     */
    Expression simplify();

    /**
     * Returns a new expression that is the negation of the current expression.
     * <p>Useful for simplifying negated expressions (implemented ~~A = A,
     * possible future implementation: de Morgan's laws)</p>
     * @return a new expression that is the negation of the current expression
     */
    Expression applyNot();
}
