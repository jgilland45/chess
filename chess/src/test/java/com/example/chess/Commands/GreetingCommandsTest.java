package com.example.chess.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class GreetingCommandsTest {

    @Test
    void sayHelloUsesProvidedName() {
        GreetingCommands commands = new GreetingCommands();

        String output = captureOutput(() -> commands.sayHello("Ada"));
        assertTrue(output.contains("Hello Ada!"));
    }

    @Test
    void sayHelloHandlesDefaultNameExplicitly() {
        GreetingCommands commands = new GreetingCommands();

        String output = captureOutput(() -> commands.sayHello("World"));
        assertEquals("Hello World!" + System.lineSeparator(), output);
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
