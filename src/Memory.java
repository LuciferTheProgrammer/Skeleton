// The Memory Class is where data can be loaded to and where later this same data can be accessed (read) from
// or modified (write) to.
public class Memory {

    // The address or location of the data in memory.
    public Word32 address = new Word32();

    // The actual data stored in memory to either be read from or written to.
    public Word32 value = new Word32();

    // The main memory.
    private final Word32[] dram= new Word32[1000];

    /**
     * This method converts a 32-bit address into an integer which signifies a
     * valid memory location within the range of 0-999 and returns the newly converted value.
     *
     * @return The memory location.
     */
    public int addressAsInt() {
        long accumulator  = 0;
        for(int i  = 0; i < 32; i++) {
            if((address.word32[i].getValue() == Bit.boolValues.TRUE))
                accumulator += calculate2Raised(31 - i);
        }
        int index = (int) (accumulator % 1000);
        if(index < 0)
            index += 1000;
        return index;
    }

    /**
     * This constructor initializes the main memory, where each slot it set to hold
     * a default Word32 object or longword.
     *
     */
    public Memory() {
        for(int i  = 0; i < dram.length; i++) {
            dram[i] = new Word32();
        }
    }

    /**
     * This method retrieves the data from the memory using the specified address
     * and copies it over to be accessed.
     *
     */
    public void read() {
        int index = addressAsInt();
        dram[index].copy(value);
    }

    /**
     * This method writes/modifies data in the memory using the specified address.
     *
     */
    public void write() {
        int index = addressAsInt();
        value.copy(dram[index]);
    }

    /**
     * This method takes in an array of Strings with each index holding a 32 character length
     * String with each character being either 't' or 'f'. Then as we iterate through the
     * array of Strings, we iterate through each of the characters of the 32 length String contained
     * within. If the character is 't' then we assign the corresponding bit value for a Word32 instance
     * to hold TRUE and otherwise false. Once the 32 length String has been processed, the initialized
     * Word32 (data) is copied over to the memory. This keeps executing until all the data
     * has been processed.
     *
     * @param data To load to memory.
     */
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
    public long calculate2Raised(int value) {
        long accumulator = 1;
        for(int i = 0; i < value ; i++) {
            accumulator *= 2;
        }
        return accumulator;
    }
}
