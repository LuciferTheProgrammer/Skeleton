public class InstructionCache {
    public Memory memory;
    public int InstructionCacheClockCycle;

    public Word32[] slots;

    public InstructionCache(Memory memory) {
        this.memory = memory;
        slots = new Word32[9] ;
        for(int i = 0; i < slots.length; i++) {
            slots[i] = new Word32();
        }
        InstructionCacheClockCycle = 0;
        TestConverter.fromInt(-1, slots[0]);

    }

    public Word32 read(Word32 sample) {
        int address = TestConverter.toInt(sample);
        int baseAddress = address - (address % 8);
        int target = address % 8;
        int firstAddress = TestConverter.toInt(slots[0]);
        if(firstAddress == baseAddress) {
            InstructionCacheClockCycle += 10;
            return slots[1 + target];
        }
        else {
            TestConverter.fromInt(baseAddress, slots[0]);
            InstructionCacheClockCycle += 350;
            for(int i = 0; i < 8; i++) {
                Word32 addr = new Word32();
                TestConverter.fromInt(baseAddress + i, addr);
                addr.copy(memory.address);
                memory.read();
                memory.value.copy(slots[1 + i]);
            }
            return slots[1 + target];
        }
    }
}