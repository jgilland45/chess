package com.example.chess.Commands;

import org.springframework.stereotype.Component;

import com.example.chess.GameObjects.Board;

import org.springframework.shell.core.command.annotation.Command;

@Component
public class BoardCommands {

	@Command(name = "setup", description = "Set up the chess board", group = "Board",
			help = "A command that sets up the chess board. Usage: setup")
	public void setupBoard() {
		Board board = new Board();
		board.setupPieces();

		// Logic to set up the chess board
		System.out.println("Chess board has been set up.");
		board.displayBoard();
	}
}