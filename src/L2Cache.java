
// The L2 Cache is responsible for reading instructions from the main memory and then
// returning those desired word instructions to Instruction Cache. It also handles
// reading from and writing data to the main memory when the processor uses load/store.
public class L2Cache {
    // The cache to hold 32 total word instructions, size of 4 by 8.
    private Word32[][] instruction_holder;

    // Indicates which addresses are in the cache.
    private Word32[] presence;

    // The memory instance to read from and write to.
    private Memory mem;

    /**
     * This constructor takes in a memory instance and sets it to the memory instance field.
     * It also initializes the 4 by 8 array, the cache, with Word32 instances. It also
     * initializes the presence array with Word32 instances and sets those words to represent
     * -1 which indicates that no addresses are in the cache.
     *
     * @param map
     */
    public L2Cache(Memory map) {
        mem = map;
        instruction_holder = new Word32[4][8];
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 8; k++) {
                instruction_holder[i][k] = new Word32();
            }
        }
        presence = new Word32[4];
        for (int i = 0; i < 4; i++) {
            presence[i] = new Word32();
            TestConverter.fromInt(-1, presence[i]);
        }
    }

    /**
     * This method takes in an address and then uses the address to compute which of the
     * four slots to use for the presence array, uses this newly computed value as an index in presence
     * to check if the desired block address is there. The use of the L2 Cache by the Instruction
     * Cache adds 20 clock cycles. If the desired address matches computed flag value from
     * the presence array, then it is a cache hit and the corresponding subset value that was computed
     * at first is used as an index in the cache to return the corresponding 8 words. Otherwise,
     * it is a cache miss, it adds 350 clock cycles and refills the of the groups of 8 words
     * from the main memory. Then the group with the refilled 8 words is returned.
     *
     * @param address The memory address of the desired 8 words.
     * @return The 8 words from the cache.
     */
    public Word32[] read_Through(int address) {
        int subset = (address / 8) % 4;
        int flag = TestConverter.toInt(presence[subset]);
        Processor.currentClockCycle += 20;
        if (address != flag) {
            Processor.currentClockCycle += 350;
            TestConverter.fromInt(address, presence[subset]);
            for (int i = 0; i < 8; i++) {
                TestConverter.fromInt(address + i, mem.address);
                mem.read();
                mem.value.copy(instruction_holder[subset][i]);
            }
        }
        return instruction_holder[subset];
    }

    /**
     * This method takes in a Word32 instance, reads
     * from the main memory and then returns the data stored in the address specified
     * by the word instance. This also adds 50 clock cycles. Used for load.
     *
     * @param sample The word address.
     * @return The data stored in the address specified by the word instance.
     */
    public Word32 read(Word32 sample) {
        Word32 temp = new Word32();
        sample.copy(mem.address);
        mem.read();
        Processor.currentClockCycle += 50;
        mem.value.copy(temp);
        return temp;
    }

    /**
     * This method takes in two Word32 instances, the address specified by the word destination
     * is where data represented by the word source is stored/written to the main memory.
     * This also adds 50 clock cycles. Used for store.
     *
     * @param sample1 The word destination for the address in memory.
     * @param sample2 The word source data to be stored in the address in memory.
     */
    public void write(Word32 sample1, Word32 sample2) {
        sample1.copy(mem.address);
        sample2.copy(mem.value);
        mem.write();
        Processor.currentClockCycle += 50;
    }
}
