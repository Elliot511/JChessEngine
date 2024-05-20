package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jchessengine.BoardDisplay;
import org.jchessengine.MoveValidator;
import org.jchessengine.util.StackPaneUtil;

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
        return MoveValidator.validateNoTeamKill(maybePiece, this) &&
                MoveValidator.validateBishopMovement(board, currentCol, currentRow, newCol, newRow);
    }
}
