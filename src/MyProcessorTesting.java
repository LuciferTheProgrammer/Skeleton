import java.awt.*;

public class MyProcessorTesting {
    public static void main(String[] args) {

        // Case 1:
        String[] sumArrayInt = {
                "copy 10 r6",
                "multiply 10 r6",
                "multiply 4 r6",
                "copy r6 r0", // Get address space 400 to r0
                "copy r6 r0",
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
                "multiply 4 r0", // Get address space 400 to r0
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
        System.out.println("Case 1: Array of size 20 and sum: ");
        var processor1 = runMyPro(sumArrayInt);
        Processor.counter++;

        System.out.println();

        // Case 2:
        String[] LinkedList = {
                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get Address Space 400 to r0.
                "copy r0 r7",
                "copy 8 r1", // Node size (4 bytes for data and 4 bytes for reference)
                "copy 5 r2",
                "leftshift 2 r2",  // Length 20
                "copy 15 r3", // Constant value to be stored on data fields.
                "store r3 r0",
                "copy r0 r4",
                "add 4 r4",
                "copy r0 r5",
                "add r1 r5",
                "store r5 r4",
                "copy r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -4",
                "subtract r1 r0",
                "add 4 r0",
                "copy 0 r6",
                "store r6 r0",
                "copy r7 r0",
                "copy 0 r3",
                "load r0 r4", // line 1
                "add r4 r3",
                "copy r0 r5",
                "add 4 r5",  // line 2
                "load r5 r0",
                "copy 0 r2", // Dummy
                "compare 0 r0", // line 3
                "bne -3",
                "halt",

        };
        System.out.println("Case 2: LinkedList of size 20 and sum: ");
        var processor2 = runMyPro(LinkedList);
        Processor.counter++;

        System.out.println();

        // Case 3:
        String[] sumArrayIntBackwards = {
                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get address space 400 to r0
                "copy 15 r1", // Fill in value 1
                "copy 10 r2",
                "multiply 2 r2", // Length 20
                "copy 4 r5",
                "store r1 r0",
                "add r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -2",
                "copy 10 r6",
                "add 7 r6",
                "multiply 7 r6",
                "multiply 2 r6",
                "multiply 2 r6",
                "copy 0 r0", // Get address space 476 to r0
                "add r6 r0",
                "copy 10 r2",
                "multiply 2 r2", // Length 20 {length}
                "copy 0 r3", // Accumulator
                "load r0 r4", //Sum the array until length is decremented from 20 to 0.
                "add r4 r3",
                "subtract r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -2",
                "halt"
        };
        System.out.println("Case 3: Array of size 20 and sum backwards: ");
        var processor3 = runMyPro(sumArrayIntBackwards);
    }

    public static Processor runMyPro(String[] placement) {
        var assembled = Assembler.assemble(placement);
        var merged = Assembler.finalOutput(assembled);
        var memory = new Memory();
        memory.load(merged);
        var processor = new Processor(memory);
        processor.run();
        return processor;

    }
}
