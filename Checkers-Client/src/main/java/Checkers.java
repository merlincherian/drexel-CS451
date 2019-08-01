import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Checkers extends Application implements MoveListener {

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tiles = new Group();
    private Group pieces = new Group();
    private MoveValidator validator = new MoveValidator();

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
                    piece.addListener(this);
                }

            }
        }
        return root;
//        HBox main = new HBox(root, mycontent());
//        return main;
    }

//    private Parent mycontent(){
//        StackPane root = new StackPane();
//        root.setPrefSize(200, HEIGHT*2);
//        StackPane.setAlignment(root,Pos.CENTER); //set it to the Center Left(by default it's on the center)
//        Button button = new Button("Submit Move");
//        root.setStyle("-fx-background-color: #" + Paint.valueOf("#ACBBB6"));
//        root.getChildren().addAll(button);
//        return root;
//    }

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

    public boolean checkMove(String player, int xstart, int ystart, int xend, int yend) {
        if(validator.validateMove(player, xstart, ystart, xend, yend)){

            validator.applyMove(player, xstart, ystart, xend, yend);
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        launch(args);
    }

}
