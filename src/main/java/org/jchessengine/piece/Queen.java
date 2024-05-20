package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jchessengine.BoardDisplay;
import org.jchessengine.MoveValidator;
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
        return MoveValidator.validateNoTeamKill(maybePiece, this) &&
                (MoveValidator.validateRookMovement(board, currentCol, currentRow, newCol, newRow) ||
                MoveValidator.validateBishopMovement(board, currentCol, currentRow, newCol, newRow));
    }

}
