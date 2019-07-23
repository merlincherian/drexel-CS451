import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Checkers extends Application {

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tiles = new Group();
    private Group pieces = new Group();

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE);
        root.getChildren().addAll(tiles, pieces);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);

                tiles.getChildren().add(tile);
                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = create_piece(PieceType.LIGHT, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = create_piece(PieceType.DARK, x, y);
                }

                if (piece != null) {
                    tile.set_piece(piece);
                    pieces.getChildren().add(piece);
                }

            }
        }

        return root;
    }

    private Piece create_piece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);
        return piece;
    }

    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("My checkers game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
