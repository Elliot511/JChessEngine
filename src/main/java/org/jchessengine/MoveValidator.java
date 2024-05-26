package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jchessengine.piece.Bishop;
import org.jchessengine.piece.King;
import org.jchessengine.piece.Knight;
import org.jchessengine.piece.Pawn;
import org.jchessengine.piece.Piece;
import org.jchessengine.piece.Queen;
import org.jchessengine.piece.Rook;
import org.jchessengine.util.BoardUtil;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public final class MoveValidator {

    private MoveValidator() {
        //no-op
    }

    public static boolean isTurn(Piece self) {
        return self.getBoard().getGameStateController().isWhitesTurn() == self.isWhite();
    }

    public static boolean validateNoTeamKill(Optional<ImageView> maybePiece, Piece self) {
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            return piece.isWhite() != self.isWhite(); // No team-killing allowed
        }
        return true;
    }

    public static boolean validateRookMovement(BoardDisplay board, int currentCol, int currentRow,
            int newCol, int newRow) {
        if ((newCol != currentCol && newRow == currentRow) || // Validate straight-line movement
                (newRow != currentRow && newCol == currentCol)) {
            boolean movingAcrossCol = newCol != currentCol;
            StackPane[][] panes = board.getStackPanes();
            int direction = movingAcrossCol ? (newCol > currentCol ? 1 : -1) : (newRow > currentRow ? 1 : -1);
            int iterator = movingAcrossCol ? currentCol + direction : currentRow + direction;
            int target = movingAcrossCol ? newCol : newRow;
            while (iterator != target) {
                if (StackPaneUtil.doesTileHavePiece(movingAcrossCol ?
                        panes[currentRow][iterator] : panes[iterator][currentCol])) {
                    return false;
                }
                iterator += direction;
            }
            return true;
        }
        return false;
    }

    public static boolean validateBishopMovement(BoardDisplay board, int currentCol, int currentRow, int newCol, int newRow) {
        try {
            if ((double) Math.abs(newCol - currentCol) / (double) Math.abs(currentRow - newRow) == 1) { // Validated diagonal travel
                StackPane[][] panes = board.getStackPanes();
                int directionCol = newCol > currentCol ? 1 : -1;
                int directionRow = newRow > currentRow ? 1 : -1;
                int colIter = currentCol + directionCol;
                int rowIter = currentRow + directionRow;
                while (colIter != newCol && rowIter != newRow) {
                    if (StackPaneUtil.doesTileHavePiece(panes[rowIter][colIter])) {
                        return false;
                    }
                    colIter += directionCol;
                    rowIter += directionRow;
                }
                return true;
            }
            else {
                return false;
            }
        } catch (ArithmeticException e) {
            return false;
        }
    }

    public static boolean validateKnightMovement(int currentCol, int currentRow, int newCol, int newRow) {
        if (Math.abs(newCol - currentCol) == 2 && Math.abs(currentRow - newRow) == 1) {
            return true;
        }
        return Math.abs(newRow - currentRow) == 2 && Math.abs(currentCol - newCol) == 1;
    }

    public static boolean validatePawnMovement(GameStateController gameStateController, Optional<ImageView> maybePiece,
            int currentCol, int currentRow, int newCol, int newRow, int startRow, boolean isWhite) {
        int direction = isWhite ? -1 : 1;
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite() == isWhite) {
                return false; // No team-killing allowed
            }
            return newRow == currentRow + direction && (newCol == currentCol - 1 || newCol == currentCol + 1);

        }
        if (validateEnPassant(gameStateController, direction, currentRow, newCol, newRow)) {
            return true;
        }
        return newCol == currentCol && newRow == currentRow + direction ||
                (currentRow == startRow && newRow == currentRow + (direction * 2));
    }

    private static boolean validateEnPassant(GameStateController gameStateController, int direction, int currentRow,
            int newCol, int newRow) {
        Optional<StackPane> maybeEnPassantablePiece = gameStateController.getEnPassantablePiece();
        if (maybeEnPassantablePiece.isPresent()) {
            int targetCol = GridPane.getColumnIndex(maybeEnPassantablePiece.get());
            int targetRow = GridPane.getRowIndex(maybeEnPassantablePiece.get());
            if (newRow != currentRow && newCol == targetCol && newRow == targetRow + direction) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateKingMovement(int currentCol, int currentRow, int newCol, int newRow) {
        if (Math.abs(newRow - currentRow) <= 1 && Math.abs(newCol - currentCol) <= 1) {
            //StackPane target = board.getStackPanes()[newCol][newRow];
            /**
             * TODO: Some kind of algorithm to check if target tile under attack.
             * 1. Check existing enemy pieces (TODO: Add removal of pieces from list of pieces upon capture)
             * 2. Use attack patterns of existing enemy pieces starting from the target tile.
             *  a. If an enemy is found, the tile is under attack
             *  b. If no enemy is found, the tile is valid to move to.
             */
            return true; // Add king blocks
        }
        return false;
    }

    public static boolean isAttackedTile(BoardDisplay board, Piece self, int newCol, int newRow) {
        return isEnemyKnightPresent(board, self, newCol, newRow) ||
                isEnemyRookPresent(board, self, newCol, newRow) ||
                isEnemyBishopPresent(board, self, newCol, newRow) ||
                isEnemyQueenPresent(board, self, newCol, newRow) ||
                isEnemyPawnPresent(board, self, newCol, newRow) ||
                isEnemyKingPresent(board, self, newCol, newRow);
    }

    public static boolean isEnemyPawnPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int direction = self.isWhite() ? -1 : 1;
        int[][] potentialEnemyTiles = { {newCol + 1, newRow + direction}, {newCol - 1, newRow + direction} };
        return checkNonSlidingEnemyPath(board, potentialEnemyTiles, self, Pawn.class, newCol, newRow);
    }

    public static boolean isEnemyKnightPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int[][] potentialEnemyTiles = { {newCol - 1, newRow - 2}, {newCol - 1, newRow + 2},
                {newCol + 1, newRow - 2}, {newCol + 1, newRow + 2}, {newCol - 2, newRow -1}, {newCol - 2, newRow + 1},
                {newCol + 2, newRow - 1}, {newCol + 2, newRow + 1} };
        return checkNonSlidingEnemyPath(board, potentialEnemyTiles, self, Knight.class, newCol, newRow);
    }

    public static boolean isEnemyKingPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int[][] potentialEnemyTiles = { {newCol -1, newRow}, {newCol + 1, newRow}, {newCol , newRow - 1},
                {newCol, newRow + 1}, {newCol - 1, newRow - 1}, {newCol + 1, newRow + 1},
                {newCol + 1, newRow - 1}, {newCol - 1, newRow + 1} };
        return checkNonSlidingEnemyPath(board, potentialEnemyTiles, self, King.class, newCol, newRow);
    }

    public static boolean isEnemyRookPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; // Directions of travel
        return checkSlidingEnemyPath(board, directions, self, Rook.class, newCol, newRow);
    }

    public static boolean isEnemyBishopPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int[][] directions = { {-1, -1}, {1, 1}, {1, -1}, {-1, 1} }; // Directions of travel
        return checkSlidingEnemyPath(board, directions, self, Bishop.class, newCol, newRow);
    }

    public static boolean isEnemyQueenPresent(BoardDisplay board, Piece self, int newCol, int newRow) {
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 1} }; // Directions of travel
        return checkSlidingEnemyPath(board, directions, self, Queen.class, newCol, newRow);
    }

    private static boolean checkSlidingEnemyPath(BoardDisplay board, int[][] directions, Piece self,
            Class<? extends Piece> enemyPieceType, int newCol, int newRow) {
        StackPane[][] tiles = board.getStackPanes();
        boolean isWhite = self.isWhite();
        for (int[] direction : directions) {
            int col = newCol;
            int row = newRow;
            while (BoardUtil.isInBounds(col, row)) {
                col += direction[0];
                row += direction[1];
                if (!BoardUtil.isInBounds(col, row)) break;
                StackPane tile = tiles[row][col];
                if (StackPaneUtil.doesTileHavePiece(tile)) {
                    Piece piece = ((Piece) StackPaneUtil.getPieceFromTile(tile).getUserData());
                    if (piece.getClass() == enemyPieceType && piece.isWhite() != isWhite) {
                        return true;
                    }
                    break; // Found piece, but it does not match, and blocks what we are looking for from attacking.
                }
            }
        }
        return false;
    }

    private static boolean checkNonSlidingEnemyPath(BoardDisplay board, int[][] potentialEnemyTiles, Piece self,
            Class<? extends Piece> enemyPieceType, int newCol, int newRow) {
        StackPane[][] tiles = board.getStackPanes();
        for (int[] potentialEnemyTile : potentialEnemyTiles) {
            if (BoardUtil.isInBounds(potentialEnemyTile[0], potentialEnemyTile[1])) {
                StackPane tile = tiles[potentialEnemyTile[1]][potentialEnemyTile[0]];
                if (StackPaneUtil.doesTileHavePiece(tile)) {
                    Piece piece = ((Piece) StackPaneUtil.getPieceFromTile(tile).getUserData());
                    if (piece.getClass() == enemyPieceType && piece.isWhite() != self.isWhite()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
