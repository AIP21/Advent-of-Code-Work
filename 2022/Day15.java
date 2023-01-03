import utils.Vec2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Day15 extends AbstractDay {
    public static void main(String[] args) {
        new Day15();
    }

    public Day15() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        int row = 2000000;

        ArrayList<Vec2> sensors = new ArrayList<>();
        ArrayList<Vec2> beacons = new ArrayList<>();
        ArrayList<Integer> beaconsOnRow = new ArrayList<>();
        ArrayList<Integer> sensorsOnRow = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();

        for (String line : lines) {
            String lin = line.replace("Sensor at ", "").replace(" closest beacon is at ", "").replace("x=", "")
                    .replace("y=", "").replace(" ", "");

            print(lin);

            String[] split = lin.split(":");
            Vec2 sensor = Vec2.parse(split[0]);
            Vec2 beacon = Vec2.parse(split[1]);

            sensors.add(sensor);
            beacons.add(beacon);
            distances.add(Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y));

            if (beacon.y == row) {
                beaconsOnRow.add(beacon.x);
            }

            if (sensor.y == row) {
                sensorsOnRow.add(sensor.x);
            }
        }

        print("Sensors: " + sensors);
        print("Beacons: " + beacons);

        print("Checking...");

        int beaconsNotPossible = 0;

        for (int x = -5000000; x <= 5000000; x++) {
            if (!beaconsOnRow.contains(x) && !sensorsOnRow.contains(x)) {
                boolean possible = beaconPossible(x, row, sensors, distances);
                if (!possible) {
                    beaconsNotPossible++;
                    // log("#", false);
                } else {
                    // log(".", false);
                }
            }

            if (x % 1000000 == 0)
                print(x);
        }

        print("Beacons not possible: " + (beaconsNotPossible));
    }

    public boolean beaconPossible(int x, int y, ArrayList<Vec2> sensors, ArrayList<Integer> distances) {
        for (int i = 0; i < sensors.size(); i++) {
            if (Math.abs(x - sensors.get(i).x) + Math.abs(y - sensors.get(i).y) <= distances.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        HashMap<Vec2, Vec2> sensorsToBeacons = new HashMap<>();

        for (String line : lines) {
            String lin = line.replace("Sensor at ", "").replace(" closest beacon is at ", "").replace("x=", "")
                    .replace("y=", "").replace(" ", "");

            print(lin);

            String[] split = lin.split(":");
            Vec2 sensor = Vec2.parse(split[0]);
            Vec2 beacon = Vec2.parse(split[1]);

            sensorsToBeacons.put(sensor, beacon);
        }

        print("Sensors and their beacons: " + sensorsToBeacons);

        print("Checking...");

        // iterate over rows, calculating range
        // a good trick for large-iteration in AOC problems is to start from the end and
        // go back
        // the answer is often near the end in order to make brute-force attempts from
        // the start take as long as possible
        for (int y = 4000000; y > 0; y--) {
            ArrayList<Vec2> ranges = mergeRanges(noBeaconRanges(sensorsToBeacons, y));

            // if range has multiple ranges, we have a gap! now to find it
            if (ranges.size() > 1) {
                // locate first x coordinate that is outside all ranges
                int foundX = 0;

                xFind: for (int x = 0; x < 4000000; x++) {
                    for (Vec2 c : ranges) {
                        if (x >= c.x && x <= c.y) {
                            continue xFind;
                        }
                    }
                    foundX = x;
                    break;
                }

                print(Long.toString(foundX * 4000000L + y));
                break;
            }
        }

        print("DONE!");
    }

    public ArrayList<Vec2> noBeaconRanges(HashMap<Vec2, Vec2> sensorsAndBeacons, int y) {
        ArrayList<Vec2> ranges = new ArrayList<Vec2>();
        for (Vec2 sensor : sensorsAndBeacons.keySet()) {
            int dist = (int) sensor.mahattanDistTo(sensorsAndBeacons.get(sensor));

            int xRange = dist - Math.abs(sensor.y - y);

            if (xRange > 0) {
                ranges.add(new Vec2(sensor.x - xRange, sensor.x + xRange));
            }
        }
        return ranges;
    }

    // tries to remove overlaps in ranges and condense down to one coord
    public ArrayList<Vec2> mergeRanges(ArrayList<Vec2> ranges) {
        // make sure in proper order
        ranges.sort(new Comparator<Vec2>() {
            @Override
            public int compare(Vec2 o1, Vec2 o2) {
                return Integer.compare(o1.x, o2.x);
            }
        });

        // create return list, stick first coord in
        ArrayList<Vec2> newRanges = new ArrayList<Vec2>();
        newRanges.add(ranges.get(0));

        for (int i = 1; i < ranges.size(); i++) {
            Vec2 range = ranges.get(i);
            Vec2 end = newRanges.get(newRanges.size() - 1);

            // try to adjust first range's bounds to include overlap
            if (range.y >= end.x && range.y <= end.y) {
                newRanges.get(newRanges.size() - 1).x = Math.min(range.x, end.x);
            }
            if (range.x >= end.x && range.x <= end.y) {
                newRanges.get(newRanges.size() - 1).y = Math.max(range.y, end.y);
            }

            // if no overlap whatsoever, append to list
            if (!(range.x >= end.x && range.x <= end.y) && !(range.y >= end.x && range.y <= end.y)) {
                newRanges.add(0, range);
            }
        }
        return newRanges;
    }
}