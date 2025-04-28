
// The L2 Cache is responsible for reading instructions from the main memory and then
// returning those desired word instructions to the Instruction Cache. It also handles
// reading and writing of data to itself (Cache) and main memory when the processor uses load/store.
// This Cache also implements a Set Way Associative Mapping.
public class L2Cache {

    // The Cache to hold 32 total word instructions, size of 4 by 8.
    private Word32[][] cache;

    // Indicates which addresses are in the Cache.
    private Word32[] tagHolder;

    // The memory instance to read from and write to.
    private Memory mem;

    // The array of queue to do FIFO replacement.
    private int[] queue;

    /**
     * This constructor takes in a Memory instance and sets it to its Memory instance field.
     * This also initializes the Caches with 32 words and the tags to be empty, values of
     * -1. It also sets the array of queue to the starting point of 0.
     */
    public L2Cache(Memory mem) {
        this.mem = mem;
        cache = new Word32[4][8];
        tagHolder = new Word32[4];
        for (int i = 0; i < 4; i++) {
            tagHolder[i] = new Word32();
            TestConverter.fromInt(-1, tagHolder[i]);
            for (int k = 0; k < 8; k++) {
                cache[i][k] = new Word32();
            }
        }
        queue = new int[2];
    }

    /**
     * This method charges 20 clock cycles for accessing L2, then takes in an address which is used
     * to compute for the block number, then using that result computes the block's starting
     * address, computes for the set either 0 or 1, and finally finds the index in the derived set.
     * This method also checks for a tag match in both ways, for a Cache hit. If there is a
     * Cache hit the target block of 8-words is returned. Otherwise, it's a Cache miss and 350
     * clock cycles are added, the FIFO pointer is updated, a block of 8-words is read from main
     * memory, the tag is updated, and the target block of 8-words is returned.
     *
     * @param address The memory address to be read from.
     * @return The target 8-word block.
     */
    public Word32[] L2_read(int address) {
        Processor.currentClockCycle += 20;
        int tagBlock = (address / 8);
        int starter = tagBlock * 8;
        int index = tagBlock % 2;
        int base = 2 * index;
        for (int i = 0; i < 2; i++) {
            int holder = TestConverter.toInt(tagHolder[base + i]);
            if (holder == starter) {
                return cache[base + i];
            }
        }
        Processor.currentClockCycle += 350;
        int evictions = queue[index];
        queue[index] = (queue[index] + 1) % 2;
        int container = evictions + base;
        for (int i = 0; i < 8; i++) {
            TestConverter.fromInt(starter + i, mem.address);
            mem.read();
            mem.value.copy(cache[container][i]);
        }
        TestConverter.fromInt(starter, tagHolder[container]);
        return cache[container];
    }

    /**
     * This method adds 50 clock cycles, takes in an address to compute the block number,
     * then using that result computes the block's starting address, computes for the index
     * of the desired word within the block, computes for the set either 0 or 1, and finally
     * finds the index in the derived set. This method also checks for a Cache hit, if there is
     *  one, then the target word is returned. Otherwise, it's a Cache miss, 350 clock cycles are added,
     * the FIFO pointer is updated, a block of 8-words is read from main memory, the tag is updated,
     * and the target word is returned.
     *
     * @param taker The memory address to be read from.
     * @return The target word.
     */
    public Word32 read_Data(Word32 taker) {
        Processor.currentClockCycle += 50;
        int address = TestConverter.toInt(taker);
        int tagBlock = address / 8;
        int starter = 8 * tagBlock;
        int target = address % 8;
        int index = tagBlock % 2;
        int base = 2 * index;

        for(int i = 0; i < 2; i++) {
            int holder = TestConverter.toInt(tagHolder[base + i]);
            if(holder == starter) {
                return cache[base + i][target];
            }
        }
        Processor.currentClockCycle += 350;
        int evictions = queue[index];
        queue[index] = (queue[index] + 1) % 2;
        int container = evictions + base;
        for(int i = 0; i < 8; i++) {
            TestConverter.fromInt(starter + i, mem.address);
            mem.read();
            mem.value.copy(cache[container][i]);
        }
        TestConverter.fromInt(starter, tagHolder[container]);
        return cache[container][target];
    }

    /**
     * This method adds 50 clock cycles, takes in an address (destination) to compute the block number,
     * then using that result computes the block's starting address, computes for the index
     * of the desired word within the block, computes for the set either 0 or 1, and finally
     * finds the index in the derived set. This method also checks if there is a Cache hit, then
     * the block is present, and the target word is updated by the source word (optional).
     * Otherwise, we always write through main memory.
     *
     * @param destination The memory address to be written to.
     * @param source The value to be written to the memory address.
     */
    public void write_Data(Word32 destination, Word32 source) {
        Processor.currentClockCycle += 50;
        int address = TestConverter.toInt(destination);
        int tagBlock = address / 8;
        int starter = 8 * tagBlock;
        int target = address % 8;
        int index = tagBlock % 2;
        int base = 2 * index;
        for(int i = 0; i < 2; i++) {
            int holder = TestConverter.toInt(tagHolder[base + i]);
            if(holder == starter) {
                source.copy(cache[base + i][target]);
                break;
            }
        }
        destination.copy(mem.address);
        source.copy(mem.value);
        mem.write();
    }

    /**
     * This method is used for debugging purposes only. To help load contents from memory and
     * help print it for printMem() and printArrayMemory().
     *
     */
    public Word32 mem_read_Debug(Word32 sample) {
        sample.copy(mem.address);
        mem.read();
        Word32 temp = new Word32();
        mem.value.copy(temp);
        return temp;
    }
}