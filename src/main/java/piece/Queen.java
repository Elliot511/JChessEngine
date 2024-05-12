package piece;

import org.example.BoardDisplay;

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
}
