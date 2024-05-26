package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import org.jchessengine.BoardDisplay;
import org.jchessengine.MoveValidator;

import java.util.Optional;

public class King extends Piece {
    public King(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "King";
        this.value = 100;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(0, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }

    @Override
    public boolean validateMove(Optional<ImageView> maybePiece, int currentCol, int currentRow, int newCol, int newRow) {
        return MoveValidator.isTurn(this) &&
                MoveValidator.validateNoTeamKill(maybePiece, this) &&
                MoveValidator.validateKingMovement(currentCol, currentRow, newCol, newRow) &&
                !MoveValidator.isAttackedTile(board, this, newCol, newRow);
    }
}
