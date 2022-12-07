import java.beans.DesignMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Day5 extends AbstractDay {
    public static void main(String[] args) {
        new Day5();
    }

    public Day5() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> startLines = inputUtils.getLines(0, 8);

        LinkedHashMap<Integer, ArrayList<Character>> setup = new LinkedHashMap<>();

        for (String line : startLines) {
            line = line.replace(" [", "").replace("] ", "").replace("[", "").replace("]", "");
            line = line.replace("   ", "-");
            line = line.replace(" ", "");
            print(line);

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != '-') {
                    if (setup.get(i + 1) != null) {
                        setup.get(i + 1).add(line.charAt(i));
                    } else {
                        setup.put(i + 1, new ArrayList<Character>());
                        setup.get(i + 1).add(line.charAt(i));
                    }
                }
            }
        }

        print(setup);

        ArrayList<String> commands = inputUtils.getLines(10, inputUtils.getLineCount());

        for (String command : commands) {
            String fixed = command.replace("move ", "").replace("from ", "").replace("to ", "");
            String[] parts = fixed.split(" ");

            int count = Integer.parseInt(parts[0]);
            int origin = Integer.parseInt(parts[1]);
            int destination = Integer.parseInt(parts[2]);

            print(count + ", " + origin + ", " + destination);
            for (int i = 0; i < count; i++) {
                ArrayList<Character> oldDest = setup.get(destination);
                ArrayList<Character> newDest = new ArrayList<>();
                newDest.add(setup.get(origin).get(0));
                newDest.addAll(oldDest);

                setup.get(destination).clear();
                setup.get(destination).addAll(newDest);

                setup.get(origin).remove(0);
            }

            print(setup);
        }

        String topCrates = "";

        for (int i = 1; i <= setup.size(); i++) {
            topCrates += setup.get(i).get(0);
        }

        print(topCrates);

        /*
         * [D]
         * [N] [C]
         * [Z] [M] [P]
         * 1 2 3
         * 
         * move 1 from 2 to 1
         * move 3 from 1 to 3
         * move 2 from 2 to 1
         * move 1 from 1 to 2
         */
    }

    @Override
    public void part2() {
        ArrayList<String> startLines = inputUtils.getLines(0, 8);

        LinkedHashMap<Integer, ArrayList<Character>> setup = new LinkedHashMap<>();

        for (String line : startLines) {
            line = line.replace(" [", "").replace("] ", "").replace("[", "").replace("]", "");
            line = line.replace("   ", "-");
            line = line.replace(" ", "");
            print(line);

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != '-') {
                    if (setup.get(i + 1) != null) {
                        setup.get(i + 1).add(line.charAt(i));
                    } else {
                        setup.put(i + 1, new ArrayList<Character>());
                        setup.get(i + 1).add(line.charAt(i));
                    }
                }
            }
        }

        print(setup);

        ArrayList<String> commands = inputUtils.getLines(10, inputUtils.getLineCount());

        for (String command : commands) {
            String fixed = command.replace("move ", "").replace("from ", "").replace("to ", "");
            String[] parts = fixed.split(" ");

            int count = Integer.parseInt(parts[0]);
            int origin = Integer.parseInt(parts[1]);
            int destination = Integer.parseInt(parts[2]);

            print(count + ", " + origin + ", " + destination);

            ArrayList<Character> movingStack = new ArrayList<>(setup.get(origin).subList(0, count));
            for (int i = count; i > 0; i--) {
                setup.get(origin).remove(0);
            }

            ArrayList<Character> newDest = new ArrayList<>();
            newDest.addAll(movingStack);
            newDest.addAll(setup.get(destination));

            setup.get(destination).clear();
            setup.get(destination).addAll(newDest);

            print(setup);
        }

        String topCrates = "";

        for (int i = 1; i <= setup.size(); i++) {
            if (setup.get(i).size() > 0)
                topCrates += setup.get(i).get(0);
        }

        print(topCrates);

        /*
         * [D]
         * [N] [C]
         * [Z] [M] [P]
         * 1 2 3
         * 
         * move 1 from 2 to 1
         * move 3 from 1 to 3
         * move 2 from 2 to 1
         * move 1 from 1 to 2
         */

        // advent of code 2022 day 5 part 2

    }
}