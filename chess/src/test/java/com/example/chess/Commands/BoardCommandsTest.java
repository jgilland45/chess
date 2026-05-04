package com.example.chess.Commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class BoardCommandsTest {

    @Test
    void setupBoardPrintsBoard() {
        BoardCommands commands = new BoardCommands();

        String output = captureOutput(commands::setupBoard);

        assertTrue(output.contains("Chess board has been set up."));
        assertTrue(output.contains("WK"));
        assertTrue(output.contains("BK"));
        assertTrue(output.split(System.lineSeparator()).length >= 9);
    }

    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            action.run();
            return buffer.toString();
        } finally {
            System.setOut(original);
        }
    }
}
