import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTests {

    // Case 1:
    @Test
    public void SumIntegersTest() {
        String[] sumArrayInt = {
                "copy 10 r6",
                "multiply 10 r6",
                "multiply 4 r6",
                "copy r6 r0", // Get address space 400 to r0
                "copy r6 r0",
                "copy 15 r1", // Fill in value 1
                "copy 10 r2",
                "multiply 2 r2", // Length 20
                "copy 1 r5",
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
        Word32[] r = processor1.getRegisters();
        System.out.println("baseAddressHolder: " + TestConverter.toInt(r[6]));
        System.out.println("Total Value: " + TestConverter.toInt(r[3]));
        processor1.printMyArrayMemory();
        System.out.println();
        assertEquals(300, TestConverter.toInt(r[3]));
        assertEquals("405:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t,t,", processor1.output.get(5));
        assertEquals("406:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t,t,", processor1.output.get(6));
        assertEquals("415:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t,t,", processor1.output.get(15));
    }

    // Case 2:
    @Test
    public void LinkedListTest() {
        String[] LinkedList = {
                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get Address Space 400 to r0.
                "copy r0 r7",
                "copy 2 r1", // Node size (1 32 word for data and 1 32 word for reference)
                "copy 5 r2",
                "leftshift 2 r2",  // Length 20
                "copy 10 r3", // Constant value to be stored on data fields.
                "store r3 r0",
                "copy r0 r4",
                "add 1 r4",
                "copy r0 r5",
                "add r1 r5",
                "store r5 r4",
                "copy r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -4",
                "subtract r1 r0", // Address 440-2 = 438 + 1 = 439 -> 0
                "add 1 r0",
                "copy 0 r6",
                "store r6 r0",
                "copy r7 r0",
                "copy 0 r3",
                "load r0 r4", // line 1
                "add r4 r3",
                "copy r0 r5",
                "add 1 r5",  // line 2
                "load r5 r0",
                "copy 0 r2", // Dummy
                "compare 0 r0", // line 3
                "bne -3",
                "halt"
        };
        System.out.println("Case 2: LinkedList of size 20 and sum: ");
        var processor2 = runMyPro(LinkedList);
        Word32[] r = processor2.getRegisters();
        System.out.println("baseAddressHolder: " + TestConverter.toInt(r[7]));
        System.out.println("Total Value: " + TestConverter.toInt(r[3]));
        processor2.printMyArrayMemory();
        System.out.println();
        assertEquals(200, TestConverter.toInt(r[3]));
        assertEquals("400:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,", processor2.output.get(0));
        assertEquals("401:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,f,t,f,f,t,f,", processor2.output.get(1));
        assertEquals("402:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,", processor2.output.get(2));
        assertEquals("403:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,f,t,f,t,f,f,", processor2.output.get(3));
    }
    // Case 3:
    @Test
    public void SumIntegersBackwardsTest() {
        String[] sumArrayIntBackwards = {
                "copy 10 r0",
                "multiply 10 r0",
                "multiply 4 r0", // Get address space 400 to r0
                "copy 6 r1", // Fill in value 1
                "copy 10 r2",
                "multiply 2 r2", // Length 20
                "copy 1 r5",
                "store r1 r0",
                "add r5 r0",
                "subtract 1 r2",
                "compare 0 r2",
                "bne -2",
                "copy 10 r6",
                "multiply 10 r6",
                "multiply 2 r6",
                "multiply 2 r6",
                "add 15 r6",
                "add 4 r6",
                "copy 0 r0", // Dummy
                "copy 0 r0",
                "add r6 r0", // Get address space 419 to r0
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
        Word32[] r = processor3.getRegisters();
        System.out.println("baseAddressHolder: " + TestConverter.toInt(r[6]));
        System.out.println("Total Value: " + TestConverter.toInt(r[3]));
        processor3.printMyArrayMemory();
        assertEquals(120, TestConverter.toInt(r[3]));
        assertEquals("409:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,", processor3.output.get(9));
        assertEquals("413:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,", processor3.output.get(13));
        assertEquals("419:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,", processor3.output.get(19));
    }

    private static Processor runMyPro(String[] placement) {
        var assembled = Assembler.assemble(placement);
        var merged = Assembler.finalOutput(assembled);
        var memory = new Memory();
        memory.load(merged);
        var processor = new Processor(memory);
        processor.run();
        return processor;
    }
}
