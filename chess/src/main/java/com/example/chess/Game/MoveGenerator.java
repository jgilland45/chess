package com.example.chess.Game;

import java.util.ArrayList;
import java.util.List;

import com.example.chess.Enums.Color;
import com.example.chess.Enums.PieceType;
import com.example.chess.Enums.MovePermutations.Promotion;
import com.example.chess.GameObjects.Board;
import com.example.chess.GameObjects.King;
import com.example.chess.GameObjects.Pawn;
import com.example.chess.GameObjects.Piece;
import com.example.chess.GameObjects.Position;

public class MoveGenerator implements LegalMoveGenerator {
    private boolean validateCheckMarkers = true;

    @Override
    public List<Move> generateLegalMoves(GameState state, boolean validateKingSafety) {
        validateCheckMarkers = true;
        return generateMoves(state, validateKingSafety);
    }

    public List<Move> generatePseudoLegalMoves(GameState state) {
        validateCheckMarkers = false;
        return generateMoves(state, false);
    }

    private List<Move> generateMoves(GameState state, boolean validateKingSafety) {
        Board board = state.getBoard();
        Piece[][] pieces = board.getPieces();
        List<Move> moves = new ArrayList<>();

        for (int fromRow = 0; fromRow < pieces.length; fromRow++) {
            for (int fromCol = 0; fromCol < pieces[fromRow].length; fromCol++) {
                Piece piece = pieces[fromRow][fromCol];
                if (piece == null) {
                    continue;
                }

                switch (piece.getType()) {
                    case PAWN -> addValidPawnMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case KING -> addValidKingMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case ROOK -> addValidRookMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case BISHOP -> addValidBishopMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case KNIGHT -> addValidKnightMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    case QUEEN -> addValidQueenMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
                    default -> throw new IllegalStateException("Unexpected piece type: " + piece.getType());
                }
            }
        }

        return moves;
    }

    private void addMoveToMoves(List<Move> moves, Move move) {
        // Assumes move is already validated for legality (e.g. does not expose own king to check, is not obstructed, etc.)
        if (move != null) {
            moves.add(move);
        }
    }

    private void addToMoves(List<Move> moves, List<Move> newMoves) {
        for (Move move : newMoves) {
            addMoveToMoves(moves, move);
        }
    }

    private List<Move> validateMoves(List<Move> moves, GameState state, boolean validateKingSafety) {
        List<Move> validMoves = new ArrayList<>(moves);
        validMoves = validateNotExposeKing(validMoves, state, validateKingSafety);
        validMoves = validateNotObstructed(validMoves, state);
        validMoves = validatePromotion(validMoves, state, validateKingSafety);
        validMoves = validatePawnCaptures(validMoves, state);
        validMoves = validateQueensideCastling(validMoves, state);
        validMoves = validateKingsideCastling(validMoves, state);
        if (validateCheckMarkers) {
            validMoves = validateChecks(validMoves, state);
            validMoves = validateCheckmates(validMoves, state);
        }

        // System.out.println("Valid moves after validation: " + validMoves);

        return validMoves;
    }

    private void addAndValidateMoves(List<Move> moves, List<Move> movesToValidate, GameState state, boolean validateKingSafety) {
        List<Move> movelist = new ArrayList<>();
        for (Move move : movesToValidate) {
            if (move != null) {
                movelist.add(move);
            }
        }
        movelist = validateMoves(movelist, state, validateKingSafety);

        if (movelist != null) {
            addToMoves(moves, movelist);
        }
    }

