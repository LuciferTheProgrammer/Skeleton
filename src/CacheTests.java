import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// The CacheTests class contains my Testing Programs for the Cache. These test programs include
// summing 20 integers in an array, creating a 20-item linked list and summing them, and finally
// summing 20 integers in an array backwards.
public class CacheTests {

    /**
     * This method checks the performance by using a test program of summing 20 integers in an array.
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
                "copy 15 r1",       // Constant value for each element in the array
                "copy 10 r2",       // Line 4
                "multiply 2 r2",    // Length 20 for the array
                "copy 1 r5",        // Line 5
                "store r1 r0",      // Stores value 15 in memory at address 400
                "add r5 r0",        // Line 6 - increment the pointer by 1 to the next memory address
                "copy 1 r5",
                "copy 1 r5",        // Line 7 - Dummy
                "copy 1 r5",
                "copy 1 r5",        // Line 8 - Dummy
                "subtract 1 r2",    // Decrement length counter by 1
                "compare 0 r2",     // Line 9 - checks if the length counter of the array is 0
                "bne -4",           // If the array is still not fully processed, loop back to line 5
                "copy 10 r0",       // Line 10
                "multiply 10 r0",
                "multiply 4 r0",     // Line 11 - Give address space 400 to r0
                "copy 10 r2",
                "multiply 2 r2",     // Line 12 - Length 20
                "copy 0 r3",         // Accumulator initialized to 0
                "load r0 r4",        // Line 13 - load the value on memory address 400 into r4
                "add r4 r3",         // Add value to the accumulator
                "add r5 r0",         // Line 14 - increment the pointer by 1 to the next memory address
                "copy 1 r5",
                "copy 1 r5",         // Line 15 - Dummy
                "subtract 1 r2",     // Decrement length counter by 1
                "compare 0 r2",      // Line 16 - checks if the length counter of the array is 0
                "bne -3",            // If the array is still not fully processed, loop back to line 13
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

    /**
     * This method checks the performance by using a test program of creating a 20-item linked list of numbers
     * and summing them. Measures clock cycle count, checks data fields of the nodes, checks the pointer to the next
     * to the next node (memory address), and also the total sum of the linked list.
     * Case 2: Creating a 20-item linked list of numbers and summing them.
     */
    @Test
    public void LinkedListTest() {
        String[] LinkedList = {
                "copy 10 r0",       // Line 1
                "multiply 10 r0",
                "multiply 4 r0",    // Line 2 - Give Address Space 400 to r0
                "copy r0 r7",
                "copy 2 r1",        // Line 3 - Node size (1 32 word for data and 1 32 word for reference)
                "copy 5 r2",
                "leftshift 2 r2",   // Line 4 - Length 20 for the linked list
                "copy 10 r3",       // Constant value to be stored on data fields of the nodes
                "store r3 r0",      // Line 5 - Stores value 10 in memory address 400
                "copy r0 r4",       // Copy the node's base address to r4
                "add 1 r4",         // Line 6 - Increment by 1 to the next memory address (next field), which contains the reference to the next node
                "copy r0 r5",       // Copy node's base address to r5
                "add r1 r5",        // Line 7 - Increment by 2 to the next memory address, the next new node
                "store r5 r4",      // Stores the memory address of the new node to the previous node's next field (reference)
                "copy r5 r0",       // Line 8 - Move the pointer to the next node
                "subtract 1 r2",    // Decrement the length counter by 1
                "compare 0 r2",     // Line 9 - checks if the length counter of the array is 0
                "bne -4",           // If the linked list is still not fully processed, loop back to line 5
                "subtract r1 r0",   // Line 10 - Moves the pointer to the last node
                "add 1 r0",         // Increment the pointer by 1 to the next memory address, which is the last node's next field
                "copy 0 r6",        // Line 11
                "store r6 r0",      // Stores the default value of 0 in the last node's next field, which is a null pointer
                "copy r7 r0",       // Line 12 - Move the pointer back to the first head node.
                "copy 0 r3",        // Initialize the accumulator to 0
                "load r0 r4",       // Line 13 - load the value in memory address 400 (data field) into r4
                "add r4 r3",        // Add the value to the accumulator
                "copy r0 r5",       // Line 14 - copies over the current node address to r5
                "add 1 r5",         // Increments the pointer by 1 to the next memory address, which is the next field
                "load r5 r0",       // Line 15 - load the value in memory address r5 (next pointer) into r0
                "copy 0 r2",        // Dummy
                "compare 0 r0",     // Line 16 - checks if the next pointer is null, which means the end of the linked list has been reached.
                "bne -3",           // If the linked list has not been fully processed, loop back to line 13
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
     * This method checks the performance by using a test program of summing 20 integers in an array backwards.
     * Measures clock cycle count, checks elements of the array, and also the total sum of the array.
     *  Case 3: Summing 20 integers in an array backwards (end to beginning).
     */
    @Test
    public void SumIntegersBackwardsTest() {
        String[] sumArrayIntBackwards = {
                "copy 10 r0",       // Line 1
                "multiply 10 r0",
                "multiply 4 r0",    // Line 2 - Give address space 400 to r0
                "copy 6 r1",        // Constant value for each element in the array
                "copy 10 r2",       // Line 3
                "multiply 2 r2",    // Length 20 for the array
                "copy 1 r5",        // Line 4
                "store r1 r0",      // Store value 6 in the memory address 400
                "add r5 r0",        // Line 5 - increment the pointer by 1 to the next memory address
                "copy 1 r5",
                "copy 1 r5",        // Line 6 - Dummy
                "copy 1 r5",
                "copy 1 r5",        // Line 7 - Dummy
                "subtract 1 r2",    // Decrement the length counter by 1
                "compare 0 r2",     // Line 8 - checks if the length counter of the array is 0
                "bne -4",           // If the array is still not fully processed, loop back to line 4
                "copy 10 r6",       // Line 9
                "multiply 10 r6",
                "multiply 2 r6",    // Line 10
                "multiply 2 r6",
                "add 15 r6",        // Line 11 - Get address space 419
                "add 4 r6",
                "copy 0 r0",        // Line 12
                "copy 0 r0",
                "add r6 r0",        // Line 13 - Give address space 419 to r0
                "copy 10 r2",
                "multiply 2 r2",    // Line 14 - Length 20 for the array
                "copy 0 r3",        // Initialize the accumulator
                "load r0 r4",       // Line 15 - load the value in memory address 419 to r4
                "add r4 r3",        // Add the value to the accumulator
                "subtract r5 r0",   // Line 16 - decrement the pointer by 1 to the next memory address
                "copy 1 r5",
                "copy 1 r5",        // Line 17 - Dummy
                "subtract 1 r2",    // Decrement the length counter by 1
                "compare 0 r2",     // Line 18 - checks if the length counter of the array is 0
                "bne -3",           // If the array is still not fully processed, loop back to line 15
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

    /**
     * The method that executes the processor program.
     *
     * @param placement The array of strings that contains sets of instructions.
     *
     * @return The processor that runs the program.
     */
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
