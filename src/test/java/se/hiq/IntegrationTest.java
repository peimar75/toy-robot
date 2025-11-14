package se.hiq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {

    @Test
    void testExampleC() {
        TableTop table = new TableTop(5, 5);
        CommandProcessor p = new CommandProcessor(table);

        p.processCommand("PLACE,1,2,EAST");
        p.processCommand("MOVE");
        p.processCommand("MOVE");
        p.processCommand("LEFT");
        p.processCommand("MOVE");

        assertEquals("3,3,NORTH", p.getRobot().report());
    }
}