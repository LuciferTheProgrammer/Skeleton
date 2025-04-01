// The Word16 class, where a word is represented by an array of 16 bits.
public class Word16 {

    // An array of bits.
    public Bit[] word16;

    /**
     * The constructor creates an array of 16 bits and initializes each bit to hold a value of FALSE.
     */
    public Word16() {
        word16 = new Bit[16];
        for(int i = 0; i < 16; i++) {
            word16[i] = new Bit(false);
        }
    }

    /**
     * The constructor takes in an array of bits and assigns it to the instance field.
     *
     * @param in The array of bits.
     */
    public Word16(Bit[] in) {
        word16 = in;
    }

    /**
     * This method sets the bit values of the result to be the same as the values of the current
     * instance.
     *
     * @param result The resulting bits.
     */
    public void copy(Word16 result) {
        for(int i = 0; i < 16; i++) {
            result.word16[i].assign(this.word16[i].getValue());
        }
    }

    /**
     * This method takes in a number and a bit source and uses the number as an index to set one of the
     * bits of the current instance to hold the same value as the source bit.
     *
     * @param n The index.
     * @param source The source bit.
     */
    public void setBitN(int n, Bit source) {
        this.word16[n].assign(source.getValue());
    }

    /**
     * This method takes in a number and a bit result and sets the resulting bit to hold the same
     * value as one of the bits of the current instance using the number as an index.
     *
     * @param n The index.
     * @param result The result bit.
     */
    public void getBitN(int n, Bit result) {
        result.assign(word16[n].getValue());
    }

    /**
     * The instance version of the "equals" method calls the static version of "equals" to
     * check if the bits of the current instance holds the same values as the bits of the other instance.
     *
     * @param other The other instance of bits.
     * @return The status of whether the two instances are the same or not.
     */
    public boolean equals(Word16 other) {
        return Word16.equals(this, other);
    }

    /**
     * The static version of the "equals" method checks if the bits of the instance a holds the same values
     * as the bits of instance b.
     *
     * @param a The array of bits for instance a.
     * @param b The array of bits for instance b.
     * @return The status of whether the two instances are the same or not.
     */
    public static boolean equals(Word16 a, Word16 b) {
        for(int i = 0; i < 16; i++) {
            if(a.word16[i].getValue() != b.word16[i].getValue())
                return false;
        }
        return true;
    }

    /**
     * The instance version of the "and" method calls the static version of "and" which sets the resulting
     * bits of a halfword to TRUE if both the corresponding bits of the current and other instance hold TRUE
     * and sets the resulting bits of the halfword to FALSE otherwise.
     *
     * @param other The other instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public void and(Word16 other, Word16 result) {
        Word16.and(this, other, result);

    }

    /**
     * The static version of the "and" method sets the resulting bits of a halfword to TRUE if both
     * the corresponding bits of instance a and instance b hold TRUE and sets the resulting bits
     * of the halfword to FALSE otherwise.
     *
     * @param a The instance to hold bits of size 16.
     * @param b The instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public static void and(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.word16[i].and(b.word16[i], result.word16[i]);
        }
    }

    /**
     * The instance version of the "or" method calls the static version of "or" which sets the resulting
     * bits of a halfword to FALSE if both the corresponding bits of the current and other instance
     * hold FALSE and sets the resulting bits of the halfword to TRUE otherwise.
     *
     * @param other The other instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public void or(Word16 other, Word16 result) {
        Word16.or(this, other, result);
    }

    /**
     * The static version of the "or" method sets the resulting bits of a halfword to FALSE if both the
     * corresponding bits of instance a and instance b hold FALSE and sets the resulting bits of the halfword
     * to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 16.
     * @param b The instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public static void or(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.word16[i].or(b.word16[i], result.word16[i]);
        }
    }

    /**
     * The instance version of the "xor" method calls the static version of "xor" which sets the resulting
     * bits of a halfword to FALSE if both the corresponding bits of the current and other instance
     * hold the same value and sets the resulting bits of the halfword to TRUE otherwise.
     *
     * @param other The other instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public void xor(Word16 other, Word16 result) {
        Word16.xor(this, other, result);
    }

    /**
     * The static version of the "xor" method sets the resulting bits of a halfword to FALSE if both
     * the corresponding bits of instance a and instance b hold the same value and sets the resulting bits
     * of the halfword to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 16.
     * @param b The instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public static void xor(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.word16[i].xor(b.word16[i], result.word16[i]);
        }
    }

    /**
     * The instance version of the "not" method calls the static version of "not" which sets the resulting
     * bits of a halfword to FALSE if the corresponding bits of the current instance hold the value TRUE
     * and sets the resulting bits of the halfword to TRUE otherwise.
     *
     * @param result The resulting instance to hold bits of size 16.
     */
    public void not( Word16 result) {
        Word16.not(this, result);
    }

    /**
     * The static version of the "not" method sets the resulting bits of a halfword to FALSE if the
     * corresponding bits of the instance a hold the value TRUE and sets the resulting bits of the
     * halfword to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 16.
     * @param result The resulting instance to hold bits of size 16.
     */
    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.word16[i].not(result.word16[i]);
        }
    }

    /**
     * The method to display the contents of the halfword, the values of its 16 bits.
     *
     * @return The values of the 16 bits.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : word16) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}