package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MouseController {

    private static final Logger LOGGER = LogManager.getLogger(MouseController.class);

    private ImageView selectedPiece = null;

    public void handleTileClicks(Rectangle tile, int col, int row, GridPane gridPane) {
        tile.setOnMouseClicked(
                mouseEvent -> {
                    Optional<ImageView> maybePiece = maybeGetPieceFromTile(gridPane, col, row);
                    if (selectedPiece == null) {
                        maybePiece.ifPresent(imageView -> selectedPiece = imageView);
                    }
                    else {
                        if (maybePiece.isPresent()) {
                            capturePiece(maybePiece.get(), gridPane);
                        }
                        else {
                            movePiece(selectedPiece, col, row);
                        }
                    }
                }
        );
    }

    public void addClickMove(ImageView imageView, GridPane gridPane) {
        imageView.setOnMouseClicked(
                mouseEvent -> {
                    if (selectedPiece == null) {
                        selectedPiece = imageView;
                        LOGGER.info("Selected piece at col: {} row: {}", GridPane.getColumnIndex(imageView),
                                GridPane.getRowIndex(imageView));
                    }
                    else {
                        capturePiece(imageView, gridPane);
                    }
                }
        );
    }

    private void movePiece(ImageView imageView, int newCol, int newRow) {
        GridPane.setColumnIndex(imageView, newCol);
        GridPane.setRowIndex(imageView, newRow);
        selectedPiece = null;
    }

    private void capturePiece(ImageView imageView, GridPane gridPane) {
        movePiece(selectedPiece, GridPane.getColumnIndex(imageView), GridPane.getRowIndex(imageView));
        gridPane.getChildren().remove(imageView);
        LOGGER.info("Piece: {} captured piece: {}", selectedPiece, imageView);
        selectedPiece = null;
    }

    private Optional<ImageView> maybeGetPieceFromTile(GridPane gridPane, int col, int row) {
        return gridPane.getChildren().stream()
                .filter(child -> child instanceof ImageView && GridPane.getColumnIndex(child) == col
                        && GridPane.getRowIndex(child) == row)
                .map(ImageView.class::cast)
                .findFirst();
    }

}
