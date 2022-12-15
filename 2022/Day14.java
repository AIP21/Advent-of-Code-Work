import java.beans.DesignMode;
import java.beans.Visibility;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import utils.BigInt;
import utils.Pair;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 extends AbstractDay {
    public static void main(String[] args) {
        new Day14();
    }

    public Day14() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Vec2>> rockPaths = new ArrayList<>();
        ArrayList<ArrayList<Tile>> grid = new ArrayList<>();

        int minWidth = Integer.MAX_VALUE, maxWidth = 0;
        int minSimWidth = 0, maxSimWidth = 0;
        int maxHeight = 0;

        for (String line : lines) {
            log(line);

            String[] split = line.split(" -> ");

            ArrayList<Vec2> path = new ArrayList<>();

            Vec2 lastPt = null;

            for (String point : split) {
                Vec2 pt = Vec2.parse(point);

                if (lastPt == null) {
                    lastPt = pt;
                } else {
                    int startX = Math.min(lastPt.x, pt.x);
                    int startY = Math.min(lastPt.y, pt.y);
                    int endX = Math.max(lastPt.x, pt.x);
                    int endY = Math.max(lastPt.y, pt.y);

                    if (startX == endX) {
                        for (int y = startY; y <= endY; y++) {
                            path.add(new Vec2(startX, y));
                        }
                    } else {
                        for (int x = startX; x <= endX; x++) {
                            path.add(new Vec2(x, startY));
                        }
                    }

                    lastPt = pt;
                }

                if (maxHeight < pt.y) {
                    maxHeight = pt.y;
                }
                if (maxWidth < pt.x) {
                    maxWidth = pt.x;
                }
                if (minWidth > pt.x) {
                    minWidth = pt.x;
                }

                path.add(pt);
            }

            rockPaths.add(path);
        }

        Source source = null;

        for (int y = 0; y <= maxHeight + 1; y++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int x = minWidth - 1; x <= maxWidth + 1; x++) {
                Vec2 posOrig = new Vec2(x, y);
                Vec2 pos = new Vec2(row.size(), y);

                boolean added = false;
                for (ArrayList<Vec2> path : rockPaths) {
                    if (path.contains(posOrig)) {
                        row.add(new Rock(pos));
                        added = true;
                        break;
                    }
                }

                if (!added) {
                    if (y == 0 && x == 500) {
                        source = new Source(pos);
                        row.add(source);
                    } else {
                        row.add(new Tile(pos));
                    }
                }
            }
            grid.add(row);
        }

        maxSimWidth = grid.get(0).size() - 1;

        print("Max height: " + maxHeight);
        print("Min width: " + minWidth + ", Max width: " + maxWidth);
        print("Min sim width: " + minSimWidth + ", Max sim width: " + maxSimWidth);

        ArrayList<Sand> sands = new ArrayList<>();
        Sand currentSand = null;
        ArrayList<Sand> inVoid = new ArrayList<>();

        log("Before sand:");
        logGrid(grid, sands);

        int steps = 0;
        int atRestCount = 0;
        while (inVoid.size() == 0 && steps < 1000000) {
            if ((currentSand == null || currentSand.atRest) && inVoid.size() == 0) {
                currentSand = new Sand(source.pos);
                sands.add(currentSand);
            }

            currentSand.simulate(grid, sands, maxHeight, maxSimWidth);

            if (currentSand.atRest) {
                atRestCount++;
            }

            if (currentSand.pos.y >= maxHeight) {
                inVoid.add(currentSand);
            }

            if (inVoid.size() != 0) {
                print("Entered the void with " + atRestCount + " at rest");
                break;
            }

            if (steps % 500 == 0) {
                log("Step " + steps + ", sand: " + sands.size() + ":");
                logGrid(grid, sands);
                log("");
                log("------------------------------------------");
                log("");
            }

            steps++;
        }

        print("FINAL");
        logGrid(grid, sands);
    }

    private void logGrid(ArrayList<ArrayList<Tile>> grid, ArrayList<Sand> sands) {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                boolean isSand = false;

                for (Sand sand : sands) {
                    if (sand.pos.equals(grid.get(y).get(x).pos)) {
                        log(sand.toString(), false);
                        isSand = true;
                        break;
                    }
                }

                if (!isSand) {
                    log(grid.get(y).get(x).toString(), false);
                }
            }
            log("");
        }
    }

    private void logGrid(ArrayList<ArrayList<Tile>> grid, HashMap<Vec2, Sand> sands) {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                if (sands.containsKey(grid.get(y).get(x).pos)) {
                    log(sands.get(grid.get(y).get(x).pos).toString(), false);
                } else {
                    log(grid.get(y).get(x).toString(), false);
                }
            }
            log("");
        }
    }

    private void printGrid(ArrayList<ArrayList<Tile>> grid, HashMap<Vec2, Sand> sands) {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                if (sands.containsKey(grid.get(y).get(x).pos)) {
                    print(sands.get(grid.get(y).get(x).pos).toString(), false);
                } else {
                    print(grid.get(y).get(x).toString(), false);
                }
            }
            print("");
        }
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Vec2>> rockPaths = new ArrayList<>();
        ArrayList<ArrayList<Tile>> grid = new ArrayList<>();

        int minWidth = Integer.MAX_VALUE, maxWidth = 0;
        int minSimWidth = 0, maxSimWidth = 0;
        int maxHeight = 0;

        for (String line : lines) {
            log(line);

            String[] split = line.split(" -> ");

            ArrayList<Vec2> path = new ArrayList<>();

            Vec2 lastPt = null;

            for (String point : split) {
                Vec2 pt = Vec2.parse(point);

                if (lastPt == null) {
                    lastPt = pt;
                } else {
                    int startX = Math.min(lastPt.x, pt.x);
                    int startY = Math.min(lastPt.y, pt.y);
                    int endX = Math.max(lastPt.x, pt.x);
                    int endY = Math.max(lastPt.y, pt.y);

                    if (startX == endX) {
                        for (int y = startY; y <= endY; y++) {
                            path.add(new Vec2(startX, y));
                        }
                    } else {
                        for (int x = startX; x <= endX; x++) {
                            path.add(new Vec2(x, startY));
                        }
                    }

                    lastPt = pt;
                }

                if (maxHeight < pt.y) {
                    maxHeight = pt.y;
                }
                if (maxWidth < pt.x) {
                    maxWidth = pt.x;
                }
                if (minWidth > pt.x) {
                    minWidth = pt.x;
                }

                path.add(pt);
            }

            rockPaths.add(path);
        }

        Source source = null;

        for (int y = 0; y <= maxHeight + 3; y++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int x = minWidth - 1; x <= maxWidth + 1; x++) {
                Vec2 posOrig = new Vec2(x, y);
                Vec2 pos = new Vec2(row.size(), y);

                boolean added = false;
                for (ArrayList<Vec2> path : rockPaths) {
                    if (path.contains(posOrig)) {
                        row.add(new Rock(pos));
                        added = true;
                        break;
                    }
                }

                if (!added) {
                    if (y == 0 && x == 500) {
                        source = new Source(pos);
                        row.add(source);
                    } else {
                        row.add(new Tile(pos));
                    }
                }
            }
            grid.add(row);
        }

        maxSimWidth = grid.get(0).size() - 1;

        print("Max height: " + maxHeight);
        print("Min width: " + minWidth + ", Max width: " + maxWidth);
        print("Min sim width: " + minSimWidth + ", Max sim width: " + maxSimWidth);

        HashMap<Vec2, Sand> sands = new HashMap<>();
        Sand currentSand = null;

        boolean sourceCovered = false;

        log("Before sand:");
        logGrid(grid, sands);
        log("");

        int steps = 0;
        int atRestCount = 0;
        while (!sourceCovered && steps < 10000000) {
            if (currentSand == null || currentSand.atRest) {
                currentSand = new Sand(source.pos);
                sands.put(currentSand.pos, currentSand);
            }

            currentSand.simulatePart2(grid, sands, maxHeight, maxSimWidth);

            if (currentSand.atRest) {
                atRestCount++;
            }

            if (currentSand.pos.x == source.pos.x && currentSand.pos.y == source.pos.y) {
                print("Source covered with " + atRestCount + " at rest");
                sourceCovered = true;
            }

            if (steps % 10000 == 0) {
                log("Step " + steps);
                logGrid(grid, sands);
                log("");
                log("------------------------------------------");
                log("");
                print("Step: " + steps);
            }

            steps++;
        }

        print("FINAL");
        printGrid(grid, sands);
        print("At rest: " + atRestCount);
    }

    private class Tile {
        public Vec2 pos;

        public Tile(Vec2 pos) {
            this.pos = new Vec2(pos.x, pos.y);
        }

        @Override
        public String toString() {
            return ".";
        }
    }

    private class Source extends Tile {
        public Source(Vec2 pos) {
            super(pos);
        }

        @Override
        public String toString() {
            return "+";
        }
    }

    private class Rock extends Tile {
        public Rock(Vec2 pos) {
            super(pos);
        }

        @Override
        public String toString() {
            return "#";
        }
    }

    private class Sand extends Tile {
        public boolean atRest = false;

        public Sand(Vec2 pos) {
            super(pos);
        }

        @Override
        public String toString() {
            return atRest ? "o" : "O";
        }

        public void simulate(ArrayList<ArrayList<Tile>> grid, ArrayList<Sand> sands, int maxHeight, int maxWidth) {
            if (pos.y + 1 <= maxHeight) {
                boolean belowEmpty = !(grid.get(pos.y + 1).get(pos.x) instanceof Rock)
                        && sands.stream().noneMatch(sand -> sand.pos.equals(new Vec2(pos.x, pos.y + 1)));
                if (belowEmpty) {
                    pos.y++;
                    atRest = false;
                    return;
                }

                if (pos.x - 1 >= 0) {
                    boolean belowLeftEmpty = !(grid.get(pos.y + 1).get(pos.x - 1) instanceof Rock)
                            && sands.stream().noneMatch(sand -> sand.pos.equals(new Vec2(pos.x - 1, pos.y + 1)));
                    if (belowLeftEmpty) {
                        pos.y++;
                        pos.x--;
                        atRest = false;
                        return;
                    }
                }

                if (pos.x + 1 <= maxWidth) {
                    boolean belowRightEmpty = !(grid.get(pos.y + 1).get(pos.x + 1) instanceof Rock)
                            && sands.stream().noneMatch(sand -> sand.pos.equals(new Vec2(pos.x + 1, pos.y + 1)));
                    if (belowRightEmpty) {
                        pos.y++;
                        pos.x++;
                        atRest = false;
                        return;
                    }
                }

                atRest = true;
                return;
            }

            atRest = true;
        }

        public void simulatePart2(ArrayList<ArrayList<Tile>> grid, HashMap<Vec2, Sand> sands, int maxHeight,
                int maxWidth) {
            if (pos.y + 1 <= maxHeight + 1) {
                boolean belowEmpty = (pos.x < maxWidth && pos.x >= 0 && pos.y + 1 <= maxHeight)
                        ? !(grid.get(pos.y + 1).get(pos.x) instanceof Rock)
                        : true;
                if (belowEmpty && !(sands.containsKey(new Vec2(pos.x, pos.y + 1)))) {
                    sands.remove(pos);
                    pos.y++;
                    sands.put(pos, this);
                    atRest = false;
                    return;
                }

                boolean belowLeftEmpty = (pos.x - 1 < maxWidth && pos.x - 1 >= 0)
                        ? !(grid.get(pos.y + 1).get(pos.x - 1) instanceof Rock)
                        : true;
                if (belowLeftEmpty && !(sands.containsKey(new Vec2(pos.x - 1, pos.y + 1)))) {
                    sands.remove(pos);
                    pos.y++;
                    pos.x--;
                    sands.put(pos, this);
                    atRest = false;
                    return;
                }

                boolean belowRightEmpty = (pos.x + 1 < maxWidth && pos.x + 1 >= 0)
                        ? !(grid.get(pos.y + 1).get(pos.x + 1) instanceof Rock)
                        : true;
                if (belowRightEmpty && !(sands.containsKey(new Vec2(pos.x + 1, pos.y + 1)))) {
                    sands.remove(pos);
                    pos.y++;
                    pos.x++;
                    sands.put(pos, this);
                    atRest = false;
                    return;
                }

                atRest = true;
                return;
            }

            atRest = true;
        }
    }
}