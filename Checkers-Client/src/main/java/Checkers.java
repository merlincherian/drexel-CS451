import javafx.application.Application;
import javafx.application.Platform;
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
import java.util.Scanner;

public class Checkers extends Application implements MoveListener {

    private boolean isServer = false;
    private NetworkConnection connection = isServer? createServer(): createClient();

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tiles = new Group();
    private Group pieces = new Group();
    private MoveValidator validator = new MoveValidator();
    private boolean turn = isServer;
    private String color = isServer? "b": "r";

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE);
        root.getChildren().addAll(tiles, pieces);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tiles.getChildren().add(tile);
                Piece piece = null;
                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = create_piece(PieceType.LIGHT, x, y, isServer);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = create_piece(PieceType.DARK, x, y, isServer);
                }

                if (piece != null) {
                    tile.set_piece(piece);
                    pieces.getChildren().add(piece);
                    piece.addListener(this);
                }

            }
        }
        return root;
    }

    private Piece create_piece(PieceType type, int x, int y, boolean isServer) {
        Piece piece = new Piece(type, x, y, isServer);
        return piece;
    }

    public Tile get_tile(int x, int y){
        return this.board[x][y];
    }


    public void remove_piece(int x, int y){
        Piece piece = get_tile(x, y).get_piece();
        pieces.getChildren().remove(piece);
    }

    @Override
    public void init() throws Exception {
        Platform.runLater(()->{
            try {
                connection.startConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("My checkers game");
        primaryStage.setScene(scene);
        primaryStage.show();
        connection.addListener(this);
//        connection.send_data("WORKING IT IS MERLNI");
    }

    public MyClient createClient(){
        System.out.println("Please enter the ip address given by server");
        Scanner in = new Scanner(System.in);
        String host = in.nextLine();
        System.out.println("Please enter the port number given by server");
        String p = in.nextLine();
        int port = Integer.parseInt(p);
        return new MyClient(host, port, data -> {
            Platform.runLater(() -> {

            });
        });
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    public MyServer createServer(){
        return new MyServer(55555, data -> {
            Platform.runLater(() -> {

            });
        });
    }

    public boolean checkMove(MoveMessage move) {
        if(validator.validateMove(move)){
            validator.applyMove(move);
            validator.displayBoard();
            if(turn){
                try {
                    connection.send(move);
                } catch (Exception e){
                    System.out.println(e);
                }
                turn = !turn;
            } else {
                remove_piece(move.xstart, move.ystart);
                Piece piece;
                if(move.player == "b"){
                    piece = create_piece(PieceType.DARK, move.xend, move.yend);
                } else {
                    piece = create_piece(PieceType.LIGHT, move.xend, move.yend);
                }
                board[move.xend][move.yend].set_piece(piece);
                pieces.getChildren().add(piece);
                //apply other players move to this board
            }
            if(move.jump){
                remove_piece(move.xjump, move.yjump);
            }
            return true;
        }
        return false;
    }


    public static void main(String[] args){
        launch(args);
    }

}