    // private List<Move> addMovePermuations(Move move) {
    //     List<Move> moves = new ArrayList<>();
    //     // add all promotion options
    //     for (Promotion promotion : Promotion.values()) {
    //         // add if there is a capture
    //         for (boolean capture : new boolean[] {true, false}) {
    //             // add if there is a check
    //             for (boolean check : new boolean[] {true, false}) {
    //                 // add if there is a checkmate
    //                 for (boolean checkmate : new boolean[] {true, false}) {
    //                     // add if there is kingside castling
    //                     for (boolean castleKingside : new boolean[] {true, false}) {
    //                         // add if there is queenside castling
    //                         for (boolean castleQueenside : new boolean[] {true, false}) {
    //                             PieceType promoteTo = switch (promotion) {
    //                                 case QUEEN -> PieceType.QUEEN;
    //                                 case ROOK -> PieceType.ROOK;
    //                                 case BISHOP -> PieceType.BISHOP;
    //                                 case KNIGHT -> PieceType.KNIGHT;
    //                                 default -> null;
    //                             };
    //                             Move newMove = new Move(
    //                                 move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
    //                                 promoteTo, capture, check, checkmate, castleKingside, castleQueenside
    //                             );
    //                             moves.add(newMove);
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //     return moves;
    // }

    private boolean canCastleKingside(GameState state, Color color) {
        if (color == Color.WHITE) {
            return state.canWhiteCastleKingside();
        } else {
            return state.canBlackCastleKingside();
        }
    }

    private boolean canCastleQueenside(GameState state, Color color) {
        if (color == Color.WHITE) {
            return state.canWhiteCastleQueenside();
        } else {
            return state.canBlackCastleQueenside();
        }
    }

    private void addValidPawnMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        Color color = piece.getColor();
        int targetRow = -1;
        int targetCol = -1;
        if (color == Color.WHITE) {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow < 1 || fromRow > 6) {
                return; // Invalid row for a white pawn (can't move from the last rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 6) {
                targetRow = fromRow - 2;
                Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
                List<Move> newMoves = addMovePermuations(newMove);
                addAndValidateMoves(moves, newMoves, state, validateKingSafety);
                // TODO: check for obstructions, promotion, captures (incl en passant), checks, and checkmates
            }
            // Handle single move
            targetRow = fromRow - 1;
            Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
            List<Move> newMoves = addMovePermuations(newMove);
            addAndValidateMoves(moves, newMoves, state, validateKingSafety);

            // Handle en passant/capture left
            targetRow = fromRow - 1;
            targetCol = fromCol - 1;
            Move enPassantLeft = new Move(fromRow, fromCol, targetRow, targetCol, null, false, false, false, false, false);
            List<Move> enPassantLeftMoves = addMovePermuations(enPassantLeft);
            addAndValidateMoves(moves, enPassantLeftMoves, state, validateKingSafety);

            // Handle en passant/capture right
            targetRow = fromRow - 1;
            targetCol = fromCol + 1;
            Move enPassantRight = new Move(fromRow, fromCol, targetRow, targetCol, null, false, false, false, false, false);
            List<Move> enPassantRightMoves = addMovePermuations(enPassantRight);
            addAndValidateMoves(moves, enPassantRightMoves, state, validateKingSafety);

