import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssemblerTest2 {

    // Test for an even number of 16-bit lines
    @Test
    void finalOutputEvenTest() {
        // Each string is 16 characters representing 16 bits (e.g., 16 t's or f's)
        String[] input = {
                "1111111111111111",
                "0000000000000000",
                "1010101010101010",
                "0101010101010101"
        };
        // Overlapping merge: 
        // merge[0] = input[0] + input[1]
        // merge[1] = input[1] + input[2]
        // merge[2] = input[2] + input[3]
        String[] expected = {
                "1111111111111111" + "0000000000000000",
                "0000000000000000" + "1010101010101010",
                "1010101010101010" + "0101010101010101"
        };
        String[] result = Assembler.finalOutput(input);
        assertArrayEquals(expected, result, "Even case finalOutput merging failed.");
    }

    // Test for an odd number of 16-bit lines
    @Test
    void finalOutputOddTest() {
        String[] input = {
                "AAAAAAAAAAAAAAAA",
                "BBBBBBBBBBBBBBBB",
                "CCCCCCCCCCCCCCCC"
        };
        // With an odd number of lines, the method appends "ffffffffffffffff" to the end.
        // Merging happens as follows:
        // merge[0] = input[0] + input[1]
        // merge[1] = input[1] + input[2]
        // merge[2] = input[2] + "ffffffffffffffff"
        String[] expected = {
                "AAAAAAAAAAAAAAAA" + "BBBBBBBBBBBBBBBB",
                "BBBBBBBBBBBBBBBB" + "CCCCCCCCCCCCCCCC",
                "CCCCCCCCCCCCCCCC" + "ffffffffffffffff"
        };
        String[] result = Assembler.finalOutput(input);
        assertArrayEquals(expected, result, "Odd case finalOutput merging failed.");
    }
}