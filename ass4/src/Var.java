import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A class representing a variable in a boolean expression.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Var extends NonLogic {
    /** The name of the variable. */
    private final String name;

    /**
     * Constructor for Var class.
     * @param name the name of the variable
     */
    public Var(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        if (assignment.containsKey(this.name)) {
            return assignment.get(this.name);
        } else {
            throw new Exception("Variable " + this.name + " needs an assignment.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getVariables() {
        List<String> variables = new ArrayList<String>();
        variables.add(this.name);
        return variables;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public Expression assign(String var, Expression expression) {
        if (var.equals(this.name)) {
            return expression;
        } else {
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Var otherVar = (Var) other;
        if (this.name.equals(otherVar.name)) {
            return true;
        }
        return false;
    }
}