            // TODO: check for obstructions, promotion, captures (incl en passant), checks, and checkmates
        } else {
            if (fromCol < 0 || fromCol >= 8) {
                return; // Invalid column
            }
            if (fromRow < 1 || fromRow > 7) {
                return; // Invalid row for a black pawn (can't move from the first rank)
            }
            // Handle double move
            if (!piece.hasMoved() && fromRow == 1) {
                targetRow = fromRow + 2;
                Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
                List<Move> newMoves = addMovePermuations(newMove);
                addAndValidateMoves(moves, newMoves, state, validateKingSafety);
                // TODO: check for obstructions, promotion, captures (incl en passant), checks, and checkmates
            }
            // Handle single move
            targetRow = fromRow + 1;
            Move newMove = new Move(fromRow, fromCol, targetRow, fromCol, null, false, false, false, false, false);
            List<Move> newMoves = addMovePermuations(newMove);
            addAndValidateMoves(moves, newMoves, state, validateKingSafety);
            
            // Handle en passant/capture left
            targetRow = fromRow + 1;
            targetCol = fromCol + 1;
            Move enPassantLeft = new Move(fromRow, fromCol, targetRow, targetCol, null, false, false, false, false, false);
            List<Move> enPassantLeftMoves = addMovePermuations(enPassantLeft);
            addAndValidateMoves(moves, enPassantLeftMoves, state, validateKingSafety);

            // Handle en passant/capture right
            targetRow = fromRow + 1;
            targetCol = fromCol - 1;
            Move enPassantRight = new Move(fromRow, fromCol, targetRow, targetCol, null, false, false, false, false, false);
            List<Move> enPassantRightMoves = addMovePermuations(enPassantRight);
            addAndValidateMoves(moves, enPassantRightMoves, state, validateKingSafety);
            
            // TODO: check for obstructions, promotion, captures (incl en passant), checks, and checkmates
        }
    }

    private void addValidKingMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int toRow = fromRow - 1; toRow <= fromRow + 1; toRow++) {
            for (int toCol = fromCol - 1; toCol <= fromCol + 1; toCol++) {
                // Skip the square the king is currently on
                if (toRow == fromRow && toCol == fromCol) {
                    continue;
                }
                // Skip squares that are off the board
                if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
                    continue;
                }
                Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                List<Move> newMoves = addMovePermuations(newMove);
                addAndValidateMoves(moves, newMoves, state, validateKingSafety);
                // TODO: check for obstructions, captures, checks, and checkmates
            }
        }

        // queenside castling
        Move queensideCastleMove = new Move(fromRow, fromCol, fromRow, fromCol - 2, null, false, false, false, false, true);
        List<Move> newQueensideCastleMoves = addMovePermuations(queensideCastleMove);
        addAndValidateMoves(moves, newQueensideCastleMoves, state, validateKingSafety);

        // kingside castling
        Move kingsideCastleMove = new Move(fromRow, fromCol, fromRow, fromCol + 2, null, false, false, false, true, false);
        List<Move> newKingsideCastleMoves = addMovePermuations(kingsideCastleMove);
        addAndValidateMoves(moves, newKingsideCastleMoves, state, validateKingSafety);
    }

    private void addValidRookMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int toRow = 0; toRow < 8; toRow++) {
            if (toRow == fromRow) {
                continue;
            }
            Move newMove = new Move(fromRow, fromCol, toRow, fromCol, null, false, false, false, false, false);
            List<Move> newMoves = addMovePermuations(newMove);
            addAndValidateMoves(moves, newMoves, null, validateKingSafety);
        }
        for (int toCol = 0; toCol < 8; toCol++) {
            if (toCol == fromCol) {
                continue;
            }
            Move newMove = new Move(fromRow, fromCol, fromRow, toCol, null, false, false, false, false, false);
            List<Move> newMoves = addMovePermuations(newMove);
            addAndValidateMoves(moves, newMoves, null, validateKingSafety);
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }
    
    private void addValidBishopMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        for (int offset = -7; offset <= 7; offset++) {
            if (offset == 0) {
                continue;
            }
            for (int rowSign = -1; rowSign <= 1; rowSign += 2) {
                for (int colSign = -1; colSign <= 1; colSign += 2) {
                    int toRow = fromRow + offset * rowSign;
                    int toCol = fromCol + offset * colSign;
                    if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                        Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                        List<Move> newMoves = addMovePermuations(newMove);
                        addAndValidateMoves(moves, newMoves, null, validateKingSafety);
                    }
                }
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidKnightMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        if (fromCol < 0 || fromCol >= 8) {
            return; // Invalid column
        }
        if (fromRow < 0 || fromRow >= 8) {
            return; // Invalid row
        }

        int[][] knightOffsets = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };
        for (int[] offset : knightOffsets) {
            int toRow = fromRow + offset[0];
            int toCol = fromCol + offset[1];
            if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
                Move newMove = new Move(fromRow, fromCol, toRow, toCol, null, false, false, false, false, false);
                List<Move> newMoves = addMovePermuations(newMove);
                addAndValidateMoves(moves, newMoves, null, validateKingSafety);
            }
        }
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private void addValidQueenMoves(List<Move> moves, Piece piece, int fromRow, int fromCol, GameState state, boolean validateKingSafety) {
        addValidRookMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
        addValidBishopMoves(moves, piece, fromRow, fromCol, state, validateKingSafety);
        // TODO: check for obstructions, captures, checks, and checkmates
    }

    private boolean isPromotionRank(Color color, int toRow) {
        return (color == Color.WHITE && toRow == 0) || (color == Color.BLACK && toRow == 7);
    }

    // Order in which we should check move validity:
    // 1. Ensure move does not put own king in check
    // 2. Ensure move is not obstructed by other pieces
    // 2a. If only obstruction is on the target square, ensure it's an opponent's piece (capture)
    // 2b. If only obstruction is on the target square and it is not an opponent's piece, the move is invalid
    // 3. If piece is pawn:
    // 3a. If promoting, add all valid promotion options
    // 3b. If en passant, ensure the target square is the en passant target
    // 4. If piece is king and move is castling, ensure castling rights and that the squares the king passes through are not under attack
    // 5. If move results in check, ensure the opponent's king is attacked in the resulting position
    // 6. If move results in checkmate, ensure the opponent's king is attacked and has no legal moves in the resulting position

    private List<Move> validateNotExposeKing(List<Move> moves, GameState state, boolean validateKingSafety) {
        /*
            Checks if the move would expose the player's own king to check. If it does, returns null. Otherwise, returns the move.
        */
       if (moves == null || moves.isEmpty()) {
           return null;
       }
       if (!validateKingSafety) {
           return new ArrayList<>(moves);
       }

       List<Move> validMoves = new ArrayList<>();

       for (Move move : moves) {
            if (move == null) {
                continue;
            }
            if (state == null) {
                continue;
            }
            
            GameState copyState = state.copy();
            copyState.applyMoveForValidation(move);
            if (!copyState.isInCheck(state.getSideToMove())) {
                validMoves.add(move);
            }
       } 
       return validMoves;
    }

    private List<Move> validateNotObstructed(List<Move> moves, GameState state) {
        /*
            Checks if the move is obstructed by other pieces. If it is, returns null. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8) {
                continue;
            }
            if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
                continue;
            }

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null) {
                continue; // No piece to move
            }

            // Check for obstructions along the path of the move (for sliding pieces)
            if (piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.ROOK || piece.getType() == PieceType.QUEEN) {
                int rowDirection = Integer.compare(toRow, fromRow);
                int colDirection = Integer.compare(toCol, fromCol);
                int currentRow = fromRow + rowDirection;
                int currentCol = fromCol + colDirection;
                while (currentRow != toRow || currentCol != toCol) {
                    if (pieces[currentRow][currentCol] != null) {
                        break; // Path is obstructed
                    }
                    currentRow += rowDirection;
                    currentCol += colDirection;
                }
            }

            // Check for capture on the target square
            Piece targetPiece = pieces[toRow][toCol];
            if (targetPiece != null && targetPiece.getColor() == piece.getColor()) {
                continue; // Can't capture own piece
            } else if (targetPiece != null && targetPiece.getColor() != piece.getColor()) { // Capture enemy piece
                Move moveWithCapture = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(), move.getPromotion(),
                    true, move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
                validMoves.add(moveWithCapture);
            } else {
                validMoves.add(move); // No capture, just a normal move
            }
        }
        return validMoves;
    }

    private List<Move> validatePromotion(List<Move> moves, GameState state, boolean validateKingSafety) {
        /*
            If the move is a pawn move to the promotion rank, adds valid promotion options. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getType() != PieceType.PAWN) {
                validMoves.add(move); // Not a pawn move
                continue;
            }

            if (isPromotionRank(piece.getColor(), toRow)) {
                Move queenPromotionMove = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                    PieceType.QUEEN, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
                Move rookPromotionMove = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                    PieceType.ROOK, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
                Move bishopPromotionMove = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                    PieceType.BISHOP, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
                Move knightPromotionMove = new Move(
                    move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol(),
                    PieceType.KNIGHT, move.isCapture(), move.isCheck(), move.isCheckmate(), move.isCastleKingside(), move.isCastleQueenside()
                );
                validMoves.add(queenPromotionMove);
                validMoves.add(rookPromotionMove);
                validMoves.add(bishopPromotionMove);
                validMoves.add(knightPromotionMove);
            } else {
                validMoves.add(move); // Not a promotion move, so we can just add it as is
            }
        }
        return validMoves;
    }

    private List<Move> validatePawnCaptures(List<Move> moves, GameState state) {
        /*
            If the move is a pawn move that could be a capture, ensures that there is an opponent's piece on the target square (or that it's a valid en passant capture). Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getType() != PieceType.PAWN) {
                validMoves.add(move); // Not a pawn move
                continue;
            }
            
            // If the move is a diagonal move, it must be a capture (either normal or en passant)
            if (Math.abs(toCol - fromCol) == 1 && ((piece.getColor() == Color.WHITE && toRow == fromRow - 1) || (piece.getColor() == Color.BLACK && toRow == fromRow + 1))) {
                // This is a potential en passant capture
                List<Position> enPassantTargets = state.getEnPassantTargets();
                if (enPassantTargets == null) {
                    // No en passant targets, but this could be a capture move, so check if there's an opponent's piece on the target square
                    Piece targetPiece = pieces[toRow][toCol];
                    if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                        if (!move.isCapture()) {
                            continue; // Move must be marked as capture if there is an opponent's piece on the target square
                        } else {
                            validMoves.add(move); // Valid capture move
                        }
                    } else {
                        if (move.isCapture()) {
                            continue; // Move is marked as capture, but there is no opponent's piece on the target square
                        } else {
                            validMoves.add(move); // Valid non-capture move
                        }
                    }
                } else if (enPassantTargets.contains(new Position(toRow, toCol))) {
                    // Must be an en passant capture or a valid capture move
                    if (!move.isCapture()) {
                        continue; // Move must be marked as capture if the target square is an en passant target
                    } else {
                        validMoves.add(move); // Valid en passant capture
                    }
                } else {
                    // cannot be an en passant capture, but there could still be an opponent's piece on the target square
                    Piece targetPiece = pieces[toRow][toCol];
                    if (targetPiece != null && targetPiece.getColor() != piece.getColor()) {
                        if (!move.isCapture()) {
                            continue; // Move must be marked as capture if there is an opponent's piece on the target square
                        } else {
                            validMoves.add(move); // Valid capture move
                        }
                    } else {
                        if (move.isCapture()) {
                            continue; // Move is marked as capture, but there is no opponent's piece on the target square
                        } else {
                            validMoves.add(move); // Valid non-capture move
                        }
                    }
                }
            } else {
                // Not a diagonal move, so it can't be a capture
                if (move.isCapture()) {
                    continue; // Move is marked as capture, but it's not a diagonal move
                } else {
                    validMoves.add(move); // Valid non-capture move
                }
            }
        }
        return validMoves;
    }

    private List<Move> validateQueensideCastling(List<Move> moves, GameState state) {
        /*
            If the move is a queenside castling move, ensures that the appropriate castling rights are available. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getType() != PieceType.KING) {
                validMoves.add(move); // Not a king move
                continue;
            }

            // Check for queenside castling move
            if (fromCol - toCol == 2 && fromRow == toRow) {
                if (!canCastleQueenside(state, piece.getColor())) {
                    if (move.isCastleQueenside()) {
                        continue; // Move is marked as queenside castling, but no queenside castling rights
                    } else {
                        validMoves.add(move); // Move is not marked as queenside castling, so we can still consider it (it will be invalidated later if it's not a valid move)
                    }
                } else {
                    if (!move.isCastleQueenside()) {
                        continue; // Move is not marked as queenside castling, but there are queenside castling rights, so this move must be marked as queenside castling
                    } else {
                        validMoves.add(move); // Valid queenside castling move
                    }
                }
            } else {
                validMoves.add(move); // Not a castling move, so we don't need to validate castling rights or attacked squares
            }
        }
        return validMoves;
    }

    private List<Move> validateKingsideCastling(List<Move> moves, GameState state) {
        /*
            If the move is a kingside castling move, ensures that the appropriate castling rights are available. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            Piece[][] pieces = state.getBoard().getPieces();
            int fromRow = move.getFromRow();
            int fromCol = move.getFromCol();
            int toRow = move.getToRow();
            int toCol = move.getToCol();

            Piece piece = pieces[fromRow][fromCol];
            if (piece == null || piece.getType() != PieceType.KING) {
                validMoves.add(move); // Not a king move
                continue;
            }

            // Check for kingside castling move
            if (toCol - fromCol == 2 && fromRow == toRow) {
                if (!canCastleKingside(state, piece.getColor())) {
                    if (move.isCastleKingside()) {
                        continue; // Move is marked as kingside castling, but no kingside castling rights
                    } else {
                        validMoves.add(move); // Move is not marked as kingside castling, so we can still consider it (it will be invalidated later if it's not a valid move)
                    }
                } else {
                    if (!move.isCastleKingside()) {
                        continue; // Move is not marked as kingside castling, but there are kingside castling rights, so this move must be marked as kingside castling
                    } else {
                        validMoves.add(move); // Valid kingside castling move
                    }
                }
            } else {
                validMoves.add(move); // Not a castling move, so we don't need to validate castling rights or attacked squares
            }
        }
        return validMoves;
    }

    private List<Move> validateChecks(List<Move> moves, GameState state) {
        /*
            If the move is a move that results in check, ensures that the opponent's king is attacked in the resulting position. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            GameState copyState = state.copy();
            copyState.applyMoveForValidation(move);
            if (copyState.isInCheck(state.getSideToMove() == Color.WHITE ? Color.BLACK : Color.WHITE)) {
                if (!move.isCheck()) {
                    continue; // Move results in check, but is not marked as check
                } else {
                    validMoves.add(move); // Valid move that results in check
                }
            } else {
                if (move.isCheck()) {
                    continue; // Move is marked as check, but does not result in check
                } else {
                    validMoves.add(move); // Valid move that does not result in check
                }
            }
        }
        return validMoves;
    }

    private List<Move> validateCheckmates(List<Move> moves, GameState state) {
        /*
            If the move is a move that results in checkmate, ensures that the opponent's king is attacked and has no legal moves in the resulting position. Otherwise, returns the move.
        */
        if (moves == null || moves.isEmpty()) {
            return null;
        }

        List<Move> validMoves = new ArrayList<>();

        for (Move move : moves) {
            if (move == null) {
                continue;
            }

            if (state == null) {
                continue;
            }

            GameState copyState = state.copy();
            copyState.applyMoveForValidation(move);
            MoveGenerator mg = new MoveGenerator();
            // Generate pseudo-legal moves for the piece,
            List<Move> movesAfterPossibleCheckmate = mg.generatePseudoLegalMoves(copyState);
            if (copyState.isInCheck(state.getSideToMove() == Color.WHITE ? Color.BLACK : Color.WHITE) && (movesAfterPossibleCheckmate == null || movesAfterPossibleCheckmate.isEmpty())) {
                if (!move.isCheckmate()) {
                    continue; // Move results in checkmate, but is not marked as checkmate
                } else {
                    validMoves.add(move); // Valid move that results in checkmate
                }
            } else {
                if (move.isCheckmate()) {
                    continue; // Move is marked as checkmate, but does not result in checkmate
                } else {
                    validMoves.add(move); // Valid move that does not result in checkmate
                }
            }
        }
        return validMoves;
    }
}