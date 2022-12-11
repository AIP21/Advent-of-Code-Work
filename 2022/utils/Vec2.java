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
}