import java.beans.DesignMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Stack;

public class Day6 extends AbstractDay {
    public static void main(String[] args) {
        new Day6();
    }

    public Day6() {
        super(true);
    }

    @Override
    public void part1() {
        String line = inputUtils.getLine(0);

        ArrayList<Character> lastFourChars = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (lastFourChars.size() == 4) {
                lastFourChars.remove(0);
                lastFourChars.add(line.charAt(i));

                // find if there are no repeats in the last four chars
                boolean noRepeats = false;
                for (int j = 0; j < lastFourChars.size(); j++) {
                    char c = lastFourChars.get(j);
                    if (Collections.frequency(lastFourChars, c) == 1) {
                        noRepeats = true;
                    } else {
                        noRepeats = false;
                        break;
                    }
                }

                if (noRepeats) {
                    print("Found no repeats at index " + (i + 1));
                    for (char c : lastFourChars) {
                        print(c);
                    }
                    break;
                }
            } else {
                lastFourChars.add(line.charAt(i));
            }
        }
    }

    @Override
    public void part2() {
        String line = inputUtils.getLine(0);

        ArrayList<Character> lastFourteenChars = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (lastFourteenChars.size() == 14) {
                lastFourteenChars.remove(0);
                lastFourteenChars.add(line.charAt(i));

                // find if there are no repeats in the last four chars
                boolean noRepeats = false;
                for (int j = 0; j < lastFourteenChars.size(); j++) {
                    char c = lastFourteenChars.get(j);
                    if (Collections.frequency(lastFourteenChars, c) == 1) {
                        noRepeats = true;
                    } else {
                        noRepeats = false;
                        break;
                    }
                }

                if (noRepeats) {
                    print("Found no repeats at index " + (i + 1));
                    for (char c : lastFourteenChars) {
                        print(c);
                    }
                    break;
                }
            } else {
                lastFourteenChars.add(line.charAt(i));
            }
        }
    }
}