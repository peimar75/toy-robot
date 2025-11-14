package se.hiq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Entry point for the Toy Robot Simulator.
 * Reads a command file and executes the commands in order.
 * 
 * Usage:
 *   java se.hiq.Main <input_file>
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java se.hiq.Main <input_file>");
            System.exit(1);
        }

        TableTop table = new TableTop(5, 5);
        CommandProcessor processor = new CommandProcessor(table);

        String filename = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String nextLine = null;
            while ((line = reader.readLine()) != null) {
                // Peek at next line for display
                reader.mark(1000); // Mark current position
                nextLine = reader.readLine();
                reader.reset(); // Reset to marked position
                
                processor.processCommand(line, nextLine);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
}