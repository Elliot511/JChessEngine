package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import org.jchessengine.BoardDisplay;

import java.util.Optional;

public class Knight extends Piece{

    public Knight(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Knight";
        this.value = 3;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(3 * spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    @Override
    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite == this.isWhite) {
                return false; // No team-killing allowed
            }
        }
        if (Math.abs(newCol - currentCol) == 2 && Math.abs(currentRow - newRow) == 1) {
            return true;
        }
        return Math.abs(newRow - currentRow) == 2 && Math.abs(currentCol - newCol) == 1;
    }

}
