import java.beans.DesignMode;
import java.beans.Visibility;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import utils.BigInt;
import utils.Utils;
import utils.Vec2;
import utils.aStar.Graph;
import utils.aStar.RouteFinder;
import utils.aStar.GraphNode;
import utils.aStar.Scorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 extends AbstractDay {
    private int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;

    public static void main(String[] args) {
        new Day15();
    }

    public Day15() {
        super(false);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        LinkedHashMap<Vec2, Vec2> sensors = new LinkedHashMap<>();

        for (String line : lines) {
            String lin = line.replace("Sensor at ", "").replace(" closest beacon is at ", "").replace("x=", "")
                    .replace("y=", "").replace(" ", "");

            String[] split = lin.split(":");
            Vec2 sensor = Vec2.parse(split[0]);
            Vec2 beacon = Vec2.parse(split[1]);

            sensors.put(sensor, beacon);

            if (sensor.x < minX)
                minX = sensor.x;
            if (sensor.y < minY)
                minY = sensor.y;

            if (beacon.x < minX)
                minX = beacon.x;
            if (beacon.y < minY)
                minY = beacon.y;

            if (sensor.x > maxX)
                maxX = sensor.x;
            if (sensor.y > maxY)
                maxY = sensor.y;

            if (beacon.x > maxX)
                maxX = beacon.x;
            if (beacon.y > maxY)
                maxY = beacon.y;
        }

        print(sensors);

        TreeMap<Integer, TreeMap<Integer, Tile>> grid = new TreeMap<>();

        // for (int y = minY; y <= maxY; y++) {
        // for (int x = minX; x <= maxX; x++) {
        // Vec2 pos = new Vec2(x, y);

        // if (sensors.containsKey(pos)) {
        // grid.putIfAbsent(y, new TreeMap<>());
        // grid.get(y).put(x, new Tile(true, false));

        // Vec2 bP = sensors.get(pos);
        // grid.putIfAbsent(bP.y, new TreeMap<>());
        // grid.get(bP.y).put(bP.x, new Tile(false, true));
        // } else {
        // grid.putIfAbsent(y, new TreeMap<>());
        // grid.get(y).put(x, new Tile(false, false));
        // }
        // }
        // }

        for (Vec2 sensor : sensors.keySet()) {
            Vec2 beacon = sensors.get(sensor);

            grid.putIfAbsent(sensor.y, new TreeMap<>());
            grid.get(sensor.y).put(sensor.x, new Tile(true, false));

            grid.putIfAbsent(beacon.y, new TreeMap<>());
            grid.get(beacon.y).put(beacon.x, new Tile(false, true));
        }

        // logGrid(grid);

        print("Sensor check:");

        int i = 0;
        for (Vec2 sensor : sensors.keySet()) {
            i++;
            Vec2 beacon = sensors.get(sensor);

            grid = bfs(grid, sensor, beacon);

            // logGrid(grid);
            print("sensor: " + i);
        }

        print("FINAL");
        // logGrid(grid);

        TreeMap<Integer, Tile> row = grid.get(10);
        int confirmedEmptyCount = 0;
        for (int x : row.keySet()) {
            if (row.get(x).confirmedEmpty && !row.get(x).isSensor && !row.get(x).isBeacon) {
                confirmedEmptyCount++;
            }
        }

        print("Confirmed empty: " + (confirmedEmptyCount - 2));
    }

    public void printGrid(TreeMap<Integer, TreeMap<Integer, Tile>> grid) {
        for (int y : grid.keySet()) {
            for (int x : grid.get(y).keySet()) {
                print(grid.get(y).get(x), false);
            }
            print(" " + y, false);
            print("");
        }
    }

    public void logGrid(TreeMap<Integer, TreeMap<Integer, Tile>> grid) {
        for (int y : grid.keySet()) {
            for (int x : grid.get(y).keySet()) {
                log(grid.get(y).get(x), false);
            }
            log(" " + y, false);
            log("");
        }
    }

    public TreeMap<Integer, TreeMap<Integer, Tile>> bfs(TreeMap<Integer, TreeMap<Integer, Tile>> grid, Vec2 pos,
            Vec2 target) {
        // reset all the tiles' checked status
        for (int y : grid.keySet()) {
            for (int x : grid.get(y).keySet()) {
                grid.get(y).get(x).checked = false;
            }
        }

        // Creating queue for bfs
        Queue<Vec2> obj = new LinkedList<>();

        // Pushing pos
        obj.add(pos);

        // Marking {x, y} as visited
        grid.get(pos.y).get(pos.x).checked = true;

        float distToTarget = pos.distTo(target);

        // Until queue is empty
        while (!obj.isEmpty()) {
            // Extracting front pair
            Vec2 coord = obj.peek();

            if (!grid.containsKey(coord.y)) {
                grid.putIfAbsent(coord.y, new TreeMap<>());

                for (int x = minX; x <= maxX; x++) {
                    grid.get(coord.y).putIfAbsent(x, new Tile(false, false));
                }
            }
            grid.get(coord.y).putIfAbsent(coord.x + 1, new Tile(false, false));
            grid.get(coord.y).putIfAbsent(coord.x - 1, new Tile(false, false));
            grid.get(coord.y).putIfAbsent(coord.x, new Tile(false, false));
            for (TreeMap<Integer, Tile> row : grid.values()) {
                row.putIfAbsent(coord.x + 1, new Tile(false, false));
                row.putIfAbsent(coord.x - 1, new Tile(false, false));
                row.putIfAbsent(coord.x, new Tile(false, false));
            }

            if (!grid.containsKey(coord.y + 1)) {
                grid.putIfAbsent(coord.y + 1, new TreeMap<>());

                for (int x = minX; x <= maxX; x++) {
                    grid.get(coord.y + 1).putIfAbsent(x, new Tile(false, false));
                }
            }
            if (!grid.containsKey(coord.y - 1)) {
                grid.putIfAbsent(coord.y - 1, new TreeMap<>());

                for (int x = minX; x <= maxX; x++) {
                    grid.get(coord.y - 1).putIfAbsent(x, new Tile(false, false));
                }
            }
            grid.get(coord.y + 1).putIfAbsent(coord.x, new Tile(false, false));
            grid.get(coord.y - 1).putIfAbsent(coord.x, new Tile(false, false));
            for (TreeMap<Integer, Tile> row : grid.values()) {
                row.putIfAbsent(coord.x, new Tile(false, false));
            }

            if (coord.distTo(pos) > distToTarget + 3) {
                break;
            }

            if (!grid.get(coord.y).get(coord.x).isSensor) {
                grid.get(coord.y).get(coord.x).confirmedEmpty = true;
            }

            // Popping front pair of queue
            obj.remove();

            if (!grid.get(coord.y).get(coord.x + 1).checked) {
                Vec2 p = new Vec2(coord.x + 1, coord.y);
                obj.add(p);
                grid.get(coord.y).get(coord.x + 1).checked = true;
            }

            if (!grid.get(coord.y).get(coord.x - 1).checked) {
                Vec2 p = new Vec2(coord.x - 1, coord.y);
                obj.add(p);
                grid.get(coord.y).get(coord.x - 1).checked = true;
            }

            if (!grid.get(coord.y + 1).get(coord.x).checked) {
                Vec2 p = new Vec2(coord.x, coord.y + 1);
                obj.add(p);
                grid.get(coord.y + 1).get(coord.x).checked = true;
            }

            if (!grid.get(coord.y - 1).get(coord.x).checked) {
                Vec2 p = new Vec2(coord.x, coord.y - 1);
                obj.add(p);
                grid.get(coord.y - 1).get(coord.x).checked = true;
            }
        }

        return grid;
    }

    private class Tile {
        public boolean checked = false;

        public boolean isSensor = false;
        public boolean isBeacon = false;

        public boolean confirmedEmpty = false;

        public Tile(boolean confirmedEmpty) {
            this.confirmedEmpty = confirmedEmpty;
        }

        public Tile(boolean isSensor, boolean isBeacon) {
            this.isSensor = isSensor;
            this.isBeacon = isBeacon;
        }

        @Override
        public String toString() {
            if (isSensor) {
                return "S";
            } else if (isBeacon) {
                return "B";
            } else if (confirmedEmpty) {
                return "#";
            } else if (checked) {
                return "0";
            } else {
                return ".";
            }
        }
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

    }
}