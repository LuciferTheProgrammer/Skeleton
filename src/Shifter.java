// This is the Shifter Class which shifts a given longword/Word32 instance either left
// or right by a given amount.
public class Shifter {

    /**
     * This method takes in a longword/Word32 instance and left shifts its bits by the given
     * amount. This is achieved by setting up a new index which is derived from the subtraction
     * of the current bit position from the amount to be shifted. If the new bit position is
     * greater or equal to 0, then a mask is created with that given bit position and a value of
     * TRUE/1 is stored. Finally, the bit wise AND is used for the current bit position for the word
     * source and for the new bit position for the mask which stores the value to the resulting
     * instance. The rest of the bits marked FALSE/0 after the operation are ignored.
     *
     * @param source The instance to hold bits of size 32.
     * @param amount The amount to shift.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        Word32 masking = new Word32();
        for(int i = 0; i < 32; i++) {
            int index2 = i - amount;
            if (index2 >= 0) {
                masking.word32[index2].assign(Bit.boolValues.TRUE);
                source.word32[i].and(masking.word32[index2], result.word32[index2]);
            }
        }
    }

    /**
     * This method takes in a longword/Word32 instance and right shifts its bits by the given
     * amount. This is achieved by setting up a new index which is derived from the addition
     * of the current bit position to the amount to be shifted. If the new bit position is
     * less than 32, then a mask is created with that given bit position and a value of
     * TRUE/1 is stored. Finally, the bit wise AND is used for the current bit position for the word
     * source and for the new bit position for the mask which stores the value to the resulting
     * instance. The rest of the bits marked FALSE/0 after the operation are ignored.
     *
     * @param source The instance to hold bits of size 32.
     * @param amount The amount to shift.
     * @param result The resulting instance to hold bits of size 32.
     */
    public static void RightShift(Word32 source, int amount, Word32 result) {
        Word32 masking = new Word32();
        for(int i = 0; i < 32; i++) {
            int index2 = i + amount;
            if (index2 < 32) {
                masking.word32[index2].assign(Bit.boolValues.TRUE);
                source.word32[i].and(masking.word32[index2], result.word32[index2]);
            }
        }
    }
}
