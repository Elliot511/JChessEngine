package org.jchessengine;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public final class StackPaneUtil {

    /**
     * Gets a piece from a given tile.
     *
     * TODO Perhaps I can just get the node at index 1? 0 Should always be the tile (Rectangle)...
     * @param stackPane
     * @return
     */
    public static ImageView getPieceFromTile(StackPane stackPane) {
        return stackPane.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .map(ImageView.class::cast)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No piece to retrieve"));
    }

    /**
     * Removes a piece from the tile.
     *
     * TODO Perhaps I can just get the node at index 1? 0 Should always be the tile (Rectangle)...
     * @param stackPane
     */
    public static void removePieceFromTile(StackPane stackPane) {
        stackPane.getChildren().removeIf(node -> node instanceof ImageView);
    }

    public static Rectangle getRectangleFromTile(StackPane stackPane) {
        return stackPane.getChildren().stream()
                .filter(node -> node instanceof Rectangle)
                .map(Rectangle.class::cast)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No rectangle to retrieve"));
    }

}
