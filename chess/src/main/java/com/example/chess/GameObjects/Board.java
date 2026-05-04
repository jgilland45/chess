package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;

public class Board {
    int numRows;
    int numCols;

    Piece[][] pieces;

    public Board() {
        // Initialize the chess board
        numRows = 8;
        numCols = 8;
        pieces = new Piece[numRows][numCols];

        // Set up the pieces on the board
        setupPieces();
    }

    public void setupPieces() {
        // Place pawns
        for (int i = 0; i < numCols; i++) {
            pieces[1][i] = new Pawn(Color.BLACK);
            pieces[6][i] = new Pawn(Color.WHITE);
        }

        // Place rooks
        pieces[0][0] = new Rook(Color.BLACK);
        pieces[0][7] = new Rook(Color.BLACK);
        pieces[7][0] = new Rook(Color.WHITE);
        pieces[7][7] = new Rook(Color.WHITE);

        // Place knights
        pieces[0][1] = new Knight(Color.BLACK);
        pieces[0][6] = new Knight(Color.BLACK);
        pieces[7][1] = new Knight(Color.WHITE);
        pieces[7][6] = new Knight(Color.WHITE);

        // Place bishops
        pieces[0][2] = new Bishop(Color.BLACK);
        pieces[0][5] = new Bishop(Color.BLACK);
        pieces[7][2] = new Bishop(Color.WHITE);
        pieces[7][5] = new Bishop(Color.WHITE);

        // Place queens
        pieces[0][3] = new Queen(Color.BLACK);
        pieces[7][3] = new Queen(Color.WHITE);

        // Place kings
        pieces[0][4] = new King(Color.BLACK);
        pieces[7][4] = new King(Color.WHITE);
    }

    public void displayBoard() {
        // Display the chess board
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (pieces[i][j] == null) {
                    System.out.print("-- ");
                } else {
                    System.out.print(pieces[i][j].toString() + " ");
                }
            }
            System.out.println();
        }
    }

    public Piece getPieceAt(int row, int col) {
        // Get the piece at the specified position
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            return pieces[row][col];
        }
        return null; // Return null if the position is out of bounds
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] pieces) {
        assert pieces.length == numRows && pieces[0].length == numCols : "Invalid board size";
        this.pieces = pieces;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
}
