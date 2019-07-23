import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {

    public static final int TILE_SIZE = 100;
    private PieceType type;
    private double offsetX, offsetY;
    private double currX, currY;

    public Piece(PieceType type, int x, int y){
        this.type = type;
        move_piece(x, y);

        Ellipse shadow = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        shadow.setFill(Paint.valueOf("#000"));

        shadow.setStroke(Paint.valueOf("#000"));
        shadow.setStrokeWidth(TILE_SIZE * 0.03);

        shadow.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        shadow.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        Ellipse coin = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        coin.setFill(type == PieceType.DARK
                ? Paint.valueOf("#410D06") : Paint.valueOf("#D7181B"));

        coin.setStroke(Paint.valueOf("#000"));
        coin.setStrokeWidth(TILE_SIZE * 0.03);

        coin.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        coin.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(shadow, coin);

        setOnMousePressed(e -> {
            offsetX = e.getSceneX();
            offsetY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - offsetX + currX, e.getSceneY() - offsetY + currY);
        });

        setOnMouseReleased(e -> {
            currX = e.getSceneX()- e.getX();
            currY = e.getSceneY() - e.getY();
        });

    }

    public void move_piece(double x, double y){
        currX = x * TILE_SIZE;
        currY = y * TILE_SIZE;
        relocate(currX, currY);
    }

    public PieceType get_type() {
        return type;
    }

}
