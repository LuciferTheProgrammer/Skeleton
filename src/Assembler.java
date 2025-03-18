import java.util.HashMap;
import java.util.LinkedList;

public class Assembler {
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
            String str = input[i].trim();
            String [] tokenizer = str.split("\\s+");
            if(opCode.containsKey(tokenizer[0])) {
                if(tokenizer.length == 1) {
                    switch(tokenizer[0]) {
                        case "halt":
                            assembled.add("ffffffffffffffff");
                            break;
                        case "return":
                            assembled.add(opCode.get(tokenizer[0]) + "fffffffffff");
                            break;
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
        }
        String [] value = assembled.toArray(new String[0]);
        return value;
    }

    public static String[] finalOutput(String[] input) {
        return null;
    }

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
