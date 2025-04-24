
// The Instruction Cache is responsible for reading instructions from L2 Cache and then
// returning those instructions to the processor every time the processor fetches for an instruction.
public class InstructionCache {

    // The L2 Cache instance to read instructions from the main memory to the Instruction Cache.
    private L2Cache l2Cache;

    // The array of slots in the Instruction Cache to store 8-word instructions and an
    // extra slot to store the address represented by the first of these 8-word instructions.
    private Word32[] slots;

    /**
     * This constructor takes in an L2 Cache and sets it to the L2 Cache instance field. It
     * also sets the array of slots to a size of 9 and initializes each slot to a default Word32
     * instance. It also initializes the first slot to a value of -1 to represent that the
     * cache is empty at the moment.
     *
     * @param l2Cache The L2 Cache instance.
     */
    public InstructionCache(L2Cache l2Cache) {
        this.l2Cache = l2Cache;
        slots = new Word32[9] ;
        for(int i = 0; i < slots.length; i++) {
            slots[i] = new Word32();
        }
        TestConverter.fromInt(-1, slots[0]);
    }

    /**
     * This method takes in an address value and computes the 8-word block address that
     * potentially has the desired address, it then computes a value from 0-7 which signifies
     * which word within the 8-word block is needed. Then it retrieves the word tag stored
     * in the first slot in the cache and checks to see if that tag matches the computed base
     * address. If it's a match, then it is cache hit, and the corresponding word contained
     * in the cache using the target index is returned, 10 clock cycles are added. Otherwise,
     * it is a cache miss, and the Instruction Cache gets a refill from the L2 Cache, 50 clock
     * cycles are added. The tag for the first slot is updated and then our desired word is returned.
     *
     * @param address The memory address of the desired word instruction.
     * @return The desired word from the cache.
     */
    public Word32 read(int address) {
        int baseAddress = address - (address % 8);
        int target = address % 8;
        int firstAddress = TestConverter.toInt(slots[0]);
        if(firstAddress == baseAddress) {
            Processor.currentClockCycle += 10;
            return slots[1 + target];
        }
        else {
            Word32[] scope = l2Cache.read_Through(baseAddress);
            TestConverter.fromInt(baseAddress, slots[0]);
            for(int i = 0; i < 8; i++) {
                scope[i].copy(slots[1 + i]);
            }
            Processor.currentClockCycle += 50;
            return slots[1 + target];
        }
    }
}