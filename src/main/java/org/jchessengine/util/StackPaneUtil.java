package org.jchessengine.util;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public final class StackPaneUtil {

    /**
     * Gets a piece from a given tile.
     *
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
     * @param stackPane
     */
    public static void removePieceFromTile(StackPane stackPane) {
        stackPane.getChildren().removeIf(node -> node instanceof ImageView);
    }

    /**
     * Gets the rectangle object representing the tile from the stackpane
     * @param stackPane
     * @return
     */
    public static Rectangle getRectangleFromTile(StackPane stackPane) {
        return stackPane.getChildren().stream()
                .filter(node -> node instanceof Rectangle)
                .map(Rectangle.class::cast)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No rectangle to retrieve"));
    }

}
