package com.example.chess.GameObjects;

public class Position {
    public int x; // File (0-7 for a-h)
    public int y; // Rank (0-7 for 1-8)

    public Position(int x, int y) {
        assert x >= 0 && x < 8 : "Invalid file (x) value: " + x;
        assert y >= 0 && y < 8 : "Invalid rank (y) value: " + y;
        this.x = x;
        this.y = y;
    }

    public int getRow() {
        return y;
    }

    public int getCol() {
        return x;
    }
}
