package se.hiq;

/**
 * Responsible for displaying the table grid and robot position in console.
 */
public class Visualizer {

    /**
     * Prints the current state of the table and robot position.
     * (0,0) is at the bottom-left corner.
     * 
     * @param table the tabletop
     * @param robot the robot (may be null)
     * @param command the command that was executed (optional, can be null)
     * @param nextCommand the next command that will be executed (optional, for display)
     */
    public static void render(TableTop table, Robot robot, String command, String nextCommand) {
        if (robot == null) {
            System.out.println("[Robot not yet placed]");
            return;
        }

        int width = table.getWidth();
        int height = table.getHeight();

        // Show the command that was executed and next command
        if (command != null && !command.trim().isEmpty()) {
            System.out.println("\n>>> Executed: " + command.toUpperCase());
            if (nextCommand != null && !nextCommand.trim().isEmpty()) {
                System.out.println(">>> Next: " + nextCommand.toUpperCase());
            }
        }

        // Arrow to indicate facing direction
        String icon;
        switch (robot.getFacing()) {
            case NORTH: icon = "↑"; break;
            case SOUTH: icon = "↓"; break;
            case EAST:  icon = "→"; break;
            case WEST:  icon = "←"; break;
            default:    icon = "?";
        }

        // Draw the grid (top row printed first)
        System.out.println();
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (x == robot.getX() && y == robot.getY()) {
                    System.out.print("[" + icon + "]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println("  " + y);
        }

        // Print X-axis labels dynamically
        for (int x = 0; x < width; x++) {
            System.out.print(" " + x + " ");
        }
        System.out.println(" (X-axis)");
        System.out.println();
        System.out.println("Current Position: " + robot.report());
        System.out.println("--------------------------------------");
    }

    /**
     * Overloaded method for backward compatibility.
     */
    public static void render(TableTop table, Robot robot) {
        render(table, robot, null, null);
    }

    /**
     * Overloaded method with command only.
     */
    public static void render(TableTop table, Robot robot, String command) {
        render(table, robot, command, null);
    }
}