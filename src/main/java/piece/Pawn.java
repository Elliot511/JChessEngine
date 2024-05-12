package piece;

import org.example.BoardDisplay;

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
}
