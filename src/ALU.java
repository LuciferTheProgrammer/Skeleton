public class ALU {
    public Word16 instruction = new Word16();
    public Word32 op1 = new Word32();
    public Word32 op2 = new Word32();
    public Word32 result = new Word32();
    public Bit less = new Bit(false);
    public Bit equal = new Bit(false);

    public void doInstruction() {
        for(int i = 0; i < 32; i++)
            result.word32[i].assign(Bit.boolValues.FALSE);
        int startingOpCode = 0;
        for (int i = 0; i < 5; i++) {
            startingOpCode *= 2;
            if (instruction.word16[i].getValue() == Bit.boolValues.TRUE)
                startingOpCode += 1;
        }
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
}
