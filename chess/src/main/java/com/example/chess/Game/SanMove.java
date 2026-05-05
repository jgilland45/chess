package com.example.chess.Game;

import java.util.List;

import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Piece;

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
        // Castling moves identified by flags
        if (castleKingside != move.isCastleKingside()) {
            return false;
        }
        if (castleQueenside != move.isCastleQueenside()) {
            return false;
        }
        if (castleKingside || castleQueenside) {
            return true;
        }

        // If target square doesn't match, it's not a match
        if (move.getToRow() != targetRow || move.getToCol() != targetCol) {
            return false;
        }

        // If promotion specified, it must match
        if (promotion != null && promotion != move.getPromotion()) {
            return false;
        }
        // TODO: use piece type and disambiguation when legal move generation is implemented.

        // Ensure the piece type matches the piece on the source square
        Piece piece = state.getBoard().getPieces()[move.getFromRow()][move.getFromCol()];
        if (piece == null || piece.getType() != pieceType) {
            return false;
        }

        // Ensure the color of the piece matches the side to move
        if (piece.getColor() != state.getSideToMove()) {
            return false;
        }

        // Capture, check, and checkmate flags must match the resulting position
        if (capture != move.isCapture()) {
            return false;
        }
        if (check != move.isCheck()) {
            return false;
        }
        if (checkmate != move.isCheckmate()) {
            return false;
        }

        // Handle disambiguation - if specified, the move must match the file/rank of the source square
        if (disambiguationFile != null && move.getFromCol() != disambiguationFile) {
            return false;
        }
        if (disambiguationRank != null && move.getFromRow() != disambiguationRank) {
            return false;
        }

        if (move.getFromRow() == targetRow && move.getFromCol() == targetCol) {
            return false; // Can't move to the same square
        }

        if (move.getPromotion() != null && move.getPromotion() != promotion) {
            return false; // Promotion piece must match if specified
        }

        return true;
    }

    @Override
    public String toString() {
        // StringBuilder sb = new StringBuilder();
        // if (castleKingside) {
        //     sb.append("O-O");
        // } else if (castleQueenside) {
        //     sb.append("O-O-O");
        // } else {
        //     if (pieceType != PieceType.PAWN) {
        //         sb.append(pieceType.getSanSymbol());
        //     }
        //     sb.append((char) ('a' + targetCol));
        //     sb.append(targetRow + 1);
        // }
        // return sb.toString();

        return "SanMove{" +
                "pieceType=" + pieceType +
                ", targetRow=" + targetRow +
                ", targetCol=" + targetCol +
                ", capture=" + capture +
                ", check=" + check +
                ", checkmate=" + checkmate +
                ", promotion=" + promotion +
                ", castleKingside=" + castleKingside +
                ", castleQueenside=" + castleQueenside +
                ", disambiguationFile=" + disambiguationFile +
                ", disambiguationRank=" + disambiguationRank +
                '}';
    }
}
