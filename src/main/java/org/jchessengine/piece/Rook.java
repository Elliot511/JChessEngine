package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jchessengine.BoardDisplay;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public class Rook extends Piece {

    public Rook(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Rook";
        this.value = 5;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(4 * spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    @Override
    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite == this.isWhite) {
                return false; // No team-killing allowed
            }
        }
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
}
