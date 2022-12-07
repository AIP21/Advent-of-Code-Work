import java.util.ArrayList;

public class Day4 extends AbstractDay {
    public static void main(String[] args) {
        new Day4();
    }

    public Day4() {
        super(false);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        int containedCount = 0;

        for (String str : lines) {
            String[] split = str.split(",");
            String[] a = split[0].split("-");
            String[] b = split[1].split("-");

            int aMin = Integer.parseInt(a[0]);
            int aMax = Integer.parseInt(a[1]);
            int bMin = Integer.parseInt(b[0]);
            int bMax = Integer.parseInt(b[1]);

            // check if a contains b
            if (aMin <= bMin && aMax >= bMax) {
                containedCount++;
            } else if (bMin <= aMin && bMax >= aMax) {
                containedCount++;
            }
        }

        print(containedCount);
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        int overlappingPairs = 0;

        for (String str : lines) {
            String[] split = str.split(",");
            String[] a = split[0].split("-");
            String[] b = split[1].split("-");

            int aMin = Integer.parseInt(a[0]);
            int aMax = Integer.parseInt(a[1]);
            int bMin = Integer.parseInt(b[0]);
            int bMax = Integer.parseInt(b[1]);

            // check if the range of a overlaps the range of b
            for (int i = aMin; i <= aMax; i++) {
                if (i >= bMin && i <= bMax) {
                    overlappingPairs++;
                    break;
                }
            }
        }

        print(overlappingPairs);
    }
}