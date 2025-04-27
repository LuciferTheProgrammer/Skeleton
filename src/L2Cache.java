
// The L2 Cache is responsible for reading instructions from the main memory and then
// returning those desired word instructions to Instruction Cache. It also handles
// reading from and writing data to the main memory when the processor uses load/store.
public class L2Cache {

    // The cache to hold 32 total word instructions, size of 4 by 8.
    private Word32[][] cache;

    // Indicates which addresses are in the cache.
    private Word32[] tagHolder;

    // The memory instance to read from and write to.
    private Memory mem;

    private int queueReplacements;


    public L2Cache(Memory mem) {
        this.mem = mem;
        cache = new Word32[4][8];
        tagHolder = new Word32[4];
        for(int i = 0; i < 4; i++){
            tagHolder[i] = new Word32();
            TestConverter.fromInt(-1, tagHolder[i]);
            for(int k = 0; k < 8; k++) {
                cache[i][k] = new Word32();
            }
        }
        queueReplacements = 0;
    }
    public Word32[] L2_read(int address) {
        Processor.currentClockCycle += 20;
        int start = (address / 8) * 8;
        for(int i = 0; i < 4; i++) {
            int flag = TestConverter.toInt(tagHolder[i]);
            if(start == flag) {
                return cache[i];
            }
        }
        Processor.currentClockCycle += 350;
        int eviction = queueReplacements;
        queueReplacements = (queueReplacements  + 1) % 4;
        for(int i = 0; i < 8; i++) {
            TestConverter.fromInt(start + i, mem.address);
            mem.read();
            mem.value.copy(cache[eviction][i]);
        }
        TestConverter.fromInt(start, tagHolder[eviction]);
        return cache[eviction];
    }
    public Word32 read_Data(Word32 taker) {
        Processor.currentClockCycle += 50;
        int address = TestConverter.toInt(taker);
        int start = (address / 8) * 8;
        int target = address % 8;
        for(int i = 0; i < 4; i++) {
            int container = TestConverter.toInt(tagHolder[i]);
            if (start == container) {
                return cache[i][target];
            }
        }
        int eviction = queueReplacements;
        queueReplacements = (queueReplacements + 1) % 4;
        for(int i = 0; i < 8; i++) {
            TestConverter.fromInt(start + i, mem.address);
            mem.read();
            mem.value.copy(cache[eviction][i]);
        }
        TestConverter.fromInt(start, tagHolder[eviction]);
        return cache[eviction][target];
    }

    public void write_Data(Word32 destination, Word32 source) {
        Processor.currentClockCycle += 50;
        int address = TestConverter.toInt(destination);
        int start = (address / 8) * 8;
        int target = address % 8;
        for (int i = 0; i < 4; i++) {
            int holder = TestConverter.toInt(tagHolder[i]);
            if (holder == start) {
                source.copy(cache[i][target]);
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

