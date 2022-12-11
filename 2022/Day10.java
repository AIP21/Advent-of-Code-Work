import java.beans.DesignMode;
import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;

public class Day10 extends AbstractDay {
    public static void main(String[] args) {
        new Day10();
    }

    public Day10() {
        super(true);
    }

    int cycle = 0;
    int signalStrengthSum = 0;

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        int xVal = 1;

        for (String op : lines) {
            String[] sides = op.split(" ");

            if (!sides[0].contains("noop")) {
                cycleTick(xVal);
                cycleTick(xVal);
                xVal += Integer.parseInt(sides[1]);
            } else {
                cycleTick(xVal);
            }
        }

        print("X Register: " + xVal);

        print("Signal strength: " + signalStrengthSum);
    }

    void cycleTick(int xVal) {
        cycle++;

        if (cycle == 20 || (cycle - 20) % 40 == 0) {
            int signalStrength = cycle * xVal;
            signalStrengthSum += signalStrength;
            print("Cycle " + cycle + ", xVal: " + xVal + ", signal strength: " + signalStrength);
        }
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        int xVal = 1;

        for (String op : lines) {
            String[] sides = op.split(" ");

            if (!sides[0].contains("noop")) {
                cycleTick2(xVal);
                cycleTick2(xVal);
                xVal += Integer.parseInt(sides[1]);
            } else {
                cycleTick2(xVal);
            }
        }

        print("");
        print("X Register: " + xVal);

        print("Signal strength: " + signalStrengthSum);
    }

    void cycleTick2(int xVal) {
        cycle++;

        int cycleMod = (cycle - 1) % 40;
        String litStr = Math.abs((cycleMod) - xVal) <= 1 ? "#" : ".";
        if (cycleMod == 39) {
            // print(litStr + " -- " + cycle);
            print(litStr);
        } else {
            print(litStr, false);
        }
    }
}