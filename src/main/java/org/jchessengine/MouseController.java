package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jchessengine.piece.Piece;
import org.jchessengine.util.BoardUtil;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public class MouseController {

    GameStateController gameStateController;

    public MouseController(GameStateController gameStateController) {
        this.gameStateController = gameStateController;
    }

    private static final Logger LOGGER = LogManager.getLogger(MouseController.class);

    private StackPane selectedTile = null;

    public void addClickMove(StackPane stackPane) {
        stackPane.setOnMouseClicked(
                mouseEvent -> {
                    Optional<ImageView> maybePiece = stackPane.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .findAny()
                            .map(ImageView.class::cast);

                    if (maybePiece.isPresent()) { // Piece selected, selecting or attacking
                        handleCaptureMove(stackPane, maybePiece);
                    }
                    else { // Tile selected, moving
                        handleRegularMove(stackPane);
                    }
                }
        );
    }

    private void handleCaptureMove(StackPane stackPane, Optional<ImageView> maybePiece) {
        if (selectedTile == null) { // No piece selected prior, selecting new piece
            selectedTile = stackPane;
            BoardUtil.paintSelectedTile(StackPaneUtil.getRectangleFromTile(stackPane), Color.CRIMSON);
            LOGGER.info("Selected piece at col: {} row: {}", GridPane.getColumnIndex(stackPane),
                    GridPane.getRowIndex(stackPane));
        }
        else {
            if (stackPane.equals(selectedTile)) { // Deselecting tile
                cancelMove();
                return;
            }
            // Capturing piece
            BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                    GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
            if (validateMove(stackPane, maybePiece)) {
                BoardUtil.checkAndSetBecomesEnPassantable(gameStateController, stackPane,
                        selectedTile, StackPaneUtil.getPieceFromTile(selectedTile));
                capturePiece(stackPane);
            }
            else {
                cancelMove();
            }
        }
    }

    private void handleRegularMove(StackPane stackPane) {
        if (selectedTile != null) {
            BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                    GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
            if (validateMove(stackPane, Optional.empty())) {
                BoardUtil.checkAndSetBecomesEnPassantable(gameStateController, stackPane,
                        selectedTile, StackPaneUtil.getPieceFromTile(selectedTile));
                movePiece(stackPane, false);
            }
            else {
                cancelMove();
            }
        }
    }

    private void movePiece(StackPane newTile, boolean hasCaptured) {
        ImageView piece = StackPaneUtil.getPieceFromTile(selectedTile);
        newTile.getChildren().add(piece);
        StackPaneUtil.removePieceFromTile(selectedTile);
        if (!hasCaptured) {
            BoardUtil.captureEnPassantedPieceIfPossible(piece, newTile, gameStateController);
        }
        selectedTile = null;
    }

    private void capturePiece(StackPane newTile) {
        BoardUtil.checkAndCaptureEnPassantableNormally(newTile, gameStateController);
        StackPaneUtil.removePieceFromTile(newTile);
        movePiece(newTile, true);
        selectedTile = null;
    }

    private void cancelMove() {
        BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
        selectedTile = null;
    }

    private boolean validateMove(StackPane newTile, Optional<ImageView> maybePiece) {
        ImageView pieceToMove = StackPaneUtil.getPieceFromTile(selectedTile);
        Piece piece = (Piece) pieceToMove.getUserData();
        return piece.validateMove(maybePiece, GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile),
                GridPane.getColumnIndex(newTile), GridPane.getRowIndex(newTile));
    }

}
