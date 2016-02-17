package audio.paco.playground.core.model;

import static com.google.common.base.Preconditions.checkArgument;

public class Coordinate {
    public final int x, y;

    public Coordinate(int x, int y) {
        checkArgument(x >= 0 && y >= 0, "Both x and y must be positive.");
        this.x = x;
        this.y = y;
    }

    public boolean equals(Coordinate other) {
        return other.x == x && other.y == y;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Coordinate) && equals((Coordinate)other);
    }

    @Override public int hashCode() { return x ^ y; }
    @Override public String toString() { return "(" + x + ", " + y + ")"; }
}
