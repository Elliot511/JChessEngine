package piece;

import org.example.BoardDisplay;

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



}
