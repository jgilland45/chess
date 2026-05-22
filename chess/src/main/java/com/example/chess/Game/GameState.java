package com.example.chess.Game;

import java.util.ArrayList;
import java.util.List;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;
import com.example.chess.GameObjects.Board;
import com.example.chess.GameObjects.Piece;
import com.example.chess.GameObjects.Position;

public class GameState {
    private final Board board;
    private Color sideToMove;
    private boolean whiteCastleKingside;
    private boolean whiteCastleQueenside;
    private boolean blackCastleKingside;
    private boolean blackCastleQueenside;
    private List<Position> enPassantTargets;
    private int halfmoveClock;
    private int fullmoveNumber;

    private GameState(Board board) {
        this.board = board;
        this.sideToMove = Color.WHITE;
        this.whiteCastleKingside = true;
        this.whiteCastleQueenside = true;
        this.blackCastleKingside = true;
        this.blackCastleQueenside = true;
        this.enPassantTargets = new ArrayList<>();
        this.halfmoveClock = 0;
        this.fullmoveNumber = 1;

        updateCastlingRights(Color.WHITE);
        updateEnPassantTargets(Color.WHITE);
        updateCastlingRights(Color.BLACK);
        updateEnPassantTargets(Color.BLACK);
    }

    public static GameState initial() {
        return new GameState(new Board());
    }

    public Board getBoard() {
        return board;
    }

    public Color getSideToMove() {
        return sideToMove;
    }

    public boolean canWhiteCastleKingside() {
        return whiteCastleKingside;
    }

    public boolean canWhiteCastleQueenside() {
        return whiteCastleQueenside;
    }

    public boolean canBlackCastleKingside() {
        return blackCastleKingside;
    }

    public boolean canBlackCastleQueenside() {
        return blackCastleQueenside;
    }

