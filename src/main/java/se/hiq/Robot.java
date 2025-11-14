package se.hiq;

/**
 * Represents the robot itself, including its position and facing direction.
 * Provides methods to move, rotate, and report its state.
 */
public class Robot {

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        /**
         * Returns the new direction after rotating 90° left.
         */
        public Direction turnLeft() {
            switch (this) {
                case NORTH: return WEST;
                case WEST:  return SOUTH;
                case SOUTH: return EAST;
                case EAST:  return NORTH;
                default:    throw new IllegalStateException("Invalid direction");
            }
        }

        /**
         * Returns the new direction after rotating 90° right.
         */
        public Direction turnRight() {
            switch (this) {
                case NORTH: return EAST;
                case EAST:  return SOUTH;
                case SOUTH: return WEST;
                case WEST:  return NORTH;
                default:    throw new IllegalStateException("Invalid direction");
            }
        }
    }

    private int x;
    private int y;
    private Direction facing;

    public Robot(int x, int y, Direction facing) {
        this.x = x;
        this.y = y;
        this.facing = facing;
    }

    /**
     * Attempts to move the robot one unit forward, provided the move is within bounds.
     */
    public void move(TableTop table) {
        int nextX = x;
        int nextY = y;

        switch (facing) {
            case NORTH: nextY++; break;
            case SOUTH: nextY--; break;
            case EAST:  nextX++; break;
            case WEST:  nextX--; break;
        }

        if (table.isValidPosition(nextX, nextY)) {
            x = nextX;
            y = nextY;
        }
    }

    /**
     * Rotates robot 90° left.
     */
    public void turnLeft() {
        facing = facing.turnLeft();
    }

    /**
     * Rotates robot 90° right.
     */
    public void turnRight() {
        facing = facing.turnRight();
    }

    /**
     * Returns current state as "X,Y,FACING".
     */
    public String report() {
        return x + "," + y + "," + facing;
    }

    // Getters for testing
    public int getX() { return x; }
    public int getY() { return y; }
    public Direction getFacing() { return facing; }
}