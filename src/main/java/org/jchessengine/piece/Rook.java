package org.jchessengine.piece;

import org.jchessengine.BoardDisplay;

public class Rook extends Piece {

    public Rook(BoardDisplay board, int col, int row, boolean isWhite) {
        super(board);
        this.name = "Rook";
        this.value = 5;
        this.isWhite = isWhite;
        this.col = col;
        this.row = row;

        this.sprite = getSprite(4 * spriteMapScale, isWhite ? 0 : spriteMapScale, spriteMapScale, spriteMapScale);
    }
}
