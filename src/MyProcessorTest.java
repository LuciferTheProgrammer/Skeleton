import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyProcessorTest {

    // Helper method to run a program.
    private static Processor runProgram(String[] program) {
        String[] assembled = Assembler.assemble(program);
        String[] merged = Assembler.finalOutput(assembled);
        Memory m = new Memory();
        m.load(merged);
        Processor p = new Processor(m);
        p.run();
        return p;
    }

    // Test ADD: adds 10 to r0 (initially 0) so r0 becomes 10.
    @Test
    public void testAdd() {
        String[] program = {
                "add 10 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        String expected = "r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,";
        assertEquals(expected, p.output.get(0));
    }

    // Test SUBTRACT: subtract 3 from r0 (initially 0) so r0 becomes -3.
    @Test
    public void testSubtract() {
        String[] program = {
                "subtract 3 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        // Expected: -3 in two's complement (assuming your toString prints bits in order)
        // For example, if -3 is represented as 11111111 11111111 11111111 11111101,
        // the expected string might be:
        String expected = "r0:" +
                "t,".repeat(29) + // 29 ones for the high–order bits
                "t,f,t,";         // last 3 bits: 1,0,1 (for -3)
        // (Adjust the expected string to match your actual formatting.)
        assertEquals(expected, p.output.get(0));
    }

    // Test AND: r0 = r0 AND 10. (0 AND anything remains 0.)
    @Test
    public void testAnd() {
        String[] program = {
                "and 10 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        String expected = "r0:" + "f,".repeat(32);
        assertEquals(expected, p.output.get(0));
    }

    // Test OR: r0 = r0 OR 10. (Since r0 is initially 0, result should be 10.)
    @Test
    public void testOr() {
        String[] program = {
                "or 10 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        // 10 in binary is 000...1010: 28 f's then: t, f, t, f,
        String expected = "r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,";
        assertEquals(expected, p.output.get(0));
    }

    // Test MULTIPLY: r0 = r0 * 4. (0 * 4 = 0.)
    @Test
    public void testMultiply() {
        String[] program = {
                "multiply 4 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        String expected = "r0:" + "f,".repeat(32);
        assertEquals(expected, p.output.get(0));
    }

    // Test LEFTSHIFT: r0 = r0 << 3. (Shifting 0 remains 0.)
    @Test
    public void testLeftShift() {
        String[] program = {
                "leftshift 3 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        String expected = "r0:" + "f,".repeat(32);
        assertEquals(expected, p.output.get(0));
    }

    // Test RIGHTSHIFT: r0 = r0 >> 3. (Shifting 0 remains 0.)
    @Test
    public void testRightShift() {
        String[] program = {
                "rightshift 3 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        String expected = "r0:" + "f,".repeat(32);
        assertEquals(expected, p.output.get(0));
    }

    // Test COPY: copy 7 into r0.
    @Test
    public void testCopy() {
        String[] program = {
                "copy 7 r0",
                "syscall 0"
        };
        Processor p = runProgram(program);
        // For 7 the correct representation is 29 false bits followed by three true bits.
        String expected = "r0:" + "f,".repeat(29) + "t,t,t,";
        assertEquals(expected, p.output.get(0));
    }
}
