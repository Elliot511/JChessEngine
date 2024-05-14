package org.jchessengine;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class BoardUtil {

    private BoardUtil() {
        // no-op
    }

    public static void paintSelectedTile(Rectangle tile) {
        tile.setFill(Color.CRIMSON);
    }

    public static void unpaintSelectedTile(Rectangle tile, int col, int row) {
        tile.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.BURLYWOOD);
    }

}
