import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// The Processor class to create a processor which would run and process sets of instructions at
// a time divided into 4 categories which are fetch, decode, execute, and store. Depending on the
// task, the output would either display contents of memory or registers.
public class Processor {

    // The memory instance.
    private Memory mem;

    // The output to holder either contents held in memory or registers.
    public List<String> output = new LinkedList<>();

    // The 32 registers to hold contents.
    private Word32[] registers;

    // The 16 bit word instruction set.
    private Word16 instructions;

    // The 32 bit word operator 1.
    private Word32 op1;

    // The 32 bit word operator 2.
    private Word32 op2;

    // The 32 bit word result.
    private Word32 result;

    // The bit less flag.
    private Bit less;

    // The bit equal flag.
    private Bit equal;

    // The 32 bit word instruction container.
    private Word32 buffer;

    // The source index.
    private int source;

    // The destination index.
    private int destination;

    // The immediate offset value.
    private int immediate;

    // The program counter.
    private int PC;

    // The tracker of the top half and bottom half of the 32 bit word instruction container.
    private int flagger;

    // The computed operation code.
    private int opCode;

    // The flag to stop.
    private boolean halt;

    // The flag to determine whether to increment the program counter or not.
    private boolean status;

    // The flag for Call/Return/Branches to indicate a change in the program counter.
    private boolean changePC;

    // The stack to be used for Call/Return.
    private Stack<Integer> callReturn;


    /**
     * The constructor which takes in a Memory object and assigns it to its Memory instance field.
     * This method also initializes all of its other member fields with default values.
     *
     * @param m The memory.
     */
    public Processor(Memory m) {
        mem = m;
        registers = new Word32[32];
        for(int i = 0; i < 32; i++) {
            registers[i] = new Word32();
        }
        instructions = new Word16();
        op1 = new Word32();
        op2 = new Word32();
        result = new Word32();
        less = new Bit(false);
        equal = new Bit(false);
        source = 0;
        destination = 0;
        immediate = 0;
        PC = 0;
        flagger = 0;
        opCode = 0;
        halt = false;
        status = false;
        changePC = false;
        callReturn = new Stack<>();
        buffer = null;
    }

    /**
     * The method runs and processes sets of instructions in a loop, which is composed of
     * fetch, decode, execute, and store. The process ends once halt is seen and processed.
     *
     */
    public void run() {
        while(!halt) {
            fetch();
            decode();
            execute();
            store();
        }
    }

    /**
     * This method
     *
     */
    private void fetch() {
        if(flagger == 0) {
            buffer = new Word32();
            TestConverter.fromInt(PC, mem.address);
            mem.read();
            mem.value.copy(buffer);
            buffer.getTopHalf(instructions);
            flagger = 1;
            status = true;
        }
        else {
            buffer.getBottomHalf(instructions);
            flagger = 0;
            status = false;
        }
    }

    private void decode() {
        opCode = returnOpcodeProcessor(instructions);
        if(opCode == 8 || opCode == 9 || opCode == 12 || opCode == 13 ||opCode == 14
                || opCode == 15 || opCode == 16 || opCode == 17) {
            immediate = immediateValue11(instructions);
        }
        else if(opCode == 19) {
            if(instructions.word16[5].getValue() == Bit.boolValues.FALSE) {
                source = convertMiddle(instructions);
                destination = convertLast(instructions);
                registers[source].copy(op1);
                registers[destination].copy(op2);
            }
            else {
                Word32 temp = new Word32();
                immediate = immediateValue5(instructions);
                destination = convertLast(instructions);
                TestConverter.fromInt(immediate, temp);
                temp.copy(op1);
                registers[destination].copy(op2);
            }
        }
        else {
            if(instructions.word16[5].getValue() == Bit.boolValues.FALSE) {
                source = convertMiddle(instructions);
                destination = convertLast(instructions);
                registers[destination].copy(op1);
                registers[source].copy(op2);
            }
            else if(instructions.word16[5].getValue() == Bit.boolValues.TRUE) {
                Word32 temp = new Word32();
                immediate = immediateValue5(instructions);
                destination = convertLast(instructions);
                TestConverter.fromInt(immediate, temp);
                registers[destination].copy(op1);
                temp.copy(op2);
            }
        }
    }

