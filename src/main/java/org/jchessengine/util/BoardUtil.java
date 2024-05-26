package org.jchessengine.util;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jchessengine.GameStateController;
import org.jchessengine.piece.King;
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

    public static void checkAndCaptureEnPassantableNormally(StackPane newTile, GameStateController gameStateController) {
        if (gameStateController.getEnPassantablePiece().isPresent()) {
            if (newTile.equals(gameStateController.getEnPassantablePiece().get())) {
                gameStateController.setEnPassantablePiece(Optional.empty());
            }
        }
    }

    public static boolean isInBounds(int col, int row) {
        return col >= 0 && col < 8 && row >= 0 && row < 8;
    }

    public static boolean isCastling(StackPane selectedTile, int col, int newCol) {
        if (StackPaneUtil.getPieceFromTile(selectedTile).getUserData() instanceof King) {
            return Math.abs(newCol - col) == 2;
        }
        return false;
    }

    /**
     * Gets the rook that the king is trying to castle with
     * @param piece
     * @param col
     * @param newCol
     * @return returns an array of size two, where index zero is initial tile, and index one is the tile to move to
     */
    public static StackPane[] getCastlingRook(Piece piece, int col, int newCol) {
        StackPane[][] tiles = piece.getBoard().getStackPanes();
        StackPane[] ret = new StackPane[2];
        if (newCol > col) { // Short castle
            ret[0] = tiles[piece.isWhite() ? 7 : 0][7];
            ret[1] = tiles[piece.isWhite() ? 7 : 0][5];
        }
        else { // Long castle
            ret[0] = tiles[piece.isWhite() ? 7 : 0][0];
            ret[1] = tiles[piece.isWhite() ? 7 : 0][3];
        }
        return ret;
    }

}
