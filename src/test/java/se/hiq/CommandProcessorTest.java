package se.hiq;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandProcessorTest {

    @Test
    void testIgnoreBeforePlace() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        processor.processCommand("MOVE");
        assertNull(processor.getRobot());
    }

    @Test
    void testValidPlaceCreatesRobot() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        processor.processCommand("PLACE,2,3,NORTH");
        assertNotNull(processor.getRobot());
        assertEquals(2, processor.getRobot().getX());
        assertEquals(3, processor.getRobot().getY());
    }

    @Test
    void testInvalidPlaceIgnored() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        processor.processCommand("PLACE,7,7,NORTH");
        assertNull(processor.getRobot());
    }

    @Test
    void testPlaceAtBoundary() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        // Valid boundary positions
        processor.processCommand("PLACE,0,0,NORTH");
        assertNotNull(processor.getRobot());
        
        processor.processCommand("PLACE,4,4,SOUTH");
        assertNotNull(processor.getRobot());
        assertEquals(4, processor.getRobot().getX());
        assertEquals(4, processor.getRobot().getY());
    }

    @Test
    void testPlaceOutOfBounds() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE,5,5,NORTH"); // Just outside
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,-1,0,NORTH"); // Negative
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,0,-1,NORTH"); // Negative
        assertNull(processor.getRobot());
    }

    @Test
    void testInvalidDirection() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE,2,2,INVALID");
        assertNull(processor.getRobot());
    }

    @Test
    void testMalformedPlaceCommand() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE");
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2");
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2,3");
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2,3,NORTH,EXTRA");
        assertNull(processor.getRobot());
    }

    @Test
    void testCaseInsensitiveCommands() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("place,2,2,north");
        assertNotNull(processor.getRobot());
        
        processor.processCommand("Move");
        assertEquals(2, processor.getRobot().getX());
        assertEquals(3, processor.getRobot().getY());
        
        processor.processCommand("left");
        assertEquals(Robot.Direction.WEST, processor.getRobot().getFacing());
        
        processor.processCommand("right");
        assertEquals(Robot.Direction.NORTH, processor.getRobot().getFacing());
    }

    @Test
    void testMultiplePlaceCommands() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE,1,1,NORTH");
        assertEquals(1, processor.getRobot().getX());
        assertEquals(1, processor.getRobot().getY());
        
        processor.processCommand("PLACE,3,3,SOUTH");
        assertEquals(3, processor.getRobot().getX());
        assertEquals(3, processor.getRobot().getY());
        assertEquals(Robot.Direction.SOUTH, processor.getRobot().getFacing());
    }

    @Test
    void testEmptyAndWhitespaceCommands() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("");
        assertNull(processor.getRobot());
        
        processor.processCommand("   ");
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2,2,NORTH");
        assertNotNull(processor.getRobot());
        
        processor.processCommand("   MOVE   ");
        assertEquals(2, processor.getRobot().getX());
        assertEquals(3, processor.getRobot().getY());
    }

    @Test
    void testNullCommand() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand(null);
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2,2,NORTH");
        assertNotNull(processor.getRobot());
    }

    @Test
    void testInvalidCommandsIgnored() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE,2,2,NORTH");
        assertNotNull(processor.getRobot());
        
        processor.processCommand("INVALID_COMMAND");
        assertEquals(2, processor.getRobot().getX());
        assertEquals(2, processor.getRobot().getY());
        
        processor.processCommand("JUMP");
        assertEquals(2, processor.getRobot().getX());
        assertEquals(2, processor.getRobot().getY());
    }

    @Test
    void testReportBeforePlace() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("REPORT");
        assertNull(processor.getRobot());
    }

    @Test
    void testNonNumericCoordinates() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);
        
        processor.processCommand("PLACE,abc,2,NORTH");
        assertNull(processor.getRobot());
        
        processor.processCommand("PLACE,2,xyz,NORTH");
        assertNull(processor.getRobot());
    }
}