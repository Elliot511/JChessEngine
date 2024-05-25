package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import org.jchessengine.BoardDisplay;
import org.jchessengine.MoveValidator;

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
        return MoveValidator.isTurn(this) &&
                MoveValidator.validateNoTeamKill(maybePiece, this) &&
                MoveValidator.validateKnightMovement(currentCol, currentRow, newCol, newRow);
    }

}
