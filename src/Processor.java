import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// The Processor class to create a processor which would run and process sets of instructions at
// a time, divided into 4 categories which are fetch, decode, execute, and store. Depending on the
// task, the output would either display contents of memory or registers.
public class Processor {

    // The memory instance.
    private Memory mem;

    // The output to hold either contents held in memory or registers.
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

    private int currentClockCycle = 0;


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
        printClockCycle();
        System.out.println(TestConverter.toInt(registers[3]));
    }

    /**
     * This method fetches, using read(), a 32 bit word instruction from memory which uses the
     * program counter as the index on every other iteration. This is to ensure that the divided 32
     * bit word instruction is processed in full, by first processing the first top half
     * which is a 16 bit word instruction in the first iteration and then processes the second bottom
     * half which is a 16 bit word instruction in the second iteration. Once the full 32 bit word
     * instruction has been processed we read() again in the next iteration and repeat the process.
     *
     */
    private void fetch() {
        if(flagger == 0) {
            buffer = new Word32();
            TestConverter.fromInt(PC, mem.address);
            mem.read();
            currentClockCycle += 300;
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

    /**
     * This method computes the opcode based on the first 5 bits from the 16 bit word instruction
     * taken. Then based on that opcode value, this method branches to the corresponding operation and executes
     * the associated block of code. This is either to compute the middle 5 bits and last 5 bits and store them into
     * op1 and op2, where bit 5 dictates if the instruction set is either an immediate or 2R
     * format. For some instructions such as Call/Return and Branch Conditions the 11 bits are computed
     * as an immediate value with no 2R format and no storing of the values to op1 and op2.
     *
     */
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

    /**
     * This method performs the operation based on the given opcode derived from the instruction set.
     * For a halt instruction, it sets the halt flag to true to end the program. For arithmetic instructions
     * the method creates an ALU instance and runs the operation which takes in the parameters stored
     * on op1, op2, and instruction and returns the result. While the compare instruction sets up the status flags
     * for a Branch condition that comes after. For a Syscall instruction it simply prints the contents
     * of the registers or memory. For a Call, Return, and Branch conditions the flag to indicate a change
     * in program counter is needed is set. For load, it simply loads data from the memory address into the
     * resulting container. While store simply writes data into the specified memory address. Finally, copy
     * copies the source value into the resulting container.
     */
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
            if(opCode == 3) {
                currentClockCycle += 10;
            }
            currentClockCycle += 2;
        }
        else if(opCode == 11) {
            ALU alu = new ALU();
            op1.copy(alu.op1);
            op2.copy(alu.op2);
            instructions.copy(alu.instruction);
            alu.doInstruction();
            less.assign(alu.less.getValue());
            equal.assign(alu.equal.getValue());
            currentClockCycle += 2;
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
            currentClockCycle += 300;
        }
        else if(opCode == 19) {
            op2.copy(mem.address);
            op1.copy(mem.value);
            mem.write();
            currentClockCycle += 300;
        }
        else if(opCode == 20) {
            op2.copy(result);
        }
    }

    /**
     * This method prints the contents of the registers.
     *
     */
    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    /**
     * This method prints the contents of the memory.
     *
     */
    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            TestConverter.fromInt(i, addr);
            addr.copy(mem.address);
            mem.read();
            currentClockCycle += 300;
            mem.value.copy(value);
            //var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            var line = i + ":" + value.toString();
            output.add(line);
            System.out.println(line);
        }
    }

    /**
     * This method stores the final resulting value into the destination register from the arithmetic,
     * load, and copy instructions. While detecting if there is a change program counter, which is
     * indicated by using a flag. This change is reflected for Call, Return, and Branch instructions.
     * Where a Call pushes the program counter + 1 into to stack and a Return pops the top of the Stack and
     * returns the value to be the new program counter. Ultimately Call and Branch
     * Conditions update the program counter by assigning it the value of itself plus the immediate
     * value. If none of the Branch conditions are met, the program counter is incremented.
     * Finally, if we are still processing the top half of the 32 bit word instruction set, program
     * counter isn't incremented, otherwise it is (indicated by status flag). This sets the program
     * to fetch the next 32 bit word instruction set from memory to be processed.
     *
     */
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

    /**
     * This method takes in a 16 bit word instruction set and computes the opcode using the first
     * 5 bits, to determine the type of instruction. Then returns the computed opcode.
     *
     * @param sample The 16 bit word instruction set.
     * @return The opcode.
     */
    public int returnOpcodeProcessor(Word16 sample) {
        int startingOpCode = 0;
        for (int i = 0; i < 5; i++) {
            startingOpCode *= 2;
            if (sample.word16[i].getValue() == Bit.boolValues.TRUE)
                startingOpCode += 1;
        }
        return startingOpCode;
    }

    /**
     * This method takes in a 16 bit word instruction set and computes the middle 5 bits, bits 6 - 10,
     * and returns the computed value for a register.
     *
     * @param sample The 16 bit word instruction set.
     * @return The computed value.
     */
    public int convertMiddle(Word16 sample) {
        int total = 0;
        for(int i = 6; i < 11; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (10 - i));
            }
        }
        return total;
    }

    /**
     * This method takes in a 16 bit word instruction set and computes the last 5 bits, bits 11 - 15,
     * and returns the computed value for a register.
     *
     * @param sample The 16 bit word instruction set.
     * @return The computed value.
     */
    public int convertLast(Word16 sample) {
        int total = 0;
        for(int i = 11; i < 16; i++) {
            if(sample.word16[i].getValue() == Bit.boolValues.TRUE) {
                total += (int) Math.pow(2, (15 - i));
            }
        }
        return total;
    }

    /**
     * This method takes in a 16 bit word instruction set and computes the middle 5 bits, bits 6 - 10,
     * but sign extends the immediate value. Finally, returns the computed value.
     *
     * @param sample The 16 bit word instruction set.
     * @return The computed value.
     */
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

    /**
     * This method takes in a 16 bit word instruction set and computes the 11 bits, bits 5 - 15,
     * but sign extends the immediate value. Finally, returns the computed value.
     *
     * @param sample The 16 bit word instruction set.
     * @return The computed value.
     */
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

    public void printClockCycle() {
        System.out.println("Current Clock Cycle: " + currentClockCycle);
    }
    public static void main(String[] args) {
        String[] sumArrayInt = {
                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get address 400 to r0

                "copy 15 r1", // Fill in value 1

                "copy 10 r2",
                "multiply 2 r2", // Length 20
                "copy 4 r5",

                "store r1 r0",
                "add r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -2",

                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get address 400 to r0

                "copy 10 r2",
                "multiply 2 r2", // Length 20

                "copy 0 r3", // Accumulator

                "load r0 r4", //Sum the array until length is decremented from 20 to 0.
                "add r4 r3",
                "add r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -2",

                "halt"
        };
        var p = runMyPro(sumArrayInt);
    }
    public static Processor runMyPro(String[] placement) {
        var assembled = Assembler.assemble(placement);
        var merged = Assembler.finalOutput(assembled);
        var memory = new Memory();
        memory.load(merged);
        var processor = new Processor(memory);
        processor.run();
        for (int i = 400; i < 480; i += 4) {
            Word32 value = new Word32();
            Word32 result = new Word32();
            TestConverter.fromInt(i, result);
            result.copy(memory.address);
            memory.read();
            memory.value.copy(value);
            int holder = TestConverter.toInt(value);
            String formatted = String.format("Current value at array index %d: is %d", i, holder);
            System.out.println(formatted);
        }
        return processor;
    }
}