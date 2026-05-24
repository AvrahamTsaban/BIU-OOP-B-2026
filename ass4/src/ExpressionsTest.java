import java.util.Map;
import java.util.HashMap;

/**
 * A class for testing the string representation of various expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com, 207088733
 * @version 1.0
 * @since 2024-05-24
 */
public final class ExpressionsTest {
    /** Private constructor to prevent instantiation. */
    private ExpressionsTest() { }

    /**
     * Main method for testing the string representation of various expressions.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create an expression with at least three variables
        Expression andExpression = new And(new Var("x"), new Var("y"));
        Expression orExpression = new Or(new Var("x"), new And(new Var("z"), new Val(true)));
        Expression nandExpressions = new Nand(andExpression, orExpression);
        // Print the expression
        System.out.println(nandExpressions.toString());

        // Print the value of the expression with an assignment to every variable
        Map<String, Boolean> assignment = new HashMap<>();
        assignment.put("x", true);
        assignment.put("y", false);
        assignment.put("z", true);
        try {
            System.out.println(nandExpressions.evaluate(assignment));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Print the Nandified version of the expression
        Expression fullyNandified = nandExpressions.nandify();
        System.out.println(fullyNandified.toString());

        // Print the Norified version of the expression
        Expression fullyNorified = nandExpressions.norify();
        System.out.println(fullyNorified.toString());

        // Print the simplified version of the expression
        Expression simplified = nandExpressions.simplify();
        System.out.println(simplified.toString());
    }
}