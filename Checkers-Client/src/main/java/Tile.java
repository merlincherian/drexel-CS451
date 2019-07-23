import javafx.scene.shape.Rectangle;
import java.awt.*;

public class Tile extends Rectangle {

    private Piece piece;

    public boolean has_piece() {
        return piece != null;
    }

    public Piece get_piece() {
        return piece;
    }

    public void set_piece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(Checkers.TILE_SIZE);
        setHeight(Checkers.TILE_SIZE);

        relocate(x * Checkers.TILE_SIZE, y * Checkers.TILE_SIZE);

        setFill(light ? javafx.scene.paint.Paint.valueOf("#e3cd96") : javafx.scene.paint.Paint.valueOf("#824508"));
    }

}

