package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jchessengine.BoardDisplay;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public class Queen extends Piece {
    public Queen(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Queen";
        this.value = 9;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    @Override
    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite == this.isWhite) {
                return false; // No team-killing allowed
            }
        }
        return validateStraightMovement(currentCol, currentRow, newCol, newRow) ||
                validateDiagonalMovement(currentCol, currentRow, newCol, newRow);

    }

    private boolean validateStraightMovement(int currentCol, int currentRow, int newCol, int newRow) {
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

    private boolean validateDiagonalMovement(int currentCol, int currentRow, int newCol, int newRow) {
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
}
