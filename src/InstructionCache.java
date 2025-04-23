
// The Instruction Cache is responsible for reading instructions from L2 Cache and then
// returning those instructions to the processor every time the processor fetches an instruction.
public class InstructionCache {

    // To count the clock cycle in the Instruction Cache.
    public int InstructionCacheClockCycle;

    // The L2 Cache instance to read instructions from the main memory to the Instruction Cache.
    public L2Cache l2Cache;

    // The array of slots in the Instruction Cache to store 8-word instructions and an
    // extra slot to store the address represented by the first of these 8-word instructions.
    public Word32[] slots;

    /**
     * This constructor takes in an L2 Cache and sets it to the L2 Cache instance field. It
     * also sets the array of slots to a size of 9 and initializes each slot to a default Word32
     * instance.
     *
     * @param l2Cache The L2 Cache instance.
     */
    public InstructionCache(L2Cache l2Cache) {
        this.l2Cache = l2Cache;
        slots = new Word32[9] ;
        for(int i = 0; i < slots.length; i++) {
            slots[i] = new Word32();
        }
        InstructionCacheClockCycle = 0;
        TestConverter.fromInt(-1, slots[0]);

    }

    public Word32 read(int address) {
        int baseAddress = address - (address % 8);
        int target = address % 8;
        int firstAddress = TestConverter.toInt(slots[0]);
        if(firstAddress == baseAddress) {
            InstructionCacheClockCycle += 10;
            return slots[1 + target];
        }
        else {
            Word32[] scope = l2Cache.read_Through(baseAddress);
            TestConverter.fromInt(baseAddress, slots[0]);
            InstructionCacheClockCycle += l2Cache.L2CacheClockCycle;
            l2Cache.L2CacheClockCycle = 0;
            for(int i = 0; i < 8; i++) {
                scope[i].copy(slots[1 + i]);
            }
            InstructionCacheClockCycle += 50;
            return slots[1 + target];
        }
    }
}