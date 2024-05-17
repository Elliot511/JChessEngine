package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Optional;

/**
 * Holds important information about the current game.
 */
public class GameStateController {

    private Optional<StackPane> enPassantablePiece = Optional.empty();

    private boolean isWhitesTurn = true;

    public void setEnPassantablePiece(Optional<StackPane> enPassantablePiece) {
        this.enPassantablePiece = enPassantablePiece;
    }

    public void setWhitesTurn(boolean whitesTurn) {
        isWhitesTurn = whitesTurn;
    }

    public Optional<StackPane> getEnPassantablePiece() {
        return enPassantablePiece;
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }
}
