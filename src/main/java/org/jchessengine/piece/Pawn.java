package org.jchessengine.piece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.jchessengine.BoardDisplay;
import org.jchessengine.util.StackPaneUtil;

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
        int direction = this.isWhite ? -1 : 1;
        if (maybePiece.isPresent()) {
            Piece piece = (Piece) maybePiece.get().getUserData();
            if (piece.isWhite == this.isWhite) {
                return false; // No team-killing allowed
            }
            return newRow == currentRow + direction && (newCol == currentCol - 1 || newCol == currentCol + 1);

        }
        if (canEnPassant(newCol, newRow, currentRow, direction)) {
            return true;
        }
        return newCol == currentCol && newRow == currentRow + direction || (currentRow == row && newRow == currentRow + (direction * 2));
    }

    public boolean canEnPassant(int newCol, int newRow, int currentRow, int direction) {
        Optional<StackPane> maybeEnPassantablePiece = this.board.getGameStateController().getEnPassantablePiece();
        if (maybeEnPassantablePiece.isPresent()) {
            int targetCol = GridPane.getColumnIndex(maybeEnPassantablePiece.get());
            int targetRow = GridPane.getRowIndex(maybeEnPassantablePiece.get());
            if (newRow != currentRow && newCol == targetCol && newRow == targetRow + direction) {
                return true;
            }
        }
        return false;
    }
}
