
// The L2 Cache is responsible for reading instructions from the main memory and then
// returning those desired word instructions to Instruction Cache. It also handles
// reading from and writing data to the cache and main memory when the processor uses load/store.
// This cache also implements a Set Way Associative Mapping.
public class L2Cache {

    // The cache to hold 32 total word instructions, size of 4 by 8.
    private Word32[][] cache;

    // Indicates which addresses are in the cache.
    private Word32[] tagHolder;

    // The memory instance to read from and write to.
    private Memory mem;

    // The array of queue to do FIFO replacement.
    private int[] queue;

    /**
     * This constructor
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

    public void write_Data(Word32 destination, Word32 source) {
        Processor.currentClockCycle += 50;
        int address = TestConverter.toInt(destination);
        int tagBlock = address /8;
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

