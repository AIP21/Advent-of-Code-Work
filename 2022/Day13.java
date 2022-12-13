import java.beans.DesignMode;
import java.beans.Visibility;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import utils.BigInt;
import utils.Pair;
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

public class Day13 extends AbstractDay {
    public static void main(String[] args) {
        new Day13();
    }

    public Day13() {
        super(true);
    }

    @Override
    public void part1() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<Pair<Packet, Packet>> packetPairs = new ArrayList<>();

        int indexSum = 0;

        for (int lInd = 0; lInd < lines.size(); lInd += 3) {
            String a = lines.get(lInd);
            String b = lines.get(lInd + 1);

            Packet pa = parsePacket(a);
            Packet pb = parsePacket(b);
            packetPairs.add(new Pair<>(pa, pb));

            // print(pa + " : " + pb);

            int inOrder = checkOrder(pa, pb, 0);
            // print(inOrder);

            // print("");

            if (inOrder == 1) {
                indexSum += (lInd / 3) + 1;
            }
        }

        print("Index sum: " + indexSum);
    }

    // region
    private abstract class Packet {
        public Object val;

        public Packet(Object val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val.toString();
        }
    }

    private class IntPacket extends Packet {
        public IntPacket(int val) {
            super(val);
        }
    }

    private class ListPacket extends Packet {
        public ListPacket(ArrayList<Packet> packets) {
            super(packets);
        }

        @Override
        public String toString() {
            if (((ArrayList) val).size() == 0) {
                return "";
            }
            return val.toString();
        }
    }
    // endregion

    private Packet parsePacket(String in) {
        if (in.length() == 0) {
            return new ListPacket(new ArrayList<>());
        }

        // check if it's a list
        if (in.charAt(0) == '[') {
            // it's a list
            ArrayList<Packet> packets = new ArrayList<>();

            // remove the brackets
            in = in.substring(1, in.length() - 1);

            // split the string into parts
            ArrayList<String> parts = new ArrayList<>();
            int bracketCount = 0;
            int lastSplit = 0;
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);

                if (c == '[') {
                    bracketCount++;
                } else if (c == ']') {
                    bracketCount--;
                } else if (c == ',' && bracketCount == 0) {
                    parts.add(in.substring(lastSplit, i));
                    lastSplit = i + 1;
                }
            }

            // add the last part
            parts.add(in.substring(lastSplit));

            // parse the parts
            for (String part : parts) {
                packets.add(parsePacket(part));
            }

            return new ListPacket(packets);
        } else {
            // it's an int
            return new IntPacket(Integer.parseInt(in));
        }
    }

    private int checkOrder(Packet a, Packet b, int curVal) {
        if (a instanceof IntPacket && b instanceof IntPacket) {
            // print("Compare " + ((int) a.val) + " to " + ((int) b.val));

            int aVal = (int) a.val;
            int bVal = (int) b.val;

            if (aVal == bVal) {
                return 0;
            } else if (aVal < bVal) {
                return 1;
            } else if (aVal > bVal) {
                return -1;
            }
        } else if (a instanceof IntPacket && b instanceof ListPacket) {
            // print("Mixed types; convert left to [" + a + "] and retry comparison");
            return checkOrder(toListPacket((IntPacket) a), b, curVal);
        } else if (a instanceof ListPacket && b instanceof IntPacket) {
            // print("Mixed types; convert right to [" + ((int) b.val) + "] and retry
            // comparison");
            return checkOrder(a, toListPacket((IntPacket) b), curVal);
        } else if (a instanceof ListPacket && b instanceof ListPacket) {
            // print("Compare lists " + a + " to " + b);
            ArrayList<Packet> packetsA = (ArrayList<Packet>) a.val;
            ArrayList<Packet> packetsB = (ArrayList<Packet>) b.val;

            for (int i = 0; i < Math.max(packetsA.size(), packetsB.size()); i++) {
                if (i + 1 > packetsA.size()) {
                    // print("Left side ran out of items, correct order");
                    return 1;
                } else if (i + 1 > packetsB.size()) {
                    // print("Right side ran out of items, incorrect order");
                    return -1;
                }

                int val = checkOrder(packetsA.get(i), packetsB.get(i), curVal);
                if (val != 0) {
                    return val;
                }
            }
        }

        return curVal;
    }

    private ListPacket toListPacket(IntPacket toConvert) {
        ArrayList<Packet> packets = new ArrayList<>();
        packets.add(toConvert);
        return new ListPacket(packets);
    }

    @Override
    public void part2() {
        ArrayList<String> lines = inputUtils.getLines();

        ArrayList<Packet> packets = new ArrayList<>();

        for (int lInd = 0; lInd < lines.size(); lInd += 3) {
            String a = lines.get(lInd);
            String b = lines.get(lInd + 1);

            Packet pa = parsePacket(a);
            Packet pb = parsePacket(b);
            packets.add(pa);
            packets.add(pb);

            // print(pa + " : " + pb);
        }

        // ad divider packets
        Packet divA = new ListPacket(new ArrayList<>());
        Packet divAL = new ListPacket(new ArrayList<>());
        ((ArrayList) divAL.val).add(new IntPacket(2));
        ((ArrayList) divA.val).add(divAL);
        packets.add(divA);

        Packet divB = new ListPacket(new ArrayList<>());
        Packet divBL = new ListPacket(new ArrayList<>());
        ((ArrayList) divBL.val).add(new IntPacket(6));
        ((ArrayList) divB.val).add(divBL);
        packets.add(divB);

        Collections.sort(packets, new PacketComparator());
        Collections.reverse(packets);

        print("Sorted:");

        int divAIndex = 0;
        int divBIndex = 0;
        for (int i = 0; i < packets.size(); i++) {
            print(i + " --- " + packets.get(i));
            if (packets.get(i) == divA) {
                divAIndex = i + 1;
            } else if (packets.get(i) == divB) {
                divBIndex = i + 1;
            }
        }

        print("Divider A index: " + divAIndex);
        print("Divider B index: " + divBIndex);
        print("Decoder key: " + (divAIndex * divBIndex));
    }

    private class PacketComparator implements Comparator<Packet> {
        @Override
        public int compare(Packet o1, Packet o2) {
            return comparePackets(o1, o2, 0);
        }
    }

    public int comparePackets(Packet a, Packet b) {
        if (a instanceof IntPacket) {
            if (b instanceof IntPacket) {
                return Integer.compare((int) a.val, (int) b.val);
            } else if (b instanceof ListPacket) {
                if (((ArrayList<Packet>) b.val).size() == 0) {
                    return 1;
                }

                ArrayList<Packet> bL = (ArrayList) b.val;

                for (int i = 0; i < bL.size(); i++) {
                    int c = comparePackets(a, bL.get(i));
                    if (c != 0) {
                        return c;
                    }
                }
                return Integer.compare(1, bL.size());
            }
        } else if (a instanceof ListPacket) {
            if (((ArrayList<Packet>) a.val).size() == 0) {
                return -1;
            }

            if (b instanceof IntPacket) {
                ArrayList<Packet> aL = (ArrayList) a.val;

                for (int i = 0; i < aL.size(); i++) {
                    int c = comparePackets(aL.get(i), b);
                    if (c != 0) {
                        return c;
                    }
                }
                return Integer.compare(aL.size(), 1);
            } else if (b instanceof ListPacket) {
                if (((ArrayList<Packet>) b.val).size() == 0) {
                    return 1;
                }

                ArrayList<Packet> aL = (ArrayList) a.val;
                ArrayList<Packet> bL = (ArrayList) b.val;

                for (int i = 0; i < Math.min(aL.size(), bL.size()); i++) {
                    int c = comparePackets(aL.get(i), bL.get(i));
                    if (c != 0) {
                        return c;
                    }
                }
                return Integer.compare(aL.size(), bL.size());
            }
        }

        print("Error comparing " + a + " to " + b);
        return 0;
    }

    private int comparePackets(Packet a, Packet b, int curVal) {
        if (a instanceof IntPacket && b instanceof IntPacket) {
            // print("Compare " + ((int) a.val) + " to " + ((int) b.val));

            int aVal = (int) a.val;
            int bVal = (int) b.val;

            if (aVal == bVal) {
                return 0;
            } else if (aVal < bVal) {
                return 1;
            } else if (aVal > bVal) {
                return -1;
            }
        } else if (a instanceof IntPacket && b instanceof ListPacket) {
            // print("Mixed types; convert left to [" + a + "] and retry comparison");
            return comparePackets(toListPacket((IntPacket) a), b, curVal);
        } else if (a instanceof ListPacket && b instanceof IntPacket) {
            // print("Mixed types; convert right to [" + ((int) b.val) + "] and retry
            // comparison");
            return comparePackets(a, toListPacket((IntPacket) b), curVal);
        } else if (a instanceof ListPacket && b instanceof ListPacket) {
            // print("Compare lists " + a + " to " + b);
            ArrayList<Packet> packetsA = (ArrayList<Packet>) a.val;
            ArrayList<Packet> packetsB = (ArrayList<Packet>) b.val;

            for (int i = 0; i < Math.max(packetsA.size(), packetsB.size()); i++) {
                if (i + 1 > packetsA.size()) {
                    // print("Left side ran out of items, correct order");
                    return 1;
                } else if (i + 1 > packetsB.size()) {
                    // print("Right side ran out of items, incorrect order");
                    return -1;
                }

                int val = comparePackets(packetsA.get(i), packetsB.get(i), curVal);
                if (val != 0) {
                    return val;
                }
            }
        }

        return curVal;
    }
}