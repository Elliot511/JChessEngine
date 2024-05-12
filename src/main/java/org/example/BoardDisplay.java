package org.example;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import piece.*;

import java.util.ArrayList;

public class BoardDisplay {

    private static final Logger LOGGER = LogManager.getLogger(BoardDisplay.class);

    private final int tileSize = 10000;

    private ArrayList<Piece> pieces = new ArrayList<>();

    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = new Rectangle(Math.sqrt(tileSize), Math.sqrt(tileSize),
                        (row + col) % 2 == 0 ? Color.BEIGE : Color.BURLYWOOD);
                gridPane.add(square, col, row);
            }
        }
        Scene scene = new Scene(gridPane, 800, 800);
        primaryStage.setTitle("JChessEngine");
        primaryStage.setScene(scene);
        primaryStage.show();
        spawnPieces(gridPane);
    }

    private void spawnPieces(GridPane gridPane) {
        pieces.add(new Rook(this, 0, 0, false));
        pieces.add(new Rook(this, 0, 7, true));
        pieces.add(new Rook(this, 7, 0, false));
        pieces.add(new Rook(this, 7, 7, true));

        pieces.add(new Knight(this, 1, 0, false));
        pieces.add(new Knight(this, 1, 7, true));
        pieces.add(new Knight(this, 6, 0, false));
        pieces.add(new Knight(this, 6, 7, true));

        pieces.add(new Bishop(this, 2, 0, false));
        pieces.add(new Bishop(this, 2, 7, true));
        pieces.add(new Bishop(this, 5, 0, false));
        pieces.add(new Bishop(this, 5, 7, true));

        pieces.add(new Queen(this, 3, 0, false));
        pieces.add(new Queen(this, 3, 7, true));
        pieces.add(new King(this, 4, 0, false));
        pieces.add(new King(this, 4, 7, true));

        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(this, i, 1, false));
            pieces.add(new Pawn(this, 7 - i, 6, true));
        }

        for (Piece piece : pieces) {
            gridPane.add(piece.spawn(), piece.getCol(), piece.getRow());
        }
    }

    public int getTileSize() {
        return tileSize;
    }

}