    private void execute() {
        if(opCode == 0) {
            halt = true;
        }
        else if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 4 ||
            opCode == 5 || opCode == 6 || opCode == 7) {
            ALU alu = new ALU();
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            instructions.copy(alu.instruction);
            alu.doInstruction();
            alu.result.copy(result);
        }
        else if(opCode == 11) {
            ALU alu = new ALU();
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            instructions.copy(alu.instruction);
            alu.doInstruction();
            less.assign(alu.less.getValue());
            equal.assign(alu.equal.getValue());
        }
        else if(opCode == 8) {
            switch(immediate) {
                case 0 -> {
                    printReg();
                }
                case 1 -> {
                    printMem();
                }
            }
        }
        else if(opCode == 9 || opCode == 10 || opCode == 12 || opCode == 13 || opCode == 14
                || opCode == 15 || opCode == 16 || opCode == 17) {
            changePC = true;
        }
        else if(opCode == 18) {
            if(instructions.word16[5].getValue() == Bit.boolValues.FALSE) {
                op2.copy(mem.address);
            }
            else if(instructions.word16[5].getValue() == Bit.boolValues.TRUE) {
                Adder.add(op2, op1, mem.address);
            }
            mem.read();
            mem.value.copy(result);
        }
        else if(opCode == 19) {
            op2.copy(mem.address);
            op1.copy(mem.value);
            mem.write();
        }
        else if(opCode == 20) {
            op2.copy(result);
        }
    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            TestConverter.fromInt(i, addr);
            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            //var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            var line = i + ":" + value.toString();
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
        if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 4 || opCode == 5 || opCode == 6 ||
                opCode == 7 || opCode == 18 || opCode == 20) {
            result.copy(registers[destination]);
        }
        if(changePC) {
            changePC = false;
            switch(opCode) {
                case 9 -> {
                    callReturn.push(PC + 1);
                    PC += immediate;
                }
                case 10 -> {
                    PC = callReturn.pop();
                    flagger = 0;
                    status = true;
                }
                case 12 -> {
                    if(less.getValue() == Bit.boolValues.TRUE || equal.getValue() == Bit.boolValues.TRUE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
                case 13 -> {
                    if(less.getValue() == Bit.boolValues.TRUE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
                case 14 -> {
                    if(less.getValue() == Bit.boolValues.FALSE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
                case 15 -> {
                    if(less.getValue() == Bit.boolValues.FALSE && equal.getValue() == Bit.boolValues.FALSE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
                case 16 -> {
                    if(equal.getValue() == Bit.boolValues.TRUE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
                case 17 -> {
                    if(equal.getValue() == Bit.boolValues.FALSE) {
                        PC += immediate;
                    }
                    else
                        PC++;
                }
            }
        }
        else {
            if(!status) {
                PC++;
            }
        }
    }

    public int returnOpcodeProcessor(Word16 sample) {
        int startingOpCode = 0;
        for (int i = 0; i < 5; i++) {
            startingOpCode *= 2;
            if (sample.word16[i].getValue() == Bit.boolValues.TRUE)
                startingOpCode += 1;
        }
        return startingOpCode;
    }

    public int convertMiddle(Word16 sample) {
        int total = 0;
        for(int i = 6; i < 11; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (10 - i));
            }
        }
        return total;
    }

    public int convertLast(Word16 sample) {
        int total = 0;
        for(int i = 11; i < 16; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (15 - i));
            }
        }
        return total;
    }
    public int immediateValue5(Word16 sample) {
        int total = 0;
        for(int i = 6; i < 11; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (10 - i));
            }
        }
        if(sample.word16[6].getValue() == Bit.boolValues.TRUE) {
            total -= 32;
        }
        return total;
    }
    public int immediateValue11(Word16 sample) {
        int total = 0;
        for(int i = 5; i < 16; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (15 - i));
            }
        }
        if(sample.word16[5].getValue() == Bit.boolValues.TRUE) {
            total -= 2048;
        }
        return total;
    }
}