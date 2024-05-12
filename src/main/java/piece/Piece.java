package piece;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.BoardDisplay;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Piece {

    private static final Logger LOGGER = LogManager.getLogger(Piece.class);

    String name;

    int value;

    boolean isWhite;

    int col;

    int row;

    Image spriteMap = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("sprites.png")));

    protected int spriteMapScale = (int) (spriteMap.getWidth() / 6);

    ImageView sprite;

    BoardDisplay board;

    public Piece(BoardDisplay board) {
        this.board = board;
    }

    public ImageView spawn() {
        sprite.setFitHeight(spriteMapScale * 0.9);
        sprite.setFitWidth(spriteMapScale * 0.9);
        return sprite;
    }

    ImageView getSprite(int x, int y, int width, int height) {
        PixelReader pixelReader = spriteMap.getPixelReader();
        WritableImage spriteImg = new WritableImage(pixelReader, x, y, width, height);
        ImageView sprite = new ImageView(spriteImg);
        sprite.setSmooth(true);
        return sprite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
