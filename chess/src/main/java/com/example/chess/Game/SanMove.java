package com.example.chess.Game;

import java.util.List;

import com.example.chess.Enums.PieceType;

public class SanMove {
    private final PieceType pieceType;
    private final int targetRow;
    private final int targetCol;
    private final boolean capture;
    private final boolean check;
    private final boolean checkmate;
    private final PieceType promotion;
    private final boolean castleKingside;
    private final boolean castleQueenside;
    private final Integer disambiguationFile;
    private final Integer disambiguationRank;

    public SanMove(PieceType pieceType, int targetRow, int targetCol, boolean capture, boolean check,
            boolean checkmate, PieceType promotion, boolean castleKingside, boolean castleQueenside,
            Integer disambiguationFile, Integer disambiguationRank) {
        this.pieceType = pieceType;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.capture = capture;
        this.check = check;
        this.checkmate = checkmate;
        this.promotion = promotion;
        this.castleKingside = castleKingside;
        this.castleQueenside = castleQueenside;
        this.disambiguationFile = disambiguationFile;
        this.disambiguationRank = disambiguationRank;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public boolean isCapture() {
        return capture;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public PieceType getPromotion() {
        return promotion;
    }

    public boolean isCastleKingside() {
        return castleKingside;
    }

    public boolean isCastleQueenside() {
        return castleQueenside;
    }

    public Integer getDisambiguationFile() {
        return disambiguationFile;
    }

    public Integer getDisambiguationRank() {
        return disambiguationRank;
    }

    public Move findMatch(List<Move> moves, GameState state) {
        for (Move move : moves) {
            if (matches(move, state)) {
                return move;
            }
        }
        return null;
    }

    private boolean matches(Move move, GameState state) {
        if (castleKingside != move.isCastleKingside()) {
            return false;
        }
        if (castleQueenside != move.isCastleQueenside()) {
            return false;
        }
        if (castleKingside || castleQueenside) {
            return true;
        }
        if (move.getToRow() != targetRow || move.getToCol() != targetCol) {
            return false;
        }
        if (promotion != null && promotion != move.getPromotion()) {
            return false;
        }
        // TODO: use piece type and disambiguation when legal move generation is implemented.
        return true;
    }
}
