package se.hiq;

import java.util.Locale;

/**
 * Parses and executes textual commands controlling the robot.
 * The robot ignores all commands until a valid PLACE command is received.
 */
public class CommandProcessor {

    private final TableTop table;
    private Robot robot;

    public CommandProcessor(TableTop table) {
        this.table = table;
    }

    /**
     * Processes a single input command (e.g. "MOVE", "LEFT", "PLACE,1,2,EAST").
     * 
     * @param inputCommand the command to process
     * @param nextCommand the next command that will be executed (optional, for display)
     */
    public void processCommand(String inputCommand, String nextCommand) {
        if (inputCommand == null || inputCommand.trim().isEmpty()) return;

        String command = inputCommand.trim().toUpperCase(Locale.ROOT);

        if (command.startsWith("PLACE")) {
            handlePlace(command);
            if (robot != null) Visualizer.render(table, robot, inputCommand.trim(), nextCommand);
            return;
        }

        if (robot == null) {
            // Ignore all non-PLACE commands until a valid PLACE has occurred
            return;
        }

        boolean changed = false;

        switch (command) {
            case "MOVE":
                robot.move(table);
                changed = true;
                break;
            case "LEFT":
                robot.turnLeft();
                changed = true;
                break;
            case "RIGHT":
                robot.turnRight();
                changed = true;
                break;
            case "REPORT":
                System.out.println(robot.report());
                break;
            default:
                // Invalid command ignored silently
        }

        if (changed) {
            Visualizer.render(table, robot, inputCommand.trim(), nextCommand);
        }
    }

    private void handlePlace(String command) {
        String[] parts = command.split(",");
        if (parts.length != 4) return;

        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            Robot.Direction dir = Robot.Direction.valueOf(parts[3]);

            if (table.isValidPosition(x, y)) {
                robot = new Robot(x, y, dir);
            }
        } catch (IllegalArgumentException e) {
            // Invalid coordinates or direction → ignore
        }
    }

    /**
     * Overloaded method for backward compatibility (used in tests).
     */
    public void processCommand(String inputCommand) {
        processCommand(inputCommand, null);
    }

    public Robot getRobot() {
        return robot;
    }
}