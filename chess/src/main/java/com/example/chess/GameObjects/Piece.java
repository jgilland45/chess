package com.example.chess.GameObjects;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.MoveType;
import com.example.chess.Enums.PieceType;

public abstract class Piece {
    protected Color color;
    protected PieceType type;
    protected boolean hasMoved; // To track if the piece has moved (important for pawns and castling)
    protected Position position; // To track the current position of the piece on the board
    protected MoveType[] validMoveTypes; // To track the valid move types for this piece

    public Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
        this.position = null; // Position will be set when the piece is placed on the board
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public MoveType[] getValidMoveTypes() {
        return validMoveTypes;
    }

    public void setValidMoveTypes(MoveType[] validMoveTypes) {
        this.validMoveTypes = validMoveTypes;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
