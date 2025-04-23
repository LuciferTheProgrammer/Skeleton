import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// The CacheTests class contains my Testing Programs for the Cache. These test programs include
// summing 20 integers in an array, creating a 20-item linked list and summing them, and finally
// summing 20 integers in an array backwards.
public class CacheTests {

    /**
     * This method to test the performance using a test program of summing 20 integers in an array.
     * Measures clock cycle count, checks elements of the array, and also the total sum of the array.
     * Case 1: Summing 20 integers in an array.
     */
    @Test
    public void SumIntegersTest() {
        String[] sumArrayInt = {
                "copy 10 r6",       // Line 1
                "multiply 10 r6",
                "multiply 4 r6",    // Line 2
                "copy r6 r0",       // Give address space 400 to r0
                "copy r6 r0",       // Line 3 - Dummy
                "copy 15 r1",       // Data for each element in the array
                "copy 10 r2",       // Line 4
                "multiply 2 r2",    // Length 20 for the array
                "copy 1 r5",        // Line 5
                "store r1 r0",      // Stores value 15 in memory at address 400
                "add r5 r0",        // Line 6 - increment pointer by 1 to the next memory address
                "subtract 1 r2",    // Decrement length counter by 1
                "compare 0 r2",     // Line 7 - checks if the length counter of the array is 0
                "bne -2",           // If the array is still not fully processed, loop back to line 5
                "copy 10 r0",       // Line 8
                "multiply 10 r0",
                "multiply 4 r0",     // Line 9 - Give address space 400 to r0
                "copy 10 r2",
                "multiply 2 r2",     // Line 10 - Length 20
                "copy 0 r3",         // Accumulator initialized to 0
                "load r0 r4",        // Line 11 - load the value on memory address 400 into r4
                "add r4 r3",         // Add value to the accumulator
                "add r5 r0",         // Line 12 - increment pointer by 1 to the next memory address
                "subtract 1 r2",     // Decrement length counter by 1
                "compare 0 r2",      // Line 13 - checks if the length counter of the array is 0
                "bne -2",            // If the array is still not fully processed, loop back to line 11
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
    /**
     * This method to test the performance using a test program of summing 20 integers in an array backwards.
     * Measures clock cycle count, checks elements of the array, and also the total sum of the array.
     *  Case 3: Summing 20 integers in an array backwards (end to beginning).
     */
    @Test
    public void SumIntegersBackwardsTest() {
        String[] sumArrayIntBackwards = {
                "copy 10 r0",       // Line 1
                "multiply 10 r0",
                "multiply 4 r0",    // Line 2 - Give address space 400 to r0
                "copy 6 r1",        // Data for each element in the array
                "copy 10 r2",       // Line 3
                "multiply 2 r2",    // Length 20 for the array
                "copy 1 r5",        // Line 4
                "store r1 r0",      // Store value 6 in the memory address 400
                "add r5 r0",        // Line 5 - increment the pointer by 1 to the next memory address
                "subtract 1 r2",    // Decrement the length counter by 1
                "compare 0 r2",     // Line 6 - checks if the length counter of the array is 0
                "bne -2",           // If the array is still not fully processed, loop back to line 4
                "copy 10 r6",       // Line 7
                "multiply 10 r6",
                "multiply 2 r6",    // Line 8
                "multiply 2 r6",
                "add 15 r6",        // Line 9 - Get address space 419
                "add 4 r6",
                "copy 0 r0",        // Line 10
                "copy 0 r0",
                "add r6 r0",        // Line 11 - Give address space 419 to r0
                "copy 10 r2",
                "multiply 2 r2",    // Line 12 - Length 20 for the array
                "copy 0 r3",        // Initialize the accumulator
                "load r0 r4",       // Line 13 - load the value in memory address 419 to r4
                "add r4 r3",        // Add the value to the accumulator
                "subtract r5 r0",   // Line 14 - decrement the pointer by 1 to the next memory address
                "subtract 1 r2",    // Decrement the length counter by 1
                "compare 0 r2",     // Line 15 - checks if the length counter of the array is 0
                "bne -2",           // If the array is still not fully processed, loop back to line 13
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
