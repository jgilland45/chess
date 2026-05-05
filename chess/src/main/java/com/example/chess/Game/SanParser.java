package com.example.chess.Game;

import com.example.chess.Enums.PieceType;

public class SanParser {

    private SanParser() {
    }

    public static SanMove parse(String notation) {
        if (notation == null || notation.trim().isEmpty()) {
            throw new IllegalArgumentException("Move notation is required.");
        }

        String working = notation.trim();
        boolean check = false;
        boolean checkmate = false;

        if (working.endsWith("+")) {
            check = true;
            working = working.substring(0, working.length() - 1);
        } else if (working.endsWith("#")) {
            checkmate = true;
            working = working.substring(0, working.length() - 1);
        }

        if (isCastleKingside(working)) {
            return new SanMove(PieceType.KING, -1, -1, false, check, checkmate, null, true, false, null, null);
        }
        if (isCastleQueenside(working)) {
            return new SanMove(PieceType.KING, -1, -1, false, check, checkmate, null, false, true, null, null);
        }

        PieceType pieceType = PieceType.PAWN;
        char firstChar = working.charAt(0);
        if (isPieceLetter(firstChar)) {
            pieceType = pieceTypeFromLetter(firstChar);
            working = working.substring(1);
        }

        PieceType promotion = null;
        int promotionIndex = working.indexOf('=');
        if (promotionIndex != -1) {
            String promotionPart = working.substring(promotionIndex + 1);
            if (promotionPart.length() != 1 || !isPieceLetter(promotionPart.charAt(0))) {
                throw new IllegalArgumentException("Invalid promotion in notation: " + notation);
            }
            promotion = pieceTypeFromLetter(promotionPart.charAt(0));
            working = working.substring(0, promotionIndex);
        }

        if (working.length() < 2) {
            throw new IllegalArgumentException("Invalid SAN move: " + notation);
        }

        String destination = working.substring(working.length() - 2);
        String prefix = working.substring(0, working.length() - 2);
        boolean capture = prefix.contains("x");
        prefix = prefix.replace("x", "");

        Integer disambiguationFile = null;
        Integer disambiguationRank = null;
        if (!prefix.isEmpty()) {
            if (prefix.length() == 1) {
                char value = prefix.charAt(0);
                if (isFile(value)) {
                    disambiguationFile = fileToCol(value);
                } else if (isRank(value)) {
                    disambiguationRank = rankToRow(value);
                } else {
                    throw new IllegalArgumentException("Invalid disambiguation in notation: " + notation);
                }
            } else if (prefix.length() == 2) {
                char file = prefix.charAt(0);
                char rank = prefix.charAt(1);
                if (!isFile(file) || !isRank(rank)) {
                    throw new IllegalArgumentException("Invalid disambiguation in notation: " + notation);
                }
                disambiguationFile = fileToCol(file);
                disambiguationRank = rankToRow(rank);
            } else {
                throw new IllegalArgumentException("Invalid disambiguation in notation: " + notation);
            }
        }

        if (pieceType == PieceType.PAWN) {
            if (!capture && !prefix.isEmpty()) {
                throw new IllegalArgumentException("Invalid pawn notation: " + notation);
            }
            if (capture && disambiguationFile == null) {
                throw new IllegalArgumentException("Pawn captures must include the source file: " + notation);
            }
        }

        int targetCol = fileToCol(destination.charAt(0));
        int targetRow = rankToRow(destination.charAt(1));

        return new SanMove(pieceType, targetRow, targetCol, capture, check, checkmate, promotion,
            false, false, disambiguationFile, disambiguationRank);
    }

    private static boolean isCastleKingside(String value) {
        return "O-O".equals(value) || "0-0".equals(value);
    }

    private static boolean isCastleQueenside(String value) {
        return "O-O-O".equals(value) || "0-0-0".equals(value);
    }

    private static boolean isPieceLetter(char value) {
        return value == 'K' || value == 'Q' || value == 'R' || value == 'B' || value == 'N';
    }

    private static PieceType pieceTypeFromLetter(char value) {
        switch (value) {
            case 'K':
                return PieceType.KING;
            case 'Q':
                return PieceType.QUEEN;
            case 'R':
                return PieceType.ROOK;
            case 'B':
                return PieceType.BISHOP;
            case 'N':
                return PieceType.KNIGHT;
            default:
                throw new IllegalArgumentException("Invalid piece letter: " + value);
        }
    }

    private static boolean isFile(char value) {
        return value >= 'a' && value <= 'h';
    }

    private static boolean isRank(char value) {
        return value >= '1' && value <= '8';
    }

    private static int fileToCol(char file) {
        if (!isFile(file)) {
            throw new IllegalArgumentException("Invalid file: " + file);
        }
        return file - 'a';
    }

    private static int rankToRow(char rank) {
        if (!isRank(rank)) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        return 8 - Character.getNumericValue(rank);
    }
}
