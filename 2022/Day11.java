import java.beans.DesignMode;
import java.beans.Visibility;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import utils.BigInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;

public class Day11 extends AbstractDay {
    private long lcm = 0;

    public static void main(String[] args) {
        new Day11();
    }

    public Day11() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<Monkey> monkeys = decodeMonkeys(lines);

        long rounds = 20;

        for (Monkey monke : monkeys) {
            monke.trueMonkey = monkeys.get(monke.trueMonkeyIndex);
            monke.falseMonkey = monkeys.get(monke.falseMonkeyIndex);

            lcm *= monke.divBy;
            print(monke);
        }

        print("");
        print("START ROUNDS");

        for (long round = 0; round < rounds; round++) {
            for (Monkey monke : monkeys) {
                monkeySeeMonkeyDo(monkeys, monke);
            }

            print("");
            print("ROUND " + round + ":");
            for (Monkey monke : monkeys) {
                print("Monkey " + monke.id + " has " + monke.items + " items");
            }
        }

        ArrayList<Long> counts = new ArrayList<>();
        for (Monkey monke : monkeys) {
            print("Monkey " + monke.id + " inspected " + monke.inspectionCount + " items");
            counts.add(monke.inspectionCount);
        }
        Collections.sort(counts);

        long top1 = counts.get(counts.size() - 1);
        long top2 = counts.get(counts.size() - 2);

        print("Monkey business: " + (top1 * top2));
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<Monkey> monkeys = decodeMonkeys(lines);

        int rounds = 10000;

        lcm = 1;
        for (Monkey monke : monkeys) {
            monke.trueMonkey = monkeys.get(monke.trueMonkeyIndex);
            monke.falseMonkey = monkeys.get(monke.falseMonkeyIndex);

            lcm *= monke.divBy;
            print(monke);
        }

        print("");
        print("START ROUNDS");

        for (int round = 0; round < rounds; round++) {
            // print("ROUND: " + round);
            for (Monkey monke : monkeys) {
                monkeySeeMonkeyDoPart2(monkeys, monke);

                // for (Monkey monkey : monkeys) {
                // print("After " + monke.id + ", " + monkey.id + " has items: " +
                // monkey.items);
                // }
            }
        }

        ArrayList<Long> counts = new ArrayList<>();
        for (Monkey monke : monkeys) {
            print("Monkey " + monke.id + " inspected " + monke.inspectionCount + " items");
            counts.add(monke.inspectionCount);
        }
        Collections.sort(counts);

        long top1 = counts.get(counts.size() - 1);
        long top2 = counts.get(counts.size() - 2);

