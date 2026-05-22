package com.example.chess.Game;

import com.example.chess.Enums.PieceType;

public class Move {
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;
    private final PieceType promotion;
    private final PieceType pieceType;
    private final boolean capture;
    private final boolean isCheck;
    private final boolean isCheckmate;
    private final boolean castleKingside;
    private final boolean castleQueenside;

    public Move(int fromRow, int fromCol, int toRow, int toCol, PieceType pieceType, PieceType promotion, boolean capture,
            boolean isCheck, boolean isCheckmate, boolean castleKingside, boolean castleQueenside) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.pieceType = pieceType;
        this.promotion = promotion;
        this.capture = capture;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.castleKingside = castleKingside;
        this.castleQueenside = castleQueenside;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public PieceType getPromotion() {
        return promotion;
    }

    public boolean isCapture() {
        return capture;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public boolean isCastleKingside() {
        return castleKingside;
    }

    public boolean isCastleQueenside() {
        return castleQueenside;
    }

    @Override
    public String toString() {
        // Castling
        if (castleKingside) {
            return "O-O" + (isCheckmate ? "#" : isCheck ? "+" : "");
        }
        if (castleQueenside) {
            return "O-O-O" + (isCheckmate ? "#" : isCheck ? "+" : "");
        }

        StringBuilder sb = new StringBuilder();
        // Piece letter (omit for pawns)
        if (pieceType != null && pieceType.getSanSymbol() != null && !pieceType.getSanSymbol().isEmpty()) {
            sb.append(pieceType.getSanSymbol());
        }

        // Pawn captures include the originating file
        if ((pieceType == null || pieceType.getSanSymbol().isEmpty()) && capture) {
            sb.append((char) ('a' + fromCol));
        }

        if (capture) sb.append('x');

        sb.append((char) ('a' + toCol));
        sb.append(8 - toRow);

        if (promotion != null) {
            sb.append("=");
            sb.append(promotion.getSanSymbol());
        }

        if (isCheckmate) sb.append('#');
        else if (isCheck) sb.append('+');

        return sb.toString();
    }
}
