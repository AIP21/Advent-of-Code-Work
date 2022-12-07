import java.util.ArrayList;

public class Day3 extends AbstractDay {
    public static void main(String[] args) {
        new Day3();
    }

    public Day3() {
        super(false);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        int totalValue = 0;

        for (String str : lines) {
            int len = str.length();
            String a = str.substring(0, len / 2);
            String b = str.substring(len / 2, len);

            int lineVal = 0;

            for (int i = 0; i < a.length(); i++) {
                if (b.contains(Character.toString(a.charAt(i)))) {
                    char similarChar = a.toLowerCase().charAt(i);
                    boolean upperCase = Character.isUpperCase(a.charAt(i));

                    int val = similarChar - 'a' + 1;
                    val += upperCase ? 26 : 0;
                    lineVal += val;
                    break;
                }
            }

            totalValue += lineVal;
        }

        print(totalValue);
    }

    @Override
    public void part2() {
        int totalValue = 0;

        for (int l = 0; l < inputUtils.getLineCount(); l += 3) {
            String[] strs = inputUtils.getLines(l, l + 3).toArray(new String[3]);

            // Find common char in each string in strs
            for (int i = 0; i < strs[0].length(); i++) {
                char c = strs[0].charAt(i);
                if (strs[1].contains(Character.toString(c)) && strs[2].contains(Character.toString(c))) {
                    char similarChar = c;
                    boolean upperCase = Character.isUpperCase(similarChar);

                    int val = strs[0].toLowerCase().charAt(i) - 'a' + 1;
                    val += upperCase ? 26 : 0;
                    totalValue += val;
                    break;
                }
            }

        }

        print(totalValue);
    }

    public void print(Object o) {
        System.out.println(o);
    }
}