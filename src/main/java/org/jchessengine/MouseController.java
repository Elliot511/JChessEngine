package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jchessengine.util.BoardUtil;
import org.jchessengine.util.StackPaneUtil;

import java.util.Optional;

public class MouseController {

    private static final Logger LOGGER = LogManager.getLogger(MouseController.class);

    private StackPane selectedTile = null;

    public void addClickMove(StackPane stackPane) {
        stackPane.setOnMouseClicked(
                mouseEvent -> {
                    Optional<ImageView> maybePiece = stackPane.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .findAny()
                            .map(ImageView.class::cast);

                    if (maybePiece.isPresent()) { // Piece clicked
                        if (selectedTile == null) { // Piece selected
                            selectedTile = stackPane;
                            BoardUtil.paintSelectedTile(StackPaneUtil.getRectangleFromTile(stackPane));
                            LOGGER.info("Selected piece at col: {} row: {}", GridPane.getColumnIndex(maybePiece.get()),
                                    GridPane.getRowIndex(maybePiece.get()));
                        }
                        else {
                            if (stackPane.equals(selectedTile)) { // Deselecting tile
                                BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                                        GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
                                selectedTile = null;
                                return;
                            }
                            // Capturing piece
                            BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                                    GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
                            capturePiece(stackPane);
                        }
                    }
                    else { // Tile clicked
                        if (selectedTile != null) {
                            BoardUtil.unpaintSelectedTile(StackPaneUtil.getRectangleFromTile(selectedTile),
                                    GridPane.getColumnIndex(selectedTile), GridPane.getRowIndex(selectedTile));
                            movePiece(stackPane);
                        }
                    }
                }
        );
    }

    private void movePiece(StackPane newTile) {
        newTile.getChildren().add(StackPaneUtil.getPieceFromTile(selectedTile));
        StackPaneUtil.removePieceFromTile(selectedTile);
        selectedTile = null;
    }

    private void capturePiece(StackPane newTile) {
        StackPaneUtil.removePieceFromTile(newTile);
        movePiece(newTile);
        selectedTile = null;
    }

}
