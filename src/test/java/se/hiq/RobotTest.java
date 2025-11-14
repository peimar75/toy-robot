package se.hiq;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    @Test
    void testLeftTurn() {
        Robot r = new Robot(0, 0, Robot.Direction.NORTH);
        r.turnLeft();
        assertEquals(Robot.Direction.WEST, r.getFacing());
    }

    @Test
    void testRightTurn() {
        Robot r = new Robot(0, 0, Robot.Direction.NORTH);
        r.turnRight();
        assertEquals(Robot.Direction.EAST, r.getFacing());
    }

    @Test
    void testMoveWithinBounds() {
        TableTop table = new TableTop(5, 5);
        Robot r = new Robot(0, 0, Robot.Direction.NORTH);
        r.move(table);
        assertEquals(0, r.getX());
        assertEquals(1, r.getY());
    }

    @Test
    void testMoveBlockedAtEdge() {
        TableTop table = new TableTop(5, 5);
        Robot r = new Robot(0, 0, Robot.Direction.SOUTH);
        r.move(table);
        assertEquals(0, r.getY()); // unchanged
    }

    @Test
    void testMoveAllDirections() {
        TableTop table = new TableTop(5, 5);
        Robot r = new Robot(2, 2, Robot.Direction.NORTH);
        r.move(table);
        assertEquals(2, r.getX());
        assertEquals(3, r.getY());
        assertEquals(Robot.Direction.NORTH, r.getFacing());

        r = new Robot(2, 2, Robot.Direction.SOUTH);
        r.move(table);
        assertEquals(2, r.getX());
        assertEquals(1, r.getY());

        r = new Robot(2, 2, Robot.Direction.EAST);
        r.move(table);
        assertEquals(3, r.getX());
        assertEquals(2, r.getY());

        r = new Robot(2, 2, Robot.Direction.WEST);
        r.move(table);
        assertEquals(1, r.getX());
        assertEquals(2, r.getY());
    }

    @Test
    void testMoveBlockedAtAllEdges() {
        TableTop table = new TableTop(5, 5);
        
        // North edge
        Robot r = new Robot(2, 4, Robot.Direction.NORTH);
        r.move(table);
        assertEquals(4, r.getY()); // unchanged
        
        // South edge
        r = new Robot(2, 0, Robot.Direction.SOUTH);
        r.move(table);
        assertEquals(0, r.getY()); // unchanged
        
        // East edge
        r = new Robot(4, 2, Robot.Direction.EAST);
        r.move(table);
        assertEquals(4, r.getX()); // unchanged
        
        // West edge
        r = new Robot(0, 2, Robot.Direction.WEST);
        r.move(table);
        assertEquals(0, r.getX()); // unchanged
    }

    @Test
    void testFullRotationLeft() {
        Robot r = new Robot(0, 0, Robot.Direction.NORTH);
        r.turnLeft(); // NORTH -> WEST
        assertEquals(Robot.Direction.WEST, r.getFacing());
        r.turnLeft(); // WEST -> SOUTH
        assertEquals(Robot.Direction.SOUTH, r.getFacing());
        r.turnLeft(); // SOUTH -> EAST
        assertEquals(Robot.Direction.EAST, r.getFacing());
        r.turnLeft(); // EAST -> NORTH
        assertEquals(Robot.Direction.NORTH, r.getFacing());
    }

    @Test
    void testFullRotationRight() {
        Robot r = new Robot(0, 0, Robot.Direction.NORTH);
        r.turnRight(); // NORTH -> EAST
        assertEquals(Robot.Direction.EAST, r.getFacing());
        r.turnRight(); // EAST -> SOUTH
        assertEquals(Robot.Direction.SOUTH, r.getFacing());
        r.turnRight(); // SOUTH -> WEST
        assertEquals(Robot.Direction.WEST, r.getFacing());
        r.turnRight(); // WEST -> NORTH
        assertEquals(Robot.Direction.NORTH, r.getFacing());
    }

    @Test
    void testReportFormat() {
        Robot r = new Robot(3, 4, Robot.Direction.SOUTH);
        String report = r.report();
        assertEquals("3,4,SOUTH", report);
    }

    @Test
    void testMoveAtCorner() {
        TableTop table = new TableTop(5, 5);
        
        // Top-left corner
        Robot r = new Robot(0, 4, Robot.Direction.NORTH);
        r.move(table);
        assertEquals(0, r.getX());
        assertEquals(4, r.getY()); // blocked
        
        // Top-right corner
        r = new Robot(4, 4, Robot.Direction.EAST);
        r.move(table);
        assertEquals(4, r.getX()); // blocked
        assertEquals(4, r.getY());
        
        // Bottom-right corner
        r = new Robot(4, 0, Robot.Direction.SOUTH);
        r.move(table);
        assertEquals(4, r.getX());
        assertEquals(0, r.getY()); // blocked
    }
}