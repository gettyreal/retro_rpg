package tile;

import java.util.Objects;

//coordinate point class
//used in tileManager to get tile positioning on the map
//NOTE = X and Y are column and row indexes, not absolute positioning
public class Point {
    public int x, y; // tile coordinates

    //costructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Override equals to use and key in the HashMap
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    // Override hashCode to use and key in the HashMap
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
