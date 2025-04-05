import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

public class MyProcessorTest {

    @Test
    public void testStoreImmediateInstruction() throws Exception {
        // This program will:
        // 1. Copy the constant 10 into register 11 (r11) to be used as the memory address.
        // 2. Use the STORE instruction in immediate format ("store 12 r11")
        //    to store the immediate value 12 into memory at the address held in r11.
        // 3. Syscall 1 prints all memory locations.
        // 4. Halt.
        //
        // We expect memory location 10 to contain the value 12.
        String[] program = {
                "copy 10 r11",    // r11 := 10 (memory address)
                "store 12 r11",   // immediate STORE: store immediate 12 into memory[r11]
                "syscall 1",      // Print memory contents (assumed to print memory line at index equal to the address)
                "halt"
        };

        // Assemble and merge the program (using your existing Assembler methods)
        String[] assembled = Assembler.assemble(program);
        String[] merged = Assembler.finalOutput(assembled);

        // Create a Memory instance and load the program
        Memory mem = new Memory();
        mem.load(merged);

        // Create a Processor instance with this Memory and run the program
        Processor proc = new Processor(mem);
        proc.run();

        // Use reflection to access the private dram array from Memory
        Field dramField = Memory.class.getDeclaredField("dram");
        dramField.setAccessible(true);
        Word32[] dram = (Word32[]) dramField.get(mem);

        // Construct the expected value for memory location 10 (which should be 12)
        Word32 expectedWord = new Word32();
        TestConverter.fromInt(12, expectedWord);
        String expected = "10:" + expectedWord.toString();

        // Assuming syscall 1 prints memory starting at address 0, the output line for memory location 10 is at index 10.
        String actual = proc.output.get(10);
        assertEquals(expected, actual, "After an immediate STORE, memory location 10 should contain the value 12.");
    }
}
