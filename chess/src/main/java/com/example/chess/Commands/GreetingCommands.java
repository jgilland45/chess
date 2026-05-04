package com.example.chess.Commands;

import org.springframework.stereotype.Component;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;

@Component
public class GreetingCommands {

	@Command(name = "hello", description = "Say hello to a given name", group = "Greetings",
			help = "A command that greets the user with 'Hello ${name}!'. Usage: hello [-n | --name]=<name>")
	public void sayHello(
		@Option(
			shortName = 'n',
			longName = "name",
			description = "the name of the person to greet",
			defaultValue = "World"
		)
		String name) {
		System.out.println("Hello " + name + "!");
	}

}
