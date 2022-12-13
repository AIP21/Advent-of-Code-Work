import java.beans.DesignMode;
import java.beans.Visibility;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import utils.BigInt;
import utils.Utils;
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

public class Day12 extends AbstractDay {
    public static void main(String[] args) {
        new Day12();
    }

    public Day12() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Character>> grid = new ArrayList<>();

        Set<Elevation> cells = new HashSet<>();
        Map<String, Set<String>> connections = new HashMap<>();

        Graph<Elevation> graph;
        RouteFinder<Elevation> pathFinder;

        int startIndex = 0;
        int endIndex = 0;
        Elevation end = null;

        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Character> row = new ArrayList<>();
            for (char c : lines.get(i).toCharArray()) {
                if (c == 'S') {
                    startIndex = i * lines.get(0).length() + row.size();
                    row.add('a');
                } else if (c == 'E') {
                    endIndex = i * lines.get(0).length() + row.size();
                    row.add('z');
                } else {
                    row.add(c);
                }
            }
            grid.add(row);
        }

        printGrid(grid);

        print("");

        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                int index = Utils.indexFromCoord(x, y, grid.get(y).size());
                Vec2 coord = new Vec2(x, y);
                int elevation = charToInt(grid.get(y).get(x));

                Elevation elev = new Elevation("" + index, coord.toString(), coord, elevation);
                cells.add(elev);

                if (index == endIndex) {
                    end = elev;
                }

                ArrayList<String> cons = new ArrayList<>();

                if (x < grid.get(y).size() - 1) {
                    // print((charToInt(grid.get(y).get(x + 1)) - elevation) + ", RIGHT at: " +
                    // coord);
                    if (!(charToInt(grid.get(y).get(x + 1)) - elevation > 1)) {
                        int right = Utils.indexFromCoord(x + 1, y, grid.get(y).size());
                        cons.add("" + right);
                    }
                }
                if (x > 0) {
                    // print((charToInt(grid.get(y).get(x - 1)) - elevation) + ", LEFT at: " +
                    // coord);
                    if (!(charToInt(grid.get(y).get(x - 1)) - elevation > 1)) {
                        int left = Utils.indexFromCoord(x - 1, y, grid.get(y).size());
                        cons.add("" + left);
                    }
                }
                if (y < grid.size() - 1) {
                    // print((charToInt(grid.get(y + 1).get(x)) - elevation) + ", DOWN at: " +
                    // coord);
                    if (!(charToInt(grid.get(y + 1).get(x)) - elevation > 1)) {
                        int down = Utils.indexFromCoord(x, y + 1, grid.get(y).size());
                        cons.add("" + down);
                    }
                }
                if (y > 0) {
                    // print((charToInt(grid.get(y - 1).get(x)) - elevation) + ", UP at: " + coord);
                    if (!(charToInt(grid.get(y - 1).get(x)) - elevation > 1)) {
                        int up = Utils.indexFromCoord(x, y - 1, grid.get(y).size());
                        cons.add("" + up);
                    }
                }

                connections.put("" + index, new HashSet<String>(cons));
            }
        }

        // for (Map.Entry<String, Set<String>> entry : connections.entrySet()) {
        // print(entry.getKey() + " -> " + entry.getValue());
        // }

        graph = new Graph<>(cells, connections);
        pathFinder = new RouteFinder<>(graph, new HeightScorer(), new HeightScorer());

        List<Elevation> route = pathFinder.findRoute(graph.getNode("" + startIndex), graph.getNode("" + endIndex));

        if (route.size() > 0) {
            // route.stream().map(Elevation::getName).collect(Collectors.toList()).forEach(station
            // -> print(station));
            printRoute(route, grid, end);
            print("Route length: " + route.size());
        }
    }

    public void printGrid(ArrayList<ArrayList<Character>> grid) {
        for (ArrayList<Character> row : grid) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    // a-z = 0-25
    public int charToInt(char chr) {
        return chr - 'a';
    }

    public void printRoute(List<Elevation> route, ArrayList<ArrayList<Character>> grid, Elevation end) {
        // print the grid with the route as <, >, v, ^
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                Vec2 coord = new Vec2(x, y);

                if (route.stream().anyMatch(elevation1 -> elevation1.getCoord().equals(coord))) {
                    Elevation current = route.stream().filter(elevation1 -> elevation1.getCoord().equals(coord))
                            .findFirst().get();
                    int index = route.indexOf(current);
                    if (index > 0) {
                        Elevation prev = route.get(index - 1);
                        if (current == end) {
                            print("E", false);
                        } else if (prev.getCoord().x < coord.x) {
                            print(">", false);
                        } else if (prev.getCoord().x > coord.x) {
                            print("<", false);
                        } else if (prev.getCoord().y < coord.y) {
                            print("v", false);
                        } else if (prev.getCoord().y > coord.y) {
                            print("^", false);
                        }
                    } else {
                        print(".", false);
                    }
                } else {
                    print(".", false);
                }

            }
            print("");
        }
    }

    // Yes, I could technically search for the closest 'a' level cells around the
    // end point but I couldn't be bothered and this also works so...
    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<ArrayList<Character>> grid = new ArrayList<>();

        Set<Elevation> cells = new HashSet<>();
        Map<String, Set<String>> connections = new HashMap<>();

        ArrayList<Integer> startIndices = new ArrayList<>();

        int endIndex = 0;

        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Character> row = new ArrayList<>();
            for (char c : lines.get(i).toCharArray()) {
                if (c == 'S' || c == 'a') {
                    startIndices.add(i * lines.get(0).length() + row.size());
                    row.add('a');
                } else if (c == 'E') {
                    endIndex = i * lines.get(0).length() + row.size();
                    row.add('z');
                } else {
                    row.add(c);
                }
            }
            grid.add(row);
        }

        // printGrid(grid);

        print("Parsing done");

        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(y).size(); x++) {
                int index = Utils.indexFromCoord(x, y, grid.get(y).size());
                Vec2 coord = new Vec2(x, y);
                int elevation = charToInt(grid.get(y).get(x));

                Elevation elev = new Elevation("" + index, coord.toString(), coord, elevation);
                cells.add(elev);

                ArrayList<String> cons = new ArrayList<>();

                if (x < grid.get(y).size() - 1) {
                    // print((charToInt(grid.get(y).get(x + 1)) - elevation) + ", RIGHT at: " +
                    // coord);
                    if (!(charToInt(grid.get(y).get(x + 1)) - elevation > 1)) {
                        int right = Utils.indexFromCoord(x + 1, y, grid.get(y).size());
                        cons.add("" + right);
                    }
                }
                if (x > 0) {
                    // print((charToInt(grid.get(y).get(x - 1)) - elevation) + ", LEFT at: " +
                    // coord);
                    if (!(charToInt(grid.get(y).get(x - 1)) - elevation > 1)) {
                        int left = Utils.indexFromCoord(x - 1, y, grid.get(y).size());
                        cons.add("" + left);
                    }
                }
                if (y < grid.size() - 1) {
                    // print((charToInt(grid.get(y + 1).get(x)) - elevation) + ", DOWN at: " +
                    // coord);
                    if (!(charToInt(grid.get(y + 1).get(x)) - elevation > 1)) {
                        int down = Utils.indexFromCoord(x, y + 1, grid.get(y).size());
                        cons.add("" + down);
                    }
                }
                if (y > 0) {
                    // print((charToInt(grid.get(y - 1).get(x)) - elevation) + ", UP at: " + coord);
                    if (!(charToInt(grid.get(y - 1).get(x)) - elevation > 1)) {
                        int up = Utils.indexFromCoord(x, y - 1, grid.get(y).size());
                        cons.add("" + up);
                    }
                }

                connections.put("" + index, new HashSet<String>(cons));
            }
        }

        print("Griddng done");

        // for (Map.Entry<String, Set<String>> entry : connections.entrySet()) {
        // print(entry.getKey() + " -> " + entry.getValue());
        // }

        ArrayList<List<Elevation>> routes = new ArrayList<>();

        Graph<Elevation> graph = new Graph<>(cells, connections);
        RouteFinder<Elevation> pathfinder = new RouteFinder<>(graph, new HeightScorer(), new HeightScorer());

        ExecutorService pool = Executors.newFixedThreadPool(500);

        // Create a new thread to pathfind for every start point and add it to the
        // thread pool
        for (int i = 0; i < startIndices.size(); i++) {
            Runnable pathfindTask = new PathfindTask(pathfinder, graph, i, endIndex, startIndices, routes);

            // passes the Task objects to the pool to execute (Step 3)
            pool.execute(pathfindTask);
        }

        pool.shutdown();

        try {
            if (!pool.awaitTermination(60000, TimeUnit.SECONDS))
                System.err.println("Threads didn't finish in 60000 seconds!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        print("All threads finished, finding shorted route");

        // find and print shortest route
        int shortestRouteLength = Integer.MAX_VALUE;
        for (List<Elevation> route : routes) {
            if (route.size() < shortestRouteLength) {
                shortestRouteLength = route.size();
            }
        }

        print("Shortest route: " + (shortestRouteLength - 1) + " steps");
    }
}

