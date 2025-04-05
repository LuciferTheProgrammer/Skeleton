import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private Memory mem;
    private Word32 op1;
    private Word32 op2;
    private int destination;
    private int source;
    private int immediate;
    private int opCode;
    private Word32 result;
    private boolean changeInProgramCounter;
    private Stack<Integer> stack;
    private Word32[] registers;
    private Word32 buffer;
    private Word16 instruction;
    private int programCounter;
    private boolean halt;
    private Bit lessHolder;
    private Bit equalHolder;
    public List<String> output = new LinkedList<>();
    private int flagger;
    private boolean container;


    public Processor(Memory m) {
        mem = m;
        programCounter = 0;
        immediate = 0;
        lessHolder = new Bit(false);
        equalHolder = new Bit(false);
        halt = false;
        stack = new Stack<>();
        changeInProgramCounter = false;
        registers = new Word32[32];
        for(int i = 0; i < 32; i++) {
            registers[i] = new Word32();
        }
        op1 = new Word32();
        op2 = new Word32();
        destination = 0;
        source = 0;
        opCode = 0;
        result = new Word32();
        buffer = null;
        instruction = new Word16();
        flagger = 0;
        container = false;
    }

    public void run() {
        while(!halt) {
            fetch();
            decode();
            execute();
            store();
            System.out.println("opCode: " + opCode);
        }
    }
    private void fetch() {
        if(flagger == 0) {
            buffer = new Word32();
            TestConverter.fromInt(programCounter, mem.address);
            mem.read();
            mem.value.copy(buffer);
            buffer.getTopHalf(instruction);
            flagger = 1;
            container = true;
        }
        else {
            buffer.getBottomHalf(instruction);
            flagger = 0;
            container = false;
        }
        System.out.println("Fetched instruction at PC=" + programCounter + " (" + (container ? "top" : "bottom") + " half): " + instruction.toString());
    }

    private void decode() {
        opCode = returnOpcodeProcessor(instruction);
        if(opCode == 0 || opCode == 10) {
            //Do Nothing
        }
        else if(opCode == 8 || opCode == 9 || opCode == 12 || opCode == 13 ||
                opCode == 14 || opCode == 15 || opCode == 16 || opCode == 17) {
            immediate = immediateValue11(instruction);
            System.out.println("Call Instruction decoded: " + instruction.toString() +
                    ", Immediate: " + immediate);
        }
        else {
            if(instruction.word16[5].getValue() == Bit.boolValues.FALSE) {
                source = convertMiddle(instruction);
                destination = convertLast(instruction);
                registers[destination].copy(op1);
                registers[source].copy(op2);
            } else if (instruction.word16[5].getValue() == Bit.boolValues.TRUE) {
                Word32 placement = new Word32();
                immediate = immediateValue5(instruction);
                destination = convertLast(instruction);
                registers[destination].copy(op1);
                TestConverter.fromInt(immediate, placement);
                placement.copy(op2);
            }
        }
    }

    private void execute() {
        if(opCode == 0) {
            halt = true;
        }
        else if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 4 || opCode == 5
                || opCode == 6 || opCode == 7) {
            ALU alu = new ALU();
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            instruction.copy(alu.instruction);
            alu.doInstruction();
            alu.result.copy(result);
        }
        else if(opCode == 11) {
            ALU alu = new ALU();
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            instruction.copy(alu.instruction);
            alu.doInstruction();
            lessHolder.assign(alu.less.getValue());
            equalHolder.assign(alu.equal.getValue());
            System.out.println("Compare executed. Less: " + lessHolder.getValue() + ", Equal: " + equalHolder.getValue());

        }
        else if (opCode == 8) {
            switch(immediate) {
                case 0 -> {printReg();}
                case 1 -> {printMem();}
            }
        }
        else if(opCode == 9) {
            changeInProgramCounter = true;
        }
        else if(opCode == 10) {
            changeInProgramCounter = true;
        }
        else if(opCode == 12 || opCode == 13 || opCode == 14 || opCode == 15
                || opCode == 16 || opCode == 17) {
            changeInProgramCounter = true;
        }
        else if (opCode == 18) {
            if(instruction.word16[5].getValue() == Bit.boolValues.FALSE) {
                op2.copy(mem.address);
            }
            else if(instruction.word16[5].getValue() == Bit.boolValues.TRUE) {
                Adder.add(op2, op1, mem.address);
            }
            mem.read();
            mem.value.copy(result);
        }
        else if(opCode == 19) {
            if(instruction.word16[5].getValue() == Bit.boolValues.TRUE) {
                Adder.add(registers[destination], op2, mem.address);
            }
            else {
                registers[destination].copy(mem.address);
            }
            registers[source].copy(mem.value);
            mem.write();
            //mem.value.copy(result);
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
        if(opCode == 8) {
            //Do Nothing
        }
        if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 4 || opCode == 5
                || opCode == 6 || opCode == 7 || opCode == 18 ||
                opCode == 20) {
            result.copy(registers[destination]);
        }
        //System.out.print("Immediate: " + immediate + " arithmetic/logic.");
        if(changeInProgramCounter) {
            changeInProgramCounter = false;
            //System.out.print("Immediate: " + immediate + "on call/return/branch.");
            switch(opCode) {
                case 9 -> {
                    System.out.println("Call encountered. PC before: " + programCounter + ", immediate: " + immediate);
                    stack.push(programCounter + 1);
                    programCounter += immediate;    // Immediate is in word units
                    //programCounter += immediate;     // immediate is in instruction slots
                    System.out.println("Call completed. New PC: " + programCounter + ", Stack top: " + stack.peek());

                }
                case 10 -> {
                    int ret = stack.pop();
                    //programCounter = stack.pop();
                    System.out.println("Return encountered. Popped return address: " + programCounter);
                    programCounter = ret;
                    flagger = 0;
                    container = true;
                }
                case 12 -> {
                    if (lessHolder.getValue() == Bit.boolValues.TRUE || equalHolder.getValue() == Bit.boolValues.TRUE) {
                        programCounter += immediate;     // immediate is in instruction slots
                    } else
                        programCounter++;
                }
                case 13 -> {
                    if (lessHolder.getValue() == Bit.boolValues.TRUE) {
                        programCounter += immediate;     // immediate is in instruction slots
                    }
                    else
                        programCounter++;
                }
                case 14  -> {
                    if(lessHolder.getValue() == Bit.boolValues.FALSE) {
                        programCounter += immediate;     // immediate is in instruction slots
                    }
                    else
                        programCounter++;
                }
                case 15 -> {
                    if (lessHolder.getValue() == Bit.boolValues.FALSE && equalHolder.getValue() == Bit.boolValues.FALSE) {
                         programCounter += immediate;     // immediate is in instruction slots
                    }
                    else
                        programCounter++;
                }
                case 16 -> {
                    if (equalHolder.getValue() == Bit.boolValues.TRUE) {
                         programCounter += immediate;     // immediate is in instruction slots
                    }
                    else
                        programCounter++;
                }
                case 17 -> {
                    System.out.println("BNE encountered. PC before: " + programCounter + ", immediate: " + immediate +
                            ", Equal flag: " + equalHolder.getValue());
                    if(equalHolder.getValue() == Bit.boolValues.FALSE) {
                        programCounter += immediate;     // immediate is in instruction slots
                    }
                    else
                        programCounter++;
                    System.out.println("BNE completed. New PC: " + programCounter);

                }
            }
        }
        else {
            if(!container)
                programCounter++;
        }
        System.out.println("Store completed. New PC: " + programCounter);

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