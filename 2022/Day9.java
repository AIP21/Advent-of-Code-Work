import java.beans.DesignMode;
import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeMap;

import utils.Vec2;

public class Day9 extends AbstractDay {
    public static void main(String[] args) {
        new Day9();
    }

    public Day9() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        Vec2 head = new Vec2(0, 0);
        Vec2 tail = new Vec2(0, 0);

        ArrayList<String> visited = new ArrayList<>();

        for (String step : lines) {
            String[] split = step.split(" ");

            int steps = Integer.parseInt(split[1]);

            for (int i = 0; i < steps; i++) {
                switch (split[0]) {
                    case "U":
                        head.y++;
                        break;
                    case "D":
                        head.y--;
                        break;
                    case "L":
                        head.x--;
                        break;
                    case "R":
                        head.x++;
                        break;
                }

                if (Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1) {
                    if (head.x == tail.x) {
                        if (head.y > tail.y) {
                            tail.y++;
                        } else {
                            tail.y--;
                        }
                    } else if (head.y == tail.y) {
                        if (head.x > tail.x) {
                            tail.x++;
                        } else {
                            tail.x--;
                        }
                    } else {
                        if (head.x > tail.x) {
                            tail.x++;
                        } else {
                            tail.x--;
                        }

                        if (head.y > tail.y) {
                            tail.y++;
                        } else {
                            tail.y--;
                        }
                    }
                }

                if (!visited.contains(tail.toString()))
                    visited.add(tail.toString());

                print(split[0] + " - head: " + head + " tail: " + tail);
            }
        }

        print("");
        print("FINAL: ");
        print("head: " + head);
        print("tail: " + tail);
        print(visited.size());
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        Vec2 head = new Vec2(0, 0);
        Vec2[] tails = new Vec2[9];
        for (int i = 0; i < tails.length; i++) {
            tails[i] = new Vec2(0, 0);
        }

        printGrid(head, tails, "start");

        ArrayList<String> visited = new ArrayList<>();

        for (String step : lines) {
            String[] split = step.split(" ");

            int steps = Integer.parseInt(split[1]);

            for (int i = 0; i < steps; i++) {
                switch (split[0]) {
                    case "U":
                        head.y++;
                        break;
                    case "D":
                        head.y--;
                        break;
                    case "L":
                        head.x--;
                        break;
                    case "R":
                        head.x++;
                        break;
                }

                Vec2 subTail = new Vec2(head.x, head.y);
                for (int e = 0; e < tails.length; e++) {
                    tails[e] = follow(subTail, tails[e]);
                    subTail = tails[e];
                }

                /*
                 * head : (1, 0)
                 * tail 0 : (0, 0)
                 * follow(head, tail 0) : (0, 0)
                 * tail 1 : (0, 0)
                 * follow(tail 0, tail 1) : (0, 0)
                 */

                if (!visited.contains(tails[tails.length - 1].toString()))
                    visited.add(tails[tails.length - 1].toString());
            }

            // printGrid(head, tails, step);
        }

        printGrid(head, tails, "end");

        print("");
        print("FINAL: ");
        print("head: " + head);
        print("tail: " + tails);
        print(visited.size());
    }

    void printGrid(Vec2 head, Vec2[] tails, String line) {
        for (int y = 21; y > -20; y--) {
            for (int x = -20; x < 26; x++) {
                Vec2 pos = new Vec2(x, y);

                if (pos.equals(head)) {
                    print("H", false);
                } else {
                    boolean found = false;
                    for (int i = 0; i < tails.length; i++) {
                        if (pos.equals(tails[i])) {
                            print(i + 1, false);
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                        print(".", false);
                }
            }

            print("");
        }
        print("\n-------- " + line + " ------------\n");
    }

    private Vec2 follow(Vec2 head, Vec2 toMove) {
        Vec2 tail = new Vec2(toMove.x, toMove.y);

        if (Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1) {
            if (head.x == tail.x) {
                if (head.y > tail.y) {
                    tail.y++;
                } else {
                    tail.y--;
                }
            } else if (head.y == tail.y) {
                if (head.x > tail.x) {
                    tail.x++;
                } else {
                    tail.x--;
                }
            } else {
                if (head.x > tail.x) {
                    tail.x++;
                } else {
                    tail.x--;
                }

                if (head.y > tail.y) {
                    tail.y++;
                } else {
                    tail.y--;
                }
            }
        }

        return tail;
    }
}