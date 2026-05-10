package com.example.chess.Commands;

import org.springframework.stereotype.Component;

import com.example.chess.Game.ChessGame;

import org.springframework.shell.core.command.annotation.Argument;
import org.springframework.shell.core.command.annotation.Command;

@Component
public class BoardCommands {
	private ChessGame game = new ChessGame();

	@Command(name = "setup", description = "Set up the chess board", group = "Board",
			help = "A command that sets up the chess board. Usage: setup")
	public void setupBoard() {
		game = new ChessGame();

		// Logic to set up the chess board
		System.out.println("Chess board has been set up.");
		game.getBoard().displayBoard();
	}

	@Command(name = "display", description = "Display the chess board", group = "Board",
			help = "A command that displays the chess board. Usage: display")
	public void displayBoard() {
		game.getBoard().displayBoard();
	}

	@Command(name = "state", description = "Print the current game state", group = "Board",
			help = "A command that prints the current game state. Usage: state")
	public void printGameState() {
		game.printGameState();
	}

	@Command(name = "move", description = "Move a piece on the chess board", group = "Board",
			help = "A command that moves a piece on the chess board using algebraic notation. Usage: move <notation>")
	public void movePiece(
		@Argument(index = 0, description = "SAN move notation", defaultValue = "")
		String notation) {
		if (notation == null || notation.trim().isEmpty()) {
			System.out.println("Usage: move <notation>");
			return;
		}
		try {
			game.applySan(notation);
			game.printGameState();
		} catch (IllegalArgumentException | UnsupportedOperationException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

}