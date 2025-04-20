public class InstructionCache {
    public int InstructionCacheClockCycle;
    public L2Cache l2Cache;
    public Word32[] slots;

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