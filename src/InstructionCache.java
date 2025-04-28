
// The Instruction Cache is responsible for reading instructions from L2 Cache, storing a block of instructions,
// and then returning those instructions to the processor every time the processor fetches for an instruction.
public class InstructionCache {

    // The L2 Cache instance to refill instructions to the Instruction Cache.
    private L2Cache l2Cache;

    // The array of slots in the Instruction Cache to store 8-word instructions.
    private Word32[] slots;

    // To hold which block address is in the Cache.
    private Word32 tag;

    // The program counter (PC).
    private Word32 address = new Word32();

    // Resulting value.
    private Word32 value = new Word32();


    /**
     * This constructor takes in an L2 Cache instance and sets it to the L2 Cache instance field.
     * and also initializes the 8-word block for the Cache. Finally, sets the tag to an empty value
     * by default, -1.
     *
     * @param l2cache The L2 Cache instance.
     */
   public InstructionCache(L2Cache l2cache) {
       this.l2Cache = l2cache;
       slots = new Word32[8];
       for(int i = 0; i < 8; i++) {
           slots[i] = new Word32();
       }
       tag = new Word32();
       TestConverter.fromInt(-1, tag);
   }

    /**
     * This method uses the program counter to compute the start address of the 8-word block
     * of the Cache. Then it compares that value to the converted tag value to check if there
     * is a Cache hit, meaning if the desired block is already loaded, if it is then 10 clock cycles
     * are added and then the target word is retrieved within the block and copied to the value field where
     * it can be fetched. Otherwise, the Instruction Cache consults with the L2 Cache where
     * L2 refills the entire 8-word block of the Instruction Cache with the desired word. Right after,
     * 50 clock cycles are added and then the target word is retrieved and copied to the value field.
     * The tag is also updated.
     *
     */
   public void read() {
       int source = TestConverter.toInt(address);
       int startBlock = (source / 8) * 8;
       int tagHolder = TestConverter.toInt(tag);
       if(tagHolder == startBlock) {
           Processor.currentClockCycle += 10;
       }
       else {
           Word32[] temp = l2Cache.L2_read(source);
           for(int i = 0; i < 8; i++) {
               temp[i].copy(slots[i]);
           }
           TestConverter.fromInt(startBlock, tag);
            Processor.currentClockCycle += 50;
       }
       int target = source % 8;
       slots[target].copy(value);
   }

    /**
     * This method takes in the program counter and converts it to a Word32 instance to be
     * stored as the address.
     *
     * @param PC The program counter.
     */
   public void setAddress(int PC) {
       TestConverter.fromInt(PC, address);
   }

    /**
     * This takes in a Word32 instance and copies the result stored in value to that
     * instance.
     *
     * @param container The value to copy to.
     */
   public void getValue(Word32 container) {
       value.copy(container);
   }
}
