import utils.DFS;
import utils.FloydWarshall;
import utils.Pair;
import utils.Vec2;

import java.lang.foreign.VaList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import javax.management.ValueExp;

public class Day16 extends AbstractDay {
    final static int INF = 99999, V = 4;

    public static void main(String[] args) {
        new Day16();
    }

    public Day16() {
        super(false);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        HashMap<String, Valve> valves = new HashMap<>();
        Set<Valve> validValves = new HashSet<>();
        Valve startValve = null;

        for (String line : lines) {
            String lin = line.replace("Valve ", "").replace(" has flow rate=", ";")
                    .replace("; tunnels lead to valves ", ";").replace("; tunnel leads to valve ", ";");

            String[] split = lin.split(";");

            Valve valve = new Valve(split[0], Integer.parseInt(split[1]), split[2].split(", "));
            valves.put(split[0], valve);

            if (split[0].equals("AA")) {
                startValve = valve;
            }

            if (valve.flowRate > 0) {
                validValves.add(valve);
            }
        }

        for (Valve valve : valves.values()) {
            valve.tunnels = new ArrayList<>();
            for (String s : valve.cons) {
                valve.tunnels.add(valves.get(s));
            }
        }

        // For each valid (flow > 0) valve (including the start), store the length of
        // the paths from it to every other valid valve
        for (Valve valve : valves.values()) {
            valve.findPaths(validValves);
        }

        startValve = valves.get("AA");
        startValve.findPaths(validValves);

        print("Start: " + startValve.leadsTo);
        for (Valve valve : validValves) {
            print(valve.id + " - " + valve.leadsTo);
        }

        // BFS
        List<State> bfsStack = new LinkedList<>();
        bfsStack.add(new State(startValve, 30, 0, new ArrayList<>()));

        int best = 0;

        while (!bfsStack.isEmpty()) {
            State state = bfsStack.remove(0);
            boolean ended = true;

            for (Entry<Valve, Integer> path : state.cur.leadsTo.entrySet()) {
                int distance = path.getValue();
                Valve target = path.getKey();

                if (state.minutes - distance > 0 && !state.alreadyOpened.contains(target)) {
                    ArrayList<Valve> opened = new ArrayList<>(state.alreadyOpened);
                    opened.add(target);

                    int newlyReleased = (state.minutes - distance) * target.flowRate;
                    ended = false;
                    log("State: " + state + "; Path to: " + target + "; Distance: " + distance
                            + "; Opening: " + newlyReleased);
                    bfsStack.add(new State(target, state.minutes - distance, state.released + newlyReleased, opened));
                }
            }

            if (ended) {
                if (state.released > best) {
                    print("BEST STATE: " + state);
                    best = state.released;
                }
            }
        }
        /*
         * System.out.println("part 1: " + best);
         * long mid = System.currentTimeMillis();
         * System.out.println((mid - begin) + " millis");
         * 
         * // part 2: same but different, state is more complex
         * Player startHuman = new Player(startValve, 26);
         * Player startElephant = new Player(startValve, 26);
         * List<StateP2> stack2 = new LinkedList<>();
         * stack2.add(new StateP2(startHuman, startElephant, 0, new ArrayList<>()));
         * best = 0;
         * while (!stack2.isEmpty()) {
         * StateP2 state = stack2.remove(0);
         * 
         * // always move the player that is behind in time, or the human if tied
         * boolean moveElephant = state.elephant.minutes > state.human.minutes;
         * boolean terminal = addMoves(stack2, state, moveElephant);
         * // if the mover cannot move, maybe the other Player still can
         * if (terminal) {
         * terminal = addMoves(stack2, state, !moveElephant);
         * }
         * 
         * if (terminal) {
         * if (state.released > best) {
         * // System.out.println("new best: " + state);
         * best = state.released;
         * }
         * }
         * }
         * System.out.println("part 2: " + best);
         * 
         * // finish
         * long end = System.currentTimeMillis();
         * System.out.println((end - mid) + " millis");
         */
    }

    record State(Valve cur, int minutes, int released, Collection<Valve> alreadyOpened) {
    }

    /*
     * private static boolean addMoves(List<StateP2> stack2, StateP2 state, boolean
     * moveElephant) {
     * boolean terminal = true;
     * Player mover = moveElephant ? state.elephant : state.human;
     * for (var way : mover.cur.ways.entrySet()) {
     * Integer dist = way.getValue();
     * Valve target = way.getKey();
     * if (mover.minutes - dist > 0 && !state.opened.contains(target)) {
     * ArrayList<Valve> opened = new ArrayList<>(state.opened);
     * opened.add(target);
     * int newlyReleased = (mover.minutes - dist) * target.flow;
     * terminal = false;
     * Player newPlayer = new Player(target, mover.minutes - dist);
     * // System.out.println("at " + state + (moveElephant ? " elephant" : " human")
     * +
     * // " using way " + target + "/" + dist + " freshly releasing " +
     * newlyReleased);
     * stack2.add(new StateP2(moveElephant ? state.human : newPlayer,
     * moveElephant ? newPlayer : state.elephant,
     * state.released + newlyReleased, opened));
     * }
     * }
     * return terminal;
     * }
     */

    class Valve {
        public String[] cons;
        public String id;
        public List<Valve> tunnels;
        public HashMap<Valve, Integer> leadsTo;
        public int flowRate;

        public Valve(String id, int flowRate, String[] cons) {
            this.id = id;
            this.flowRate = flowRate;
            this.cons = cons;
        }

        public void findPaths(Set<Valve> targets) {
            leadsTo = new HashMap<>();
            List<Valve> stack = new LinkedList<>();
            HashMap<Valve, Integer> distances = new HashMap<>();
            Set<Valve> visited = new HashSet<>();
            stack.add(this);
            distances.put(this, 0);
            while (!stack.isEmpty() && leadsTo.size() < targets.size()) {
                Valve cur = stack.remove(0);
                visited.add(cur);
                Integer curDist = distances.get(cur);
                if (cur != this && targets.contains(cur)) {
                    leadsTo.put(cur, curDist + 1); // the way takes curDist minutes; +1 minute for opening the valve
                }
                for (Valve v : cur.tunnels) {
                    if (!visited.contains(v)) {
                        stack.add(v);
                        distances.put(v, curDist + 1);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return id;
        }
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

    }
}