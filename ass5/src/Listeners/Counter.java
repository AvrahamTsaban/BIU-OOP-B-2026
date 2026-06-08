package Listeners;

/**
 * The Counter class is a simple utility class that keeps track of a count value.
 * It provides methods to increase, decrease, and retrieve the current count.
 * <p>Design note: Implemented only for consistency with the specifications, as it is equivalent to Integer class.</p>
 * @author Avraham Tsaban
 * @version 1.5
 * @since 2024-06-05
 */
public class Counter {
    private int count;

    /**
     * Constructor for Counter class.
     */
    public Counter() {
        this.count = 0;
    }
    /**
     * Increase the counter by a specified number.
     * @param number the amount to increase the counter by
     */
    public void increase(int number) {
        this.count += number;
    }
    /**
     * Decrease the counter by a specified number.
     * @param number the amount to decrease the counter by
     */
    public void decrease(int number) {
        this.count -= number;
    }

    /**
     * Get the current value of the counter.
     * @return current count value
     */
    public int getValue() {
        return this.count;
    }
}
