import java.util.List;
import java.util.Map;

/**
 * A class representing a boolean value.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public class Val extends NonLogic {
    /** The boolean value of the Val instance. */
    private final boolean value;

    /**
     * Constructor for Val class.
     * @param value the boolean value to initialize the Val instance with
     */
    public Val(boolean value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean evaluate(Map<String, Boolean> assignment) throws Exception {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getVariables() {
        return new java.util.ArrayList<String>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value ? "T" : "F";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Val otherVal = (Val) other;
        if (this.value == otherVal.value) {
            return true;
        }
        return false;
    }
}
