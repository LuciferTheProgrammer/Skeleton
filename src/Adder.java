// This is the Adder Class which takes two of our longwords or Word32 bit instances
// and proceeds to add/subtract their corresponding bits.
public class Adder {

    /**
     * This method proceeds to take two longwords/Word32 instances and subtract them
     * bit by bit. This is achieved by first negating all the bits of instance b
     * using the bit wise NOT method and then creates an instance of the Word32 which
     * represents the value 1. Then the negated instance b and 1 are added to instance
     * a to get the final resulting bits. [DAVID!]
     *
     * @param a The instance to hold bits of size 32.
     * @param b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */

    public static void subtract(Word32 a, Word32 b, Word32 result) {
        Word32 notB = new Word32();
        Word32 add1 = new Word32();
        Word32 newB = new Word32();
        b.not(notB);
        add1.word32[31].assign(Bit.boolValues.TRUE);
        Adder.add(notB, add1, newB);
        Adder.add(a, newB, result);
    }

    /**
     * This method proceeds to take two longwords/Word32 instances and add them bit by bit which starts
     * from the right most bit or the least significant bit and proceeds to add with a carry bit
     * all the way through the left most bit or the most significant bit. This is achieved by
     * first using the XOR bit wise operator for the corresponding bits of instance a, b,
     * and the carry bit to get the sum bit which is then stored to the result bit. Furthermore,
     * the bit wise AND is used for the corresponding bits of instance a and b along with the carry bit
     * and the result bit from the XOR of the corresponding bits of a and b. Finally, the bit wise OR
     * is utilized from both resulting bits which is then stored to the carry bit for the next
     * bit addition in the loop.
     *
     * @param a The instance to hold bits of size 32.
     * @param b b The instance to hold bits of size 32.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void add(Word32 a, Word32 b, Word32 result) {
        Bit carryIn= new Bit(false);
        for(int i = 31; i >= 0; i--) {
            Bit carryOut= new Bit(false);
            Bit sumBit = new Bit(false);
            Bit aXORb = new Bit(false);
            Bit aANDb = new Bit(false);
            Bit aXORbANDCarryIn = new Bit(false);
            a.word32[i].xor(b.word32[i], aXORb);
            aXORb.xor(carryIn, sumBit);
            result.word32[i].assign(sumBit.getValue());
            a.word32[i].and(b.word32[i], aANDb);
            aXORb.and(carryIn, aXORbANDCarryIn);
            aANDb.or(aXORbANDCarryIn, carryOut);
            carryIn.assign(carryOut.getValue());
        }
    }
}
