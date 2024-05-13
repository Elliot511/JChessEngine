package org.jchessengine.piece;

import org.jchessengine.BoardDisplay;

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
}
