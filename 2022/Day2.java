import java.util.ArrayList;

import utils.InputUtils;

public class Day2 {
    private InputUtils inputUtils;

    public static void main(String[] args) {
        new Day2();
    }

    public Day2() {
        inputUtils = new InputUtils(System.getProperty("user.dir") + "/inputs/"
                + (this.getClass().toString().replace("class ", "")) + ".txt");

        part1();

        part2();
    }

    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        int totalScore = 0;

        for (String line : lines) {
            String[] split = line.split(" ");
            String l = split[0];
            String r = split[1];

            int roundScore = 0;
            if (l.contains("A")) { // rock
                if (r.contains("X")) { // rock
                    roundScore = 1;
                    roundScore += 3;
                } else if (r.contains("Y")) { // paper
                    roundScore = 2;
                    roundScore += 6;
                } else if (r.contains("Z")) { // scissors
                    roundScore = 3;
                    roundScore += 0;
                }
            } else if (l.contains("B")) { // paper
                if (r.contains("X")) { // rock
                    roundScore = 1;
                    roundScore += 0;
                } else if (r.contains("Y")) { // paper
                    roundScore = 2;
                    roundScore += 3;
                } else if (r.contains("Z")) { // scissors
                    roundScore = 3;
                    roundScore += 6;
                }
            } else if (l.contains("C")) { // scissor
                if (r.contains("X")) { // rock
                    roundScore = 1;
                    roundScore += 6;
                } else if (r.contains("Y")) { // paper
                    roundScore = 2;
                    roundScore += 0;
                } else if (r.contains("Z")) { // scissors
                    roundScore = 3;
                    roundScore += 3;
                }
            }

            totalScore += roundScore;
        }

        System.out.println(totalScore);
    }

    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        int totalScore = 0;

        for (String line : lines) {
            String[] split = line.split(" ");
            String l = split[0];
            String r = split[1];

            int roundScore = 0;
            if (l.contains("A")) { // rock
                if (r.contains("X")) { // lose, scissor
                    roundScore = 3;
                    roundScore += 0;
                } else if (r.contains("Y")) { // draw, rock
                    roundScore = 1;
                    roundScore += 3;
                } else if (r.contains("Z")) { // win, paper
                    roundScore = 2;
                    roundScore += 6;
                }
            } else if (l.contains("B")) { // paper
                if (r.contains("X")) { // lose, rock
                    roundScore = 1;
                    roundScore += 0;
                } else if (r.contains("Y")) { // draw, paper
                    roundScore = 2;
                    roundScore += 3;
                } else if (r.contains("Z")) { // win, scissor
                    roundScore = 3;
                    roundScore += 6;
                }
            } else if (l.contains("C")) { // scissor
                if (r.contains("X")) { // lose, paper
                    roundScore = 2;
                    roundScore += 0;
                } else if (r.contains("Y")) { // draw, scissor
                    roundScore = 3;
                    roundScore += 3;
                } else if (r.contains("Z")) { // win, rock
                    roundScore = 1;
                    roundScore += 6;
                }
            }

            totalScore += roundScore;
        }

        System.out.println(totalScore);
    }
}