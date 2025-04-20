public class L2Cache {
    public int L2CacheClockCycle;
    public Word32[][] instruction_holder;
    public Word32[] presence;
    public Memory mem;

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
        L2CacheClockCycle = 0;
    }

    public Word32[] read_Through(int address) {
        int subset = (address / 8) % 4;
        int flag = TestConverter.toInt(presence[subset]);
        L2CacheClockCycle += 20;
        if (address != flag) {
            L2CacheClockCycle += 350;
            TestConverter.fromInt(address, presence[subset]);
            for (int i = 0; i < 8; i++) {
                TestConverter.fromInt(address + i, mem.address);
                mem.read();
                mem.value.copy(instruction_holder[subset][i]);
            }
        }
        return instruction_holder[subset];
    }

    public Word32 read(Word32 sample) {
        L2CacheClockCycle += 50;
        Word32 temp = new Word32();
        sample.copy(mem.address);
        mem.read();
        mem.value.copy(temp);
        return temp;
    }
    public void write(Word32 sample1, Word32 sample2) {
        L2CacheClockCycle += 50;
        sample1.copy(mem.address);
        sample2.copy(mem.value);
        mem.write();
    }
}
