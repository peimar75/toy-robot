package se.hiq;

/**
 * Represents the table surface on which the robot moves.
 * The table has fixed dimensions and disallows any moves outside its boundaries.
 */
public class TableTop {

    private final int width;
    private final int height;

    public TableTop(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Checks whether the provided (x, y) coordinates lie within the table boundaries.
     *
     * @param x horizontal position
     * @param y vertical position
     * @return true if valid, false otherwise
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns the width of the table.
     *
     * @return table width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the table.
     *
     * @return table height
     */
    public int getHeight() {
        return height;
    }
}