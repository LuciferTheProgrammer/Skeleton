
// The Instruction Cache is responsible for reading instructions from L2 Cache and then
// returning those instructions to the processor every time the processor fetches for an instruction.
public class InstructionCache {

    // The L2 Cache instance to read instructions from the main memory to the Instruction Cache.
    private L2Cache l2Cache;

    // The array of slots in the Instruction Cache to store 8-word instructions and an
    // extra slot to store the address represented by the first of these 8-word instructions.
    private Word32[] slots;

    private Word32 tag;

    private Word32 address = new Word32();

    private Word32 value = new Word32();


   public InstructionCache(L2Cache l2cache) {
       this.l2Cache = l2cache;
       slots = new Word32[8];
       for(int i = 0; i < 8; i++) {
           slots[i] = new Word32();
       }
       tag = new Word32();
       TestConverter.fromInt(-1, tag);
   }

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
   public void setAddress(int PC) {
       TestConverter.fromInt(PC, address);
   }
   public void getValue(Word32 container) {
       value.copy(container);
   }
}
