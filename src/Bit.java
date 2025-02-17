// The Bit class, where a bit can either be represented as on or off.
public class Bit {

    // Enumerated datatype to be used. Either "TRUE" or "FALSE" to represent
    // "on" and "off" for the bit.
    public enum boolValues { FALSE, TRUE }

    // Instance member field to store the given enum datatype "boolValues".
    private boolValues holder;

    /**
     * This is the constructor which accepts a boolean as a parameter and uses this value
     * to correspondingly assign a value of "TRUE" or "FALSE" to the holder instance field.
     *
     * @param value The boolean value.
     */
    public Bit(boolean value) {
        if(value)
            holder = boolValues.TRUE;
        else
            holder = boolValues.FALSE;
    }

    /**
     * This method returns the value stored in the holder instance field.
     *
     * @return The value stored on holder.
     */
    public boolValues getValue() {
        return holder;
    }

    /**
     * This method takes in a value of an enum datatype "boolsValue" and assigns it to
     * the holder instance field.
     *
     * @param value The enum datatype boolsValue.
     */
    public void assign(boolValues value) {
        holder = value;
    }

    /**
     * The instance version of the "and" method calls the static version
     * of "and" which sets the result bit to TRUE if both the current bit and second bit hold TRUE
     * and sets the result bit to FALSE otherwise.
     *
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public void and(Bit b2, Bit result) {
        Bit.and(this, b2, result);

    }

    /**
     * The static version of the "and" method which sets the result bit to TRUE if both the first and
     * second bit hold TRUE and sets the result bit to FALSE otherwise.
     *
     * @param b1 The first bit.
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public static void and(Bit b1, Bit b2, Bit result) {
        if(b1.getValue() == boolValues.FALSE)
            result.assign(boolValues.FALSE);
        else if(b2.getValue() == boolValues.FALSE)
            result.assign(boolValues.FALSE);
        else
            result.assign(boolValues.TRUE);
    }

    /**
     * The instance version of the "or" method calls the static version of "or"
     * which sets the result bit to FALSE if both the current bit and second bit hold FALSE
     * and sets the result bit to TRUE otherwise.
     *
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public void or(Bit b2, Bit result) {
        Bit.or(this, b2, result);
    }

    /**
     * The static version of the "or" method which sets the result bit to FALSE
     * if both the first bit and second bit hold FALSE and sets the result bit to TRUE otherwise.
     *
     * @param b1 The first bit.
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public static void or(Bit b1, Bit b2, Bit result) {
        if(b1.getValue() == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else if(b2.getValue() == boolValues.TRUE)
            result.assign(boolValues.TRUE);
        else
            result.assign(boolValues.FALSE);
    }



    /**
     * The instance version of the "xor" method calls the static version of "xor" which sets
     * the result bit to FALSE if both the current bit and second bit hold the same value and sets the
     * result bit to TRUE otherwise.
     *
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public void xor(Bit b2, Bit result) {
        Bit.xor(this, b2, result);
    }

    /**
     * The static version of the "xor" method which sets the result bit to FALSE if both the first bit
     * and second bit hold the same value and sets the result bit to TRUE otherwise.
     *
     * @param b1 The first bit.
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public static void xor(Bit b1, Bit b2, Bit result) {
        if(b1.getValue() == b2.getValue())
            result.assign(boolValues.FALSE);
        else
            result.assign(boolValues.TRUE);
    }

    /**
     * The static version of the "not" method sets the result bit to FALSE if the second bit holds TRUE and
     * sets the result bit to TRUE otherwise.
     *
     * @param b2 The second bit.
     * @param result The result bit.
     */
    public static void not(Bit b2, Bit result) {
        if(b2.getValue() == boolValues.TRUE)
            result.assign(boolValues.FALSE);
        else
            result.assign(boolValues.TRUE);
    }

    /**
     * The instance version of the "not" method calls the static version of "not" which sets the result
     * bit to FALSE if the current bit holds TRUE and sets the result bit to TRUE otherwise.
     *
     * @param result The result bit.
     */
    public void not(Bit result) {
        Bit.not(this, result);
    }

    /**
     * The method to display the contents of the Bit as a String.
     *
     * @return The value of the Bit.
     */
    public String toString() {
        if(holder == boolValues.TRUE)
            return ("t");
        else
            return ("f");
    }
}
