package se.hiq;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableTopTest {

    @Test
    void testValidPosition() {
        TableTop table = new TableTop(5, 5);
        assertTrue(table.isValidPosition(0, 0));
        assertTrue(table.isValidPosition(4, 4));
        assertTrue(table.isValidPosition(2, 3));
    }

    @Test
    void testInvalidPositionOutOfBounds() {
        TableTop table = new TableTop(5, 5);
        assertFalse(table.isValidPosition(5, 5));
        assertFalse(table.isValidPosition(5, 0));
        assertFalse(table.isValidPosition(0, 5));
        assertFalse(table.isValidPosition(-1, 0));
        assertFalse(table.isValidPosition(0, -1));
        assertFalse(table.isValidPosition(10, 10));
    }

    @Test
    void testBoundaryPositions() {
        TableTop table = new TableTop(5, 5);
        // Valid boundaries (0 to 4 inclusive)
        assertTrue(table.isValidPosition(0, 0));
        assertTrue(table.isValidPosition(4, 4));
        assertTrue(table.isValidPosition(0, 4));
        assertTrue(table.isValidPosition(4, 0));
        
        // Just outside
        assertFalse(table.isValidPosition(5, 0));
        assertFalse(table.isValidPosition(0, 5));
    }

    @Test
    void testDifferentTableSizes() {
        TableTop smallTable = new TableTop(3, 3);
        assertTrue(smallTable.isValidPosition(0, 0));
        assertTrue(smallTable.isValidPosition(2, 2));
        assertFalse(smallTable.isValidPosition(3, 3));
        
        TableTop largeTable = new TableTop(10, 10);
        assertTrue(largeTable.isValidPosition(9, 9));
        assertFalse(largeTable.isValidPosition(10, 10));
    }

    @Test
    void testRectangularTable() {
        TableTop table = new TableTop(5, 3);
        assertTrue(table.isValidPosition(4, 2));
        assertFalse(table.isValidPosition(4, 3));
        assertFalse(table.isValidPosition(5, 2));
    }
}

