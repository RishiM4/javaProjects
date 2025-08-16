public class NumberConverter {
    public void convert(int input, int base) {
        int results[] = new int[(int)(Math.log(input)/Math.log(base))+1];
        convert(input, base, results, results.length-1);
        for(int i = 0; i < results.length; i++) {
            System.err.println(results[i]);
        }
    }
    private void convert(int input, int base, int[] results, int depth) {
        if (depth == -1) {
            return;
        }
        int count = 0;
        while (input >= Math.pow(base, depth)) {
            input -= Math.pow(base, depth);
            count++;
        }
        results[depth] = count;
        convert(input, base, results, depth-1);

    }
    public static void main(String[] args) {
       new NumberConverter().convert(64, 5);
    }
}
