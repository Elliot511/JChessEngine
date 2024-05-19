package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jchessengine.BoardDisplay;
import org.jchessengine.util.BoardUtil;
import org.jchessengine.util.StackPaneUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bishop extends Piece {

    public Bishop(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Bishop";
        this.value = 3;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(2 * spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    @Override
    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite == this.isWhite) {
                return false; // No team-killing allowed
            }
        }
        try {
            if (Math.abs(newCol - currentCol) / Math.abs(currentRow - newRow) == 1) { // Validated diagonal travel
                int directionCol = newCol > currentCol ? 1 : -1;
                int directionRow = newRow > currentRow ? 1 : -1;
                StackPane[][] panes = board.getStackPanes();
                int colIter = currentCol + directionCol;
                int rowIter = currentRow + directionRow;
                while (colIter != newCol && rowIter != newRow) {
                    if (StackPaneUtil.doesTileHavePiece(panes[rowIter][colIter])) {
                        return false;
                    }
                    colIter += directionCol;
                    rowIter += directionRow;
                }
            }
            else {
                return false;
            }
        } catch (ArithmeticException e) {
            return false;
        }
        return true;
    }
}