class Elevation implements GraphNode {
    private final String id;
    private final String name;
    private final Vec2 coord;
    private final int height;

    public Elevation(String id, String name, Vec2 coord, int height) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.height = height;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public Vec2 getCoord() {
        return coord;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Elevation.class.getSimpleName() + "[", "]").add("id='" + id + "'")
                .add("name='" + name + "'").add("height=" + height).toString();
    }
}

class HeightScorer implements Scorer<Elevation> {
    @Override
    public double computeCost(Elevation from, Elevation to) {
        if (to.getHeight() - from.getHeight() > 1) { // 9 - 8 = 1
            return Integer.MAX_VALUE;
        } else {
            return 1;
        }
    }
}

class PathfindTask implements Runnable {
    private RouteFinder<Elevation> pathFinder;
    private Graph<Elevation> graph;

    private int index;
    private int endInd;
    private ArrayList<Integer> startIndices;

    public ArrayList<List<Elevation>> routes;

    public PathfindTask(RouteFinder<Elevation> pathFinder, Graph<Elevation> graph, int index, int endInd,
            ArrayList<Integer> startIndices, ArrayList<List<Elevation>> routes) {
        this.pathFinder = pathFinder;
        this.graph = graph;
        this.index = index;
        this.endInd = endInd;
        this.startIndices = startIndices;
        this.routes = routes;
    }

    // Prints task name and sleeps for 1s
    // This Whole process is repeated 5 times
    public void run() {
        try {
            routes.add(pathFinder.findRoute(graph.getNode("" + startIndices.get(index)), graph.getNode("" + endInd)));

            System.out.println(index);
        } catch (Exception e) {
        }
    }
}