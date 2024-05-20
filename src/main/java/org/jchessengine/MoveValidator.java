package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.jchessengine.piece.Piece;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public final class MoveValidator {

    private MoveValidator() {
        //no-op
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

}
