/**
 * An enum representing internal values for boolean expressions.
 * @author Avraham Tsaban, avraham.tsaban@gmail.com
 * @version 1.0
 * @since 2024-05-24
 */
public enum InternalValue {
    /** Represents the boolean value true. */
    T,
    /** Represents the boolean value false. */
    F,
    /** Represents an unassigned value, e. g. an expression is dependent on an unassigned variable. */
    UNASSIGNED;
}
