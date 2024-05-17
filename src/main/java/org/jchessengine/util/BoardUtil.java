package org.jchessengine.util;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jchessengine.GameStateController;
import org.jchessengine.piece.Pawn;
import org.jchessengine.piece.Piece;

import java.util.Optional;

public final class BoardUtil {

    private BoardUtil() {
        // no-op
    }

    public static void paintSelectedTile(Rectangle tile, Color color) {
        tile.setFill(color);
    }

    public static void unpaintSelectedTile(Rectangle tile, int col, int row) {
        tile.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BURLYWOOD);
    }

    public static boolean firstPawnMovePerformed(StackPane selectedTile, StackPane newTile) {
        ImageView piece = StackPaneUtil.getPieceFromTile(selectedTile);
        int direction = ((Piece) piece.getUserData()).isWhite() ? -1 : 1;
        return GridPane.getRowIndex(newTile) == GridPane.getRowIndex(selectedTile) + (direction * 2);
    }

    public static void checkAndSetBecomesEnPassantable(GameStateController gameStateController, StackPane newTile,
            StackPane selectedTile, ImageView pieceImg) {
        Piece piece = (Piece) pieceImg.getUserData();
        if (piece instanceof Pawn) {
            if (BoardUtil.firstPawnMovePerformed(selectedTile, newTile)) {
                gameStateController.setEnPassantablePiece(Optional.of(newTile));
                return;
            }
        }
        if (gameStateController.getEnPassantablePiece().isPresent() &&
                ((Pawn) StackPaneUtil.getPieceFromTile(gameStateController.getEnPassantablePiece()
                        .get()).getUserData()).isWhite() == piece.isWhite()) {
            gameStateController.setEnPassantablePiece(Optional.empty());
        }
    }

    public static void captureEnPassantedPieceIfPossible(ImageView piece, StackPane newTile,
            GameStateController gameStateController) {
        if (piece.getUserData() instanceof Pawn && gameStateController.getEnPassantablePiece().isPresent()) {
            StackPane enPassantable = gameStateController.getEnPassantablePiece().get();
            int direction = ((Piece) piece.getUserData()).isWhite() ? -1 : 1;
            if (GridPane.getRowIndex(newTile) - direction == GridPane.getRowIndex(enPassantable)
                    && GridPane.getColumnIndex(newTile).equals(GridPane.getColumnIndex(enPassantable))) {
                StackPaneUtil.removePieceFromTile(enPassantable);
                gameStateController.setEnPassantablePiece(Optional.empty());
            }
        }

    }

}
