import java.util.HashMap;
import java.util.LinkedList;

// The Assembler Class has an assemble method that takes in a set of instructions as an
// array of Strings, which is then tokenized and outputs an array of bit Strings (16 t's and/or f's).
// Then finally has a final output method which takes in an array of Strings and merges each consecutive
// 2 line Strings into one and then returns an array of Strings with merged 32 bit lines.
public class Assembler {

    /**
     * This method creates a Linked List to hold the assembled bit Strings of length 16, a
     * Hash Map to hold the op code and another Hash Map to hold the registers. Then this method
     * takes in an array of Strings which gets tokenized. Then the first token is utilized to look up
     * any matching keys on the Hash Map for the op code, if there is a match then depending on the number
     * of tokens for that specific String (length 1, 2, or 3), a branch case generates and appends
     * a bit String of length 16 to the Linked List. Once all the Strings have been processed, then
     * the Linked List of assembled bit Strings of length 16 is converted to an array of Strings which is
     * then returned.
     *
     * @param input The array of Strings, instructions.
     * @return The array of Strings, bit Strings of length 16.
     */
    public static String[] assemble(String[] input) {
        LinkedList <String> assembled = new LinkedList<>();
        HashMap <String, String> opCode = new HashMap<>();
        opCode.put("add", "fffft");
        opCode.put("syscall", "ftfff");
        opCode.put("return", "ftftf");
        opCode.put("subtract", "fftft");
        opCode.put("halt", "fffff");
        opCode.put("and", "ffftf");
        opCode.put("multiply", "ffftt");
        opCode.put("leftshift", "fftff");
        opCode.put("or", "ffttf");
        opCode.put("rightshift", "ffttt");
        opCode.put("call", "ftfft");
        opCode.put("compare", "ftftt");
        opCode.put("ble", "fttff");
        opCode.put("blt", "fttft");
        opCode.put("bge", "ftttf");
        opCode.put("bgt", "ftttt");
        opCode.put("beq", "tffff");
        opCode.put("bne", "tffft");
        opCode.put("load", "tfftf");
        opCode.put("store", "tfftt");
        opCode.put("copy", "tftff");

        HashMap <String, String> register = new HashMap<>();
        register.put("r0", "fffff");
        register.put("r1", "fffft");
        register.put("r2", "ffftf");
        register.put("r3", "ffftt");
        register.put("r4", "fftff");
        register.put("r5", "fftft");
        register.put("r6", "ffttf");
        register.put("r7", "ffttt");
        register.put("r8", "ftfff");
        register.put("r9", "ftfft");
        register.put("r10", "ftftf");
        register.put("r11", "ftftt");
        register.put("r12", "fttff");
        register.put("r13", "fttft");
        register.put("r14", "ftttf");
        register.put("r15", "ftttt");
        register.put("r16", "tffff");
        register.put("r17", "tffft");
        register.put("r18", "tfftf");
        register.put("r19", "tfftt");
        register.put("r20", "tftff");
        register.put("r21", "tftft");
        register.put("r22", "tfttf");
        register.put("r23", "tfttt");
        register.put("r24", "ttfff");
        register.put("r25", "ttfft");
        register.put("r26", "ttftf");
        register.put("r27", "ttftt");
        register.put("r28", "tttff");
        register.put("r29", "tttft");
        register.put("r30", "ttttf");
        register.put("r31", "ttttt");

        for(int i = 0; i < input.length; i++) {
            String [] tokenizer = input[i].split(" ");
            if(opCode.containsKey(tokenizer[0])) {
                if(tokenizer.length == 1) {
                    switch (tokenizer[0]) {
                        case "halt":
                            assembled.add("ffffffffffffffff");
                            break;
                        case "return":
                            assembled.add(opCode.get(tokenizer[0]) + "fffffffffff");
                            break;
                    }
                }
                if(tokenizer.length == 2) {
                    switch(tokenizer[0]) {
                        case "syscall":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "call":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "ble":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "blt":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "bge":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "bgt":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "beq":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;
                        case "bne":
                            assembled.add(opCode.get(tokenizer[0]) + convertedValue11(tokenizer[1]));
                            break;

                    }
                }
                if(tokenizer.length == 3) {
                    if(register.containsKey(tokenizer[1])) {
                        assembled.add(opCode.get(tokenizer[0]) + "f" + register.get(tokenizer[1]) + register.get(tokenizer[2]));
                    }
                    else
                        assembled.add(opCode.get(tokenizer[0]) + "t" + convertedValue5(tokenizer[1])+ register.get(tokenizer[2]));
                }
            }
        }
        return assembled.toArray(new String[0]);
    }

    /**
     * This method creates a Linked List to hold the resulting merged 32 bit line Strings. This
     * method takes in an array of Strings and merges each consecutive 2 lines into one line of String
     * which is then appended to the Linked List. If the length of the array of Strings is odd, a 16 bit
     * ("ffffffffffffffff") is added at the end to make it even. Finally, the Linked List of the result
     * is converted to an array of Strings which is then returned.
     *
     * @param input The array of Strings.
     * @return The resulting 32 bit line(s) array of Strings.
     */
    public static String[] finalOutput(String[] input) {
        LinkedList<String> output = new LinkedList<>();
        if(input.length % 2 == 0) {
            for(int i = 0; i < input.length - 1; i+=2) {
                output.add(input[i] + input[i + 1]);
            }
        }
        else {
            String[] temp = new String[input.length + 1];
            for(int i = 0; i < input.length; i++) {
                temp[i] = input[i];
            }
            temp[temp.length - 1] = "ffffffffffffffff";
            for (int i = 0; i < temp.length - 1; i+=2) {
                output.add(temp[i] + temp[i + 1]);
            }
        }
        return output.toArray(new String[0]);
    }

    /**
     * This method takes in a String which is then converted to an integer. In addition, the integer
     * is then converted to its 11 bit String binary representation through divisions of 2 by taking the
     * remainder for each iteration. If the remainder is 0 then a String representation of "f" is appended
     * to the resulting String. Otherwise, "t" is appended to the resulting String. Finally, the resulting
     * String is returned.
     *
     * @param input The String to be processed.
     * @return The 11 bit String binary representation.
     */
    public static String convertedValue11(String input) {
        StringBuilder returnValue = new StringBuilder();
        int holder = Integer.parseInt(input);
        if(holder < 0)
            holder = holder + 2048;
        for(int i = 0; i < 11; i++) {
            int remainder = (holder % 2);
            holder = holder / 2;
            if(remainder == 0)
                returnValue.append("f");
            else
                returnValue.append("t");
        }
        return returnValue.reverse().toString();
    }

    /**
     * This method takes in a String which is then converted to an integer. In addition, the integer
     * is then converted to its 5 bit String binary representation through divisions of 2 by taking the
     * remainder for each iteration. If the remainder is 0 then a String representation of "f" is appended
     * to the resulting String. Otherwise, "t" is appended to the resulting String. Finally, the resulting
     * String is returned.
     *
     * @param input The String to be processed.
     * @return The 5 bit String binary representation.
     */
    public static String convertedValue5(String input) {
        StringBuilder returnValue = new StringBuilder();
        int holder = Integer.parseInt(input);
        if(holder < 0)
            holder = holder + 32;
        for(int i = 0; i < 5; i++) {
            int remainder = (holder % 2);
            holder = holder / 2;
            if(remainder == 0)
                returnValue.append("f");
            else
                returnValue.append("t");
        }
        return returnValue.reverse().toString();
    }
}