        print("Monkey business: " + (top1 * top2));
    }

    private void monkeySeeMonkeyDo(ArrayList<Monkey> monkeys, Monkey monke) {
        for (Long item : monke.items) {
            if (monke.opMult) {
                if (monke.opItself) {
                    item *= item;
                } else {
                    item *= monke.opValue;
                }
            } else {
                if (monke.opItself) {
                    item += item;
                } else {
                    item += monke.opValue;
                }
            }

            monke.inspectionCount++;

            item = (long) Math.floor(item / 3.0);

            item = item % lcm;

            if (item % monke.divBy == 0) {
                monke.trueMonkey.items.add(item);
                // print(" IS div by " + monke.divBy);
                // print(" " + item + " thrown to " + monke.trueMonkey);
            } else {
                monke.falseMonkey.items.add(item);
                // print(" NOT div by " + monke.divBy);
                // print(" " + item + " thrown to " + monke.falseMonkey);
            }
        }

        monke.items.clear();
    }

    private void monkeySeeMonkeyDoPart2(ArrayList<Monkey> monkeys, Monkey monke) {
        // print("Monkey " + monke.id + ":");
        for (Long item : monke.items) {
            if (monke.opMult) {
                if (monke.opItself) {
                    item *= item;
                } else {
                    item *= monke.opValue;
                }
            } else {
                if (monke.opItself) {
                    item += item;
                } else {
                    item += monke.opValue;
                }
            }

            monke.inspectionCount++;

            // print(" " + monke.id + " inspects " + item);
            // print(" Worry level " + (monke.opMult ? "* " : "+ ") + (monke.opItself ?
            // "itself" : monke.opValue)
            // + " to "
            // + worry);

            item = item % lcm;

            if (item % monke.divBy == 0) {
                monke.trueMonkey.items.add(item);
                // print(" IS div by " + monke.divBy);
                // print(" " + item + " thrown to " + monke.trueMonkey);
            } else {
                monke.falseMonkey.items.add(item);
                // print(" NOT div by " + monke.divBy);
                // print(" " + item + " thrown to " + monke.falseMonkey);
            }
        }

        monke.items.clear();
    }

    private ArrayList<Monkey> decodeMonkeys(ArrayList<String> lines) {
        ArrayList<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 7) {
            ArrayList<Long> startingItems = new ArrayList<>();
            boolean opMult = false;
            boolean opItself = false;
            long opValue = 1;
            long divBy = 0;
            int trueMonkey = 0;
            int falseMonkey = 0;

            for (int j = 1; j < 7; j++) {
                if (i + j < lines.size()) {
                    switch (j) {
                        case 1:
                            String listLine = lines.get(i + j).replace("  Starting items: ", "");
                            String[] list = listLine.split(", ");
                            for (String s : list) {
                                startingItems.add(Long.parseLong(s));
                            }
                            break;
                        case 2:
                            String opLine = lines.get(i + j).replace("  Operation: new = old ", "");
                            String[] parts = opLine.split(" ");
                            if (parts[0].contains("*")) {
                                opMult = true;
                            } else if (parts[0].contains("+")) {
                                opMult = false;
                            }

                            if (parts[1].contains("old")) {
                                opItself = true;
                            } else {
                                opItself = false;
                                opValue = Long.parseLong(parts[1]);
                            }
                            break;
                        case 3:
                            String divLine = lines.get(i + j).replace("  Test: divisible by ", "");
                            divBy = Long.parseLong(divLine);
                            break;
                        case 4:
                            String trueLine = lines.get(i + j).replace("    If true: throw to monkey ", "");
                            trueMonkey = Integer.parseInt(trueLine);
                            break;
                        case 5:
                            String falseLine = lines.get(i + j).replace("    If false: throw to monkey ", "");
                            falseMonkey = Integer.parseInt(falseLine);
                            break;
                    }
                }
            }

            monkeys.add(new Monkey(i / 7, startingItems, opMult, opItself, opValue, divBy, trueMonkey, falseMonkey));
        }

        return monkeys;
    }
}

class Monkey {
    int id;

    ArrayList<Long> items;
    boolean opMult;
    boolean opItself;
    long opValue;
    long divBy;
    Monkey trueMonkey;
    Monkey falseMonkey;
    int trueMonkeyIndex;
    int falseMonkeyIndex;

    long inspectionCount = 0;

    public Monkey(int id, ArrayList<Long> startingItems, boolean opMult, boolean opItself, long opValue,
            long divBy,
            int trueMonkeyIndex, int falseMonkeyIndex) {
        this.id = id;
        this.items = startingItems;
        this.opMult = opMult;
        this.opItself = opItself;
        this.opValue = opValue;
        this.divBy = divBy;
        this.trueMonkeyIndex = trueMonkeyIndex;
        this.falseMonkeyIndex = falseMonkeyIndex;
    }

    @Override
    public String toString() {
        StringBuilder strB = new StringBuilder();
        strB.append(String.format("Monkey: %d", id));
        strB.append(String.format("  Items: %s\n", items));
        strB.append(String.format("  Operation: new = old %s %s\n", opMult ? "*" : "+",
                (opItself ? "old" : opValue)));
        strB.append(String.format("  Test: divisible by %d\n", divBy));
        strB.append(String.format("    If true: throw to monkey %d\n", trueMonkeyIndex));
        strB.append(String.format("    If false: throw to monkey %d\n", falseMonkeyIndex));
        return strB.toString();
    }
}