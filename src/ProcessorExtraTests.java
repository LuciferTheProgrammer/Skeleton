import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessorExtraTests {

    // Helper method: run the program by assembling, merging, loading memory, and running the processor.
    private Processor runProgram(String[] program) {
        String[] assembled = Assembler.assemble(program);
        String[] merged = Assembler.finalOutput(assembled);
        Memory m = new Memory();
        m.load(merged);
        Processor p = new Processor(m);
        p.run();
        return p;
    }

    // Helper method: convert an integer into the bit-string representation produced by Word32.toString()
    private String convert(int value) {
        Word32 w = new Word32();
        TestConverter.fromInt(value, w);
        return w.toString(); // e.g., "f,f,...,t,f,t,"
    }

    // Test for the compare instruction.
    @Test
    public void testCompare() {
        // In this test:
        //  - "copy 5 r0" sets r0 to 5.
        //  - "copy 10 r1" sets r1 to 10.
        //  - "compare r0 r1" uses 2R format: source = r0, destination = r1.
        //     The ALU.compare then sets flags and, in our design, the result (a new Word32, all false) is copied into r1.
        //  - "syscall 0" prints registers.
        String[] program = {
                "copy 5 r0",
                "copy 10 r1",
                "compare r0 r1",
                "syscall 0"
        };
        Processor p = runProgram(program);
        // Expected:
        // r0 should remain 5 and r1 should become 0 (since compare yields 0).
        String expectedR0 = "r0:" + convert(5);
        String expectedR1 = "r1:" + convert(0);
        // Assuming your syscall prints registers in order: r0 on output index 0, r1 on index 1, etc.
        assertEquals(expectedR0, p.output.get(0), "r0 is incorrect after compare");
        assertEquals(expectedR1, p.output.get(1), "r1 is incorrect after compare");
    }

    // Test for the load instruction.
    @Test
    public void testLoad() {
        // In this test:
        //  - "copy 123 r5" sets r5 to 123.
        //  - "store r5 r7" stores the value in r5 into memory at the address in r7 (assumed to be 0).
        //  - "load r7 r6" loads from memory at address in r7 into r6 (so r6 should become 123).
        //  - "syscall 0" prints registers.
        String[] program = {
                "copy 123 r5",
                "store r5 r7",
                "load r7 r6",
                "syscall 0"
        };
        Processor p = runProgram(program);
        // Expected: r6 should contain 123.
        String expectedR6 = "r6:" + convert(123);
        // Assuming registers are printed in order (r0 at index 0, r1 at index 1, ..., r6 at index 6)
        assertEquals(expectedR6, p.output.get(6), "r6 is incorrect after load");
    }

    // Test for the store instruction.
    @Test
    public void testStore() {
        // In this test:
        //  - "copy 456 r5" sets r5 to 456.
        //  - "store r5 r7" stores r5 into memory at the address in r7 (assumed to be 0).
        //  - "syscall 1" prints memory contents.
        String[] program = {
                "copy 456 r5",
                "store r5 r7",
                "syscall 1"
        };
        Processor p = runProgram(program);
        // Expected: Memory at address 0 should contain 456.
        String expectedMem0 = "0:" + convert(456);
        // Assuming syscall 1 prints memory starting with address 0 on output index 0.
        assertEquals(expectedMem0, p.output.get(0), "Memory at address 0 is incorrect after store");
    }
}
