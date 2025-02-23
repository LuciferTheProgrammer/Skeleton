public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();

    private final Word32[] dram= new Word32[1000];

    public int addressAsInt() {
        int accumulator  = 0;
        for(int i  = 0; i < 32; i++) {
            if(accumulator > 999)
                break;
            if((address.word32[i].getValue() == Bit.boolValues.TRUE))
                accumulator += calculate2Raised(31 - i);
        }
        return accumulator;
    }

    public Memory() {
        for(int i  = 0; i < dram.length; i++) {
            dram[i] = new Word32();
        }
    }

    public void read() {
        int index = addressAsInt();
        dram[index].copy(value);
    }

    public void write() {
        int index = addressAsInt();
        value.copy(dram[index]);
    }

    public void load(String[] data) {
        for(int i = 0; i < data.length; i++) {
            Word32 temp = new Word32();
            for(int k = 0; k < 32; k++) {
                if(data[i].charAt(k) == 't')
                    temp.word32[k].assign(Bit.boolValues.TRUE);
                else
                    temp.word32[k].assign(Bit.boolValues.FALSE);
            }
            temp.copy(dram[i]);
        }
    }
    /**
     * This method takes in a number which determines the number of times the base is multiplied
     * by 2 or to take the powers of 2.
     *
     * @param value The frequency the base is multiplied by 2.
     * @return The accumulated value.
     */
    public int calculate2Raised(int value) {
        int accumulator = 1;
        for(int i = 0; i < value ; i++) {
            accumulator *= 2;
        }
        return accumulator;
    }
}
