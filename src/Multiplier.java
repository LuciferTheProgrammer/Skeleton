// This is the Multiplier Class which takes two of our longwords or Word32 bit instances
// and proceeds to multiply their corresponding bits.
public class Multiplier {

    /**
     * This method proceeds to take two longwords/Word32 instances and multiply the corresponding
     * bits. This starts by checking if the current bit at instance b is TRUE/1, then instance
     * a is left shifted by 31 - the current bit position.Then the newly shifted instance a is added to the
     * result instance to get the newly accumulated sum which is then copied to the resulting instance
     * for further processing by the next bit.
     *
     * @param a The instance to hold bits of size 32.
     * @param b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void multiply(Word32 a, Word32 b, Word32 result) {
        Word32 aShifted = new Word32();
        Word32 sum = new Word32();
        for(int i = 0; i < 32; i++) {
            if(b.word32[i].getValue() == Bit.boolValues.TRUE) {
                Shifter.LeftShift(a, 31 - i, aShifted);
                Adder.add(result, aShifted, sum);
                sum.copy(result);
            }
        }
    }
}
