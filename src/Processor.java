import java.util.*;

public class Processor {
    private Memory mem;
    private Word32 holder = new Word32();
    private ALU alu = new ALU();
    private int statusOfFetch = 0;
    private boolean halt = false;
    private Word32[] registers = new Word32[32];
    public List<String> output = new LinkedList<>();


    public Processor(Memory m) {
        mem = m;
        for (int i = 0; i < 32; i++) {
            registers[i] = new Word32();
        }
    }

    public void run() {
        while(!halt) {
            fetch();
            decode();
            execute();
            store();
        }
    }

    private void fetch() {
        if (statusOfFetch == 0) {
            mem.read();
            holder = mem.value;
            holder.getTopHalf(alu.instruction);
            statusOfFetch++;
        }
        else if (statusOfFetch == 1) {
            holder.getBottomHalf(alu.instruction);
            statusOfFetch = 0;
        }
    }

    private void decode() {
        int opCode = 0;
        opCode = alu.returnOpcode(opCode);
        if(opCode == 0)
            halt = true;
        else if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 4 || opCode == 5 ||
                opCode == 6 || opCode == 7 || opCode == 11 || opCode == 18 || opCode == 19
                || opCode == 20) {
            if(alu.instruction.word16[5].getValue() == Bit.boolValues.FALSE) {
                int index = convertMiddle(alu.instruction);
                registers[index].copy(alu.op1);
                int index2 = convertLast(alu.instruction);
                registers[index2].copy(alu.op2);
            }
            else {
                Word32 result = convertImmediate5(alu.instruction);
                result.copy(alu.op1);
                int index = convertLast(alu.instruction);
                registers[index].copy(alu.op2);
            }
        }
        else if(opCode == 8 || opCode == 9 || opCode == 12 || opCode == 13 || opCode == 14
                || opCode == 15 || opCode == 16 || opCode == 17) {
            Word32 taker = convertImmediate11(alu.instruction);
            taker.copy(alu.op1);
        }
        else if(opCode == 10) {
            //To implement
        }
    }

    private void execute() {
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + ""; // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
    }

   public int convertMiddle(Word16 sample) {
       int total = 0;
       for (int i = 6; i < 11; i++) {
           if (sample.word16[i].getValue() == Bit.boolValues.TRUE) {
               total += (int) Math.pow(2, (10 - i));
           }
       }
       return total;
   }

    public int convertLast(Word16 sample) {
        int total = 0;
        for (int i = 11; i < 16; i++) {
            if (sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (15 - i));
            }
        }
        return total;
    }

    public Word32 convertImmediate5(Word16 sample) {
        Word32 placement = new Word32();
        int holder = convertMiddle(sample);
        long temp = 0;
        if(holder >= 16) {
            temp = (holder - 32) + (long)Math.pow(2, 32);
        }
        else
            temp = holder;
        for(int i = 31; i >= 0; i--) {
            long remainder = temp % 2;
            temp = temp / 2;
            if(remainder == 0)
                placement.word32[i].assign(Bit.boolValues.FALSE);
            else
                placement.word32[i].assign(Bit.boolValues.TRUE);
        }
        return placement;
    }

    public int convert11Bits(Word16 sample) {
        int total = 0;
        for (int i = 5; i < 16; i++) {
            if (sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (15 - i));
            }
        }
        return total;
    }

    public Word32 convertImmediate11(Word16 sample) {
        Word32 placement = new Word32();
        int holder = convert11Bits(sample);
        long temporary = 0;
        if(holder >= 1024) {
            temporary = (holder - 2048) + (long)Math.pow(2, 32);
        }
        else
            temporary = holder;
        for(int i = 31; i >= 0; i--) {
            long remainder = temporary % 2;
            temporary = temporary / 2;
            if(remainder == 0)
                placement.word32[i].assign(Bit.boolValues.FALSE);
            else
                placement.word32[i].assign(Bit.boolValues.TRUE);
        }
        return placement;
    }
}