    public List<Position> getEnPassantTargets() {
        return enPassantTargets;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void applyMove(Move move) {
        applyMoveInternal(move, true);
    }

    void applyMoveForValidation(Move move) {
        applyMoveInternal(move, false);
    }

    private void applyMoveInternal(Move move, boolean updateDerivedState) {
        board.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());

        if (!updateDerivedState) {
            return;
        }

        // update white castling rights
        updateCastlingRights(Color.WHITE);

        // update black castling rights
        updateCastlingRights(Color.BLACK);

        // update white en passant targets
        updateEnPassantTargets(Color.WHITE);

        // update black en passant targets
        updateEnPassantTargets(Color.BLACK);

        // update halfmove clock (50 move rule)
        Piece movedPiece = board.getPieceAt(move.getToRow(), move.getToCol());
        if (move.isCapture() || (movedPiece != null && movedPiece.getType() == PieceType.PAWN)) {
            halfmoveClock = 0;
        } else {
            halfmoveClock++;
        }

        // update fullmove number (number of full moves) and side to move
        if (sideToMove == Color.BLACK) {
            fullmoveNumber++;
        }
        sideToMove = (sideToMove == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void updateCastlingRights(Color color) {
        int homeRow;
        boolean castleKingside = true;
        boolean castleQueenside = true;
        if (color == Color.WHITE) {
            homeRow = 7;
        } else {
            homeRow = 0;
        }

        // check that king has not moved from initial square
        Piece king = board.getPieceAt(homeRow, 4);
        if (king == null || king.getType() != PieceType.KING) {
            castleKingside = false;
            castleQueenside = false;
        } else if (king.hasMoved()) {
            castleKingside = false;
            castleQueenside = false;
        } else {
            // check that kingside rook has not moved from initial square
            Piece kingsideRook = board.getPieceAt(homeRow, 7);
            if (kingsideRook == null || kingsideRook.getType() != PieceType.ROOK || kingsideRook.hasMoved()) {
                castleKingside = false;
            }
            // check that queenside rook has not moved from initial square
            Piece queensideRook = board.getPieceAt(homeRow, 0);
            if (queensideRook == null || queensideRook.getType() != PieceType.ROOK || queensideRook.hasMoved()) {
                castleQueenside = false;
            }
            // check there are no pieces between king and rooks
            if (board.getPieceAt(homeRow, 5) != null || board.getPieceAt(homeRow, 6) != null) {
                castleKingside = false;
            }
            if (board.getPieceAt(homeRow, 1) != null || board.getPieceAt(homeRow, 2) != null || board.getPieceAt(homeRow, 3) != null) {
                castleQueenside = false;
            }
            // check if king is in check
            if (isInCheck(color)) {
                castleKingside = false;
                castleQueenside = false;
            }
            // check that squares the king would pass through are not attacked
            if (isSquareAttackedBy(homeRow, 5, (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
                castleKingside = false;
            }
            if (isSquareAttackedBy(homeRow, 6, (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
                castleKingside = false;
            }
            if (isSquareAttackedBy(homeRow, 3, (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
                castleQueenside = false;
            }
            if (isSquareAttackedBy(homeRow, 2, (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
                castleQueenside = false;
            }
            if (isSquareAttackedBy(homeRow, 1, (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
                castleQueenside = false;
            }
        }

        if (color == Color.WHITE) {
            whiteCastleKingside = castleKingside;
            whiteCastleQueenside = castleQueenside;
        } else {
            blackCastleKingside = castleKingside;
            blackCastleQueenside = castleQueenside;
        }
    }

    private void updateEnPassantTargets(Color color) {
        // go through all pawns of the given color and check if any of them can be captured en passant
        // this will look a little weird, since if you pass in "White",
        // you will be checking for black pawns that can be captured en passant by white pawns

        // TODO: ensure the potential en passant target square is actually valid (i.e. the pawn just made a 2-square move from its starting position)

        enPassantTargets.clear();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getType() == PieceType.PAWN && piece.getColor() != color) {
                    // check if this pawn can be captured en passant by any of the given color's pawns
                    int direction = (color == Color.WHITE) ? -1 : 1;
                    if (col > 0) {
                        Piece leftPawn = board.getPieceAt(row + direction, col - 1);
                        if (leftPawn != null && leftPawn.getType() == PieceType.PAWN && leftPawn.getColor() == color) {
                            enPassantTargets.add(new Position(row, col));
                        }
                    }
                    if (col < 7) {
                        Piece rightPawn = board.getPieceAt(row + direction, col + 1);
                        if (rightPawn != null && rightPawn.getType() == PieceType.PAWN && rightPawn.getColor() == color) {
                            enPassantTargets.add(new Position(row, col));
                        }
                    }
                }
            }
        }
    }

    public boolean isSquareAttackedBy(int i, int j, Color color) {
        MoveGenerator moveGenerator = new MoveGenerator();
        // Generate pseudo-legal moves for all pieces,
        // since we are only checking if the square is attacked, we don't need to worry about king safety
        
        // System.out.println("Calling from isSquareAttackedBy for" + color + " on square " + (char)('a' + j) + (8 - i));
        List<Move> moves = moveGenerator.generatePseudoLegalMoves(this);
        if (moves == null || moves.isEmpty()) {
            return false;
        }

        Piece[][] pieces = board.getPieces();
        for (Move move : moves) {
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8) {
                continue;
            }
            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getColor() != color) {
                continue;
            }
            if (move.getToRow() == i && move.getToCol() == j) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(Color color) {
        // Find the king of the given color
        Position kingPosition = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPieceAt(i, j);
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                    kingPosition = new Position(i, j);
                    break;
                }
            }
            if (kingPosition != null) {
                break;
            }
        }

        if (kingPosition == null) {
            // This should not happen in a valid game state, but if it does, we can consider the king to be in check
            return true;
        }

        if (isSquareAttackedBy(kingPosition.getRow(), kingPosition.getCol(), (color == Color.WHITE) ? Color.BLACK : Color.WHITE)) {
            return true;
        }

        return false;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Side to move: ").append(sideToMove).append("\n");
        sb.append("Castling rights: ");
        if (whiteCastleKingside) {
            sb.append("White kingside ");
        } if (whiteCastleQueenside) {
            sb.append("White queenside ");
        } if (blackCastleKingside) {
            sb.append("Black kingside ");
        } if (blackCastleQueenside) {
            sb.append("Black queenside ");
        }
        sb.append("\n");
        sb.append("En passant targets: ");
        for (Position pos : enPassantTargets) {
            sb.append((char)('a' + pos.getCol())).append(8 - pos.getRow()).append(" ");
        }
        sb.append("\n");
        sb.append("Halfmove clock: ").append(halfmoveClock).append("\n");
        sb.append("Fullmove number: ").append(fullmoveNumber).append("\n");
        sb.append("Board:\n");
        sb.append(board.toString());
        return sb.toString();
    }

    public GameState copy() {
        GameState copy = new GameState(board.copy());
        copy.sideToMove = sideToMove;
        copy.whiteCastleKingside = whiteCastleKingside;
        copy.whiteCastleQueenside = whiteCastleQueenside;
        copy.blackCastleKingside = blackCastleKingside;
        copy.blackCastleQueenside = blackCastleQueenside;
        copy.enPassantTargets = new ArrayList<>();
        for (Position pos : enPassantTargets) {
            copy.enPassantTargets.add(new Position(pos.getRow(), pos.getCol()));
        }
        copy.halfmoveClock = halfmoveClock;
        copy.fullmoveNumber = fullmoveNumber;
        return copy;
    }

    public void printValidMoves() {
        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> validMoves = moveGenerator.generateLegalMoves(this, true);
        System.out.println("Valid moves for " + sideToMove + ":");
        for (Move move : validMoves) {
            System.out.print(move + ", ");
        }
        System.out.println();
    }
}
