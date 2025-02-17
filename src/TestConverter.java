// This is the TestConverter Class which converts an integer to a longword, represented
// by 32 bits and to convert a longword to an integer.
public class TestConverter {

    /**
     * This method takes in a number which is then converted to a longword, respresented
     * by an array of 32 bits. This is first achieved by taking in a number, if the number is negative
     * then it is added to the value of 2^32 and if not then it remains the same. Then, the value
     * is divided by a power of 2 dependent on the current bit position which is then used to
     * take mod 2 to determine the remainder. If the remainder returned is 1, the current bit is set
     * to TRUE and otherwise it's set to FALSE.
     *
     * @param value The number to be converted into a longword.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void fromInt(int value, Word32 result) {

        long upTo32 = calculatePowersOfTwo(32);
        long valueHolder = value;
        if(valueHolder < 0)
            valueHolder += upTo32;
        for(int i = 0; i < 32; i++) {
            long masking = calculatePowersOfTwo(31 - i);
            long remainder = (valueHolder / masking) % 2;
            if (remainder == 1)
                result.word32[i].assign(Bit.boolValues.TRUE);
            else
                result.word32[i].assign(Bit.boolValues.FALSE);
        }
    }

    /**
     * This method takes in a longword and converts it into an integer. This is first achieved by
     * checking if the left most or most significant bit is set to TRUE/1 which means the value
     * will be negative which sets the container to hold a value of 2^32, else the container is set to 0.
     * Then, for every bit that is TRUE/1 we add the value of 2 raised to the power of current bit
     * position to the total sum. Once we have the final accumulated sum it's subtracted from the
     * container and the final result is returned as the converted integer value.
     *
     * @param value The longword to be converted into an integer.
     * @return The resulting integer.
     */
    public static int toInt(Word32 value) {
        long total = 0;
        long holder = 0;
        if(value.word32[0].getValue() == Bit.boolValues.TRUE)
           holder = calculatePowersOfTwo(32);
        for(int i = 0; i < 32; i++) {
            if(value.word32[i].getValue() == Bit.boolValues.TRUE)
                total += calculatePowersOfTwo(31 - i);
        }
        int newTotal = (int) (total - holder);
        return newTotal;
    }

    /**
     * This method takes in a number which determines the number of times the base is multiplied
     * by 2 or to take the powers of 2.
     *
     * @param value The frequency the base is multiplied by 2.
     * @return The accumulated value.
     */
    public static long calculatePowersOfTwo(int value) {
        long accumulator = 1;
        for(int i = 0; i < value ; i++) {
            accumulator *= 2;
        }
        return accumulator;
    }
}