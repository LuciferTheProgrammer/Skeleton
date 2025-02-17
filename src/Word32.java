// The Word32 class, where a word is represented by an array of 32 bits.
public class Word32 {

    // An array of bits.
    public Bit[] word32;

    /**
     * The constructor creates an array of 32 bits and initializes each bit to hold a value of FALSE.
     */
    public Word32() {
        word32 = new Bit[32];
        for(int i = 0; i < 32; i++) {
            word32[i] = new Bit(false);
        }
    }

    /**
     * The constructor takes in an array of bits and assigns it to the instance field.
     *
     * @param in The array of bits.
     */
    public Word32(Bit[] in) {
        word32 = in;
    }

    /**
     * This method takes in a halfword and then sets its 16 bits to have the same values
     * as the corresponding bits of the current longword instance which contains 32 bits.
     *
     * @param result The resulting instance to hold bits of size 16.
     */
    public void getTopHalf(Word16 result) {
        for(int i = 0; i < 16; i++) {
            result.word16[i].assign(this.word32[i].getValue());
        }
    }

    /**
     * This method takes in a halfword and then sets its 16 bits to have the same values
     * as bits 16-31 of the current longword instance which contains 32 bits.
     *
     * @param result The resulting instance to hold bits of size 16.
     */
    public void getBottomHalf(Word16 result) {
        for(int i = 0; i < 16; i++) {
            result.word16[i].assign(this.word32[i + 16].getValue());
        }
    }

    /**
     * This method sets the bit values of the result to be the same as the values of the current
     * instance.
     *
     * @param result The resulting instance to hold bits of size 32.
     */
    public void copy(Word32 result) {
        for(int i = 0; i < 32; i++) {
            result.word32[i].assign(this.word32[i].getValue());
        }
    }

    /**
     * The instance version of the "equals" method calls the static version of "equals" to
     * check if the bits of the current instance holds the same values as the bits of the other instance.
     *
     * @param other The other instance of bits.
     * @return The status of whether the two instances are the same or not.
     */
    public boolean equals(Word32 other) {
        return Word32.equals(this, other);
    }

    /**
     * The static version of the "equals" method checks if the bits of the instance a holds the same values
     * as the bits of instance b.
     *
     * @param a The array of bits for instance a.
     * @param b The array of bits for instance b.
     * @return The status of whether the two instances are the same or not
     */
    public static boolean equals(Word32 a, Word32 b) {
        for(int i = 0; i < 32; i++) {
            if(a.word32[i].getValue() != b.word32[i].getValue())
                return false;
        }
        return true;
    }

    /**
     * This method takes in a number and a bit result and sets the resulting bit to hold the same
     * value as one of the bits of the current instance using the number as an index.
     *
     * @param n The index.
     * @param result The result bit.
     */
    public void getBitN(int n, Bit result) {
        result.assign(word32[n].getValue());
    }

    /**
     * This method takes in a number and a bit source and uses the number as an index to set one of the
     * bits of the current instance to hold the same value as the source bit.
     *
     * @param n The index.
     * @param source The source bit.
     */
    public void setBitN(int n, Bit source) {
        this.word32[n].assign(source.getValue());
    }

    /**
     * The instance version of the "and" method calls the static version of "and" which sets the resulting
     * bits of a longword to TRUE if both the corresponding bits of the current and other instance hold TRUE
     * and sets the resulting bits of the longword to FALSE otherwise.
     *
     * @param other The other instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public void and(Word32 other, Word32 result) {
        Word32.and(this, other, result);
    }

    /**
     * The static version of the "and" method sets the resulting bits of a longword to TRUE if both
     * the corresponding bits of instance a and instance b hold TRUE and sets the resulting bits
     * of the longword to FALSE otherwise.
     *
     * @param a The instance to hold bits of size 32.
     * @param b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void and(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.word32[i].and(b.word32[i], result.word32[i]);
        }
    }

    /**
     * The instance version of the "or" method calls the static version of "or" which sets the resulting
     * bits of a longword to FALSE if both the corresponding bits of the current and other instance
     * hold FALSE and sets the resulting bits of the longword to TRUE otherwise.
     *
     * @param other The other instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public void or(Word32 other, Word32 result) {
        Word32.or(this, other, result);
    }

    /**
     * The static version of the "or" method sets the resulting bits of a longword to FALSE if both the
     * corresponding bits of instance a and instance b hold FALSE and sets the resulting bits of the longword
     * to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 32.
     * @param b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void or(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.word32[i].or(b.word32[i], result.word32[i]);
        }
    }

    /**
     * The instance version of the "xor" method calls the static version of "xor" which sets the resulting
     * bits of a longword to FALSE if both the corresponding bits of the current and other instance
     * hold the same value and sets the resulting bits of the longword to TRUE otherwise.
     *
     * @param other The other instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public void xor(Word32 other, Word32 result) {
        Word32.xor(this, other, result);
    }

    /**
     * The static version of the "xor" method sets the resulting bits of a longword to FALSE if both
     * the corresponding bits of instance a and instance b hold the same value and sets the resulting bits
     * of the longword to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 32.
     * @param b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void xor(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.word32[i].xor(b.word32[i], result.word32[i]);
        }
    }

    /**
     * The instance version of the "not" method calls the static version of "not" which sets the resulting
     * bits of a longword to FALSE if the corresponding bits of the current instance hold the value TRUE
     * and sets the resulting bits of the longword to TRUE otherwise.
     *
     * @param result The resulting instance to hold bits of size 32.
     */
    public void not( Word32 result) {
        Word32.not(this, result);
    }

    /**
     * The static version of the "not" method sets the resulting bits of a longword to FALSE if the
     * corresponding bits of the instance a hold the value TRUE and sets the resulting bits of the
     * longword to TRUE otherwise.
     *
     * @param a The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void not(Word32 a, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.word32[i].not(result.word32[i]);
        }
    }

    /**
     * The method to display the contents of the longword, the values of its 32 bits.
     *
     * @return The values of the 32 bits.
     */
    public String toString() {
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for (Bit bit : word32) {
            sb.append(bit.toString());
            counter++;
            if(counter < word32.length)
                sb.append(",");
        }
        return sb.toString();
    }
}
