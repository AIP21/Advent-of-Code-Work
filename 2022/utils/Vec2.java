package utils;

public class Vec2 {
    public int x;
    public int y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec2) {
            Vec2 other = (Vec2) obj;

            return other.x == x && other.y == y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return x * 1000 + y;
    }

    public static Vec2 parse(String str) {
        String[] split = str.replace("(", "").replace(")", "").replace(" ", "").split(",");
        return new Vec2(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    public float sqrDistTo(Vec2 other) {
        return (float) (Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public static float sqrDist(Vec2 a, Vec2 b) {
        return a.sqrDistTo(b);
    }

    public float distTo(Vec2 other) {
        return (float) Math.sqrt(sqrDistTo(other));
    }

    public static float dist(Vec2 a, Vec2 b) {
        return a.distTo(b);
    }

    public int mahattanDistTo(Vec2 o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y);
    }

    public static int mahattanDist(Vec2 a, Vec2 b) {
        return a.mahattanDistTo(b);
    }
}