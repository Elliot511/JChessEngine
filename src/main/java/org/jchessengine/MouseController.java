package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MouseController {

    private static final Logger LOGGER = LogManager.getLogger(MouseController.class);

    private StackPane selectedTile = null;

    private Rectangle currentlySelectedTile = null;

    public void addClickMove(StackPane stackPane, int col, int row) {
        stackPane.setOnMouseClicked(
                mouseEvent -> {
                    Optional<ImageView> maybePiece = stackPane.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .findAny()
                            .map(ImageView.class::cast);

                    if (maybePiece.isPresent()) { // Piece clicked
                        if (selectedTile == null) {
                            selectedTile = stackPane;
                            BoardUtil.paintSelectedTile(StackPaneUtil.getRectangleFromTile(stackPane));
                            LOGGER.info("Selected piece at col: {} row: {}", GridPane.getColumnIndex(maybePiece.get()),
                                    GridPane.getRowIndex(maybePiece.get()));
                        }
                        else {
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
