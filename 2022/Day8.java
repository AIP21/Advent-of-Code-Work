import java.beans.DesignMode;
import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;

public class Day8 extends AbstractDay {
    public static void main(String[] args) {
        new Day8();
    }

    public Day8() {
        super(false);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
        ArrayList<ArrayList<String>> visibility = new ArrayList<>();

        for (String line : lines) {
            ArrayList<Integer> nums = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                nums.add(Integer.parseInt(line.substring(i, i + 1)));
            }

            grid.add(nums);
        }

        // for (ArrayList<Integer> row : grid) {
        // print(row);
        // }

        // print("\n");

        for (int i = 0; i < grid.size(); i++) {
            ArrayList<String> row = new ArrayList<>();

            for (int j = 0; j < grid.get(i).size(); j++) {
                if (i == 0 || i == grid.size() - 1)
                    row.add("T");
                else if (j == 0 || j == grid.get(i).size() - 1) {
                    row.add("T");
                } else {
                    int thisVal = grid.get(i).get(j);

                    boolean leftVisible = true;
                    boolean rightVisible = true;
                    for (int x = 0; x < j; x++) {
                        if (grid.get(i).get(x) >= thisVal) {
                            leftVisible = false;
                            break;
                        }
                    }
                    for (int x = j + 1; x < grid.get(i).size(); x++) {
                        if (grid.get(i).get(x) >= thisVal) {
                            rightVisible = false;
                            break;
                        }
                    }

                    boolean aboveVisible = true;
                    boolean belowVisible = true;
                    for (int y = 0; y < i; y++) {
                        if (grid.get(y).get(j) >= thisVal) {
                            aboveVisible = false;
                            break;
                        }
                    }
                    for (int y = i + 1; y < grid.size(); y++) {
                        if (grid.get(y).get(j) >= thisVal) {
                            belowVisible = false;
                            break;
                        }
                    }

                    row.add((leftVisible || rightVisible || aboveVisible || belowVisible) ? "T" : "F");
                }
            }
            visibility.add(row);
        }

        int visibleCount = 0;
        for (ArrayList<String> row : visibility) {
            print(row);
            for (String tree : row) {
                if (tree == "T")
                    visibleCount++;
            }
        }

        print(visibleCount);
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
        ArrayList<ArrayList<Integer>> scores = new ArrayList<>();

        for (String line : lines) {
            ArrayList<Integer> nums = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                nums.add(Integer.parseInt(line.substring(i, i + 1)));
            }

            grid.add(nums);
        }

        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Integer> rowScore = new ArrayList<>();

            for (int j = 0; j < grid.get(i).size(); j++) {
                int thisVal = grid.get(i).get(j);

                int leftDist = 0;
                int rightDist = 0;
                for (int x = j - 1; x >= 0; x--) {
                    leftDist++;
                    if (grid.get(i).get(x) >= thisVal) {
                        break;
                    }
                }
                for (int x = j + 1; x < grid.get(i).size(); x++) {
                    rightDist++;
                    if (grid.get(i).get(x) >= thisVal) {
                        break;
                    }
                }

                int aboveDist = 0;
                int belowDist = 0;
                for (int y = i - 1; y >= 0; y--) {
                    aboveDist++;
                    if (grid.get(y).get(j) >= thisVal) {
                        break;
                    }
                }
                for (int y = i + 1; y < grid.size(); y++) {
                    belowDist++;
                    if (grid.get(y).get(j) >= thisVal) {
                        break;
                    }
                }

                rowScore.add(leftDist * rightDist * aboveDist * belowDist);

                if (leftDist * rightDist * aboveDist * belowDist != 0)
                    print("found at " + i + ", " + j + " with distances " + leftDist + ", " + rightDist + ", "
                            + aboveDist + ", " + belowDist);
            }
            scores.add(rowScore);
        }

        int highestScore = 0;
        for (ArrayList<Integer> row : scores) {
            print(row);
            for (int score : row) {
                if (score > highestScore)
                    highestScore = score;
            }
        }

        print(highestScore);
    }
}