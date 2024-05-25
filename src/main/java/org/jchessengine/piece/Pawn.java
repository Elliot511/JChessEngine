package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import org.jchessengine.BoardDisplay;
import org.jchessengine.MoveValidator;

import java.util.Optional;

public class Pawn extends Piece {
    public Pawn(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Pawn";
        this.value = 1;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(5 * spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        return MoveValidator.isTurn(this) &&
                MoveValidator.validatePawnMovement(board.getGameStateController(), maybePiece, currentCol,
                currentRow, newCol, newRow, row, isWhite);
    }
}
