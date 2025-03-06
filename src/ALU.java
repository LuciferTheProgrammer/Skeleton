// The ALU class holds and processes sets of instructions.
public class ALU {

    // The instruction set.
    public Word16 instruction = new Word16();

    // The 1st operator.
    public Word32 op1 = new Word32();

    //The 2nd operator.
    public Word32 op2 = new Word32();

    // To hold the resulting value after the specified instruction.
    public Word32 result = new Word32();

    // Used for the compare instruction, sets the less flag.
    public Bit less = new Bit(false);

    // Used for the compare instruction, sets the equal flag.
    public Bit equal = new Bit(false);


    /**
     * This method computes for the opcode which is then utilized to match to a specific instruction.
     * When a match is found that corresponding instruction is executed accordingly. The instruction
     * descriptions include add, and, multiply, left shift, subtract, or, right shift, and compare.
     *
     */
    public void doInstruction() {
        result = new Word32();
        int startingOpCode = 0;
        startingOpCode = returnOpcode(startingOpCode);
        switch (startingOpCode) {
            case 1:
                Adder.add(op1, op2, result);
                break;
            case 2:
                op1.and(op2, result);
                break;
            case 3:
                Multiplier.multiply(op1, op2, result);
                break;
            case 4:
                int amountToShiftLeft = conversionSmallRange(op2);
                Shifter.LeftShift(op1, amountToShiftLeft, result);
                break;
            case 5:
                Adder.subtract(op1, op2, result);
                break;
            case 6:
                op1.or(op2, result);
                break;
            case 7:
                int amountToShiftRight = conversionSmallRange(op2);
                Shifter.RightShift(op1, amountToShiftRight, result);
                break;
            case 11:
                int holder = compare(op1, op2);
                if(holder== 0) {
                    less.assign(Bit.boolValues.FALSE);
                    equal.assign(Bit.boolValues.TRUE);
                }
                else if(holder == 1) {
                    less.assign(Bit.boolValues.FALSE);
                    equal.assign(Bit.boolValues.FALSE);

                }
                else if(holder == -1) {
                    less.assign(Bit.boolValues.TRUE);
                    equal.assign(Bit.boolValues.FALSE);
                }
                break;
        }
    }

    /**
     * This method takes in a Word32 opcode and then proceeds to do a bit to integer conversion
     * of its last 5 bits only when those bit values hold a value of TRUE. Then, the newly converted
     * value is returned as the amount to be shifted either via left or right shift.
     *
     * @param subset_op2 The Word32 instance.
     * @return The amount to be shifted.
     */
    public int conversionSmallRange(Word32 subset_op2) {
        int amountToShift = 0;
        for (int i = 27; i < 32; i++) {
            if(subset_op2.word32[i].getValue() == Bit.boolValues.TRUE)
                amountToShift += calculate2Raised(31 - i);
        }
        return amountToShift;
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

    /**
     * This method takes in two Word32 instances and compares their values bit by bit. If
     * the first Word32 instance is bigger than the second Word32 instance then a result of 1
     * is returned. If the first Word32 instance is smaller than the second Word32 instance then a
     * result of -1 is returned. Otherwise, both Word32 instances are equivalent and a value of
     * 0 is returned.
     *
     * @param sample1 The first Word32 instance.
     * @param sample2 The second Word32 instance.
     * @return The comparison status.
     */
    public int compare(Word32 sample1, Word32 sample2) {
        //Check for signed bits.
        if(sample1.word32[0].getValue() != sample2.word32[0].getValue()) {
            //op1 < op2.
            if (sample1.word32[0].getValue() == Bit.boolValues.TRUE)
                return -1;
            //op1 > op2.
            else
                return 1;
        }
        else if(sample1.word32[0].getValue() == Bit.boolValues.TRUE) {
            for(int i = 1; i < 32; i++) {
                if(sample1.word32[i].getValue() != sample2.word32[i].getValue()) {
                    //op1 < op2
                    if (sample1.word32[i].getValue() == Bit.boolValues.TRUE)
                        return -1;
                    //op1 > op2
                    else
                        return 1;
                }
            }
        }
        else if(sample1.word32[0].getValue() == Bit.boolValues.FALSE) {
            for (int i = 1; i < 32; i++) {
                if(sample1.word32[i].getValue() != sample2.word32[i].getValue()) {
                    //op1 > op2
                    if (sample1.word32[i].getValue() == Bit.boolValues.TRUE)
                        return 1;
                    //op1 < op2
                    else
                        return -1;
                }
            }
        }
        return 0;
    }

    /**
     * This method proceeds to compute the opcode by processing 5 bits from the given instruction.
     * Then it's multiplied by 2 which the value then gets added by 1 if the corresponding
     * bit holds the value TRUE, and finally it returns the resulting value.
     *
     * @param startingOpCode The starting value for the opcode.
     * @return The computed opcode.
     */
    public int returnOpcode(int startingOpCode) {
        for (int i = 0; i < 5; i++) {
            startingOpCode *= 2;
            if (instruction.word16[i].getValue() == Bit.boolValues.TRUE)
                startingOpCode += 1;
        }
        return startingOpCode;
    }
}
