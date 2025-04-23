public class Sum {
    public static void main(String[] args) {
        int[] array = new int[20];
        for(int i = 0; i < array.length; i++) {
            array[i] = 10;
        }
        int accumulator = 0;
        for(int i = 0; i < array.length; i++) {
            accumulator += array[i];
        }
        System.out.println("Total of array of size 20 containing all values of 10: " + accumulator);
        Word32 word = new Word32();
        TestConverter.fromInt(10, word);
        System.out.println(word.toString());
    }
}
