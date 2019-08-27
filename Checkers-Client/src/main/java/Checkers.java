import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Checkers extends Application implements MoveListener {

    private boolean isServer = true; //user is a (true) host or (false) guest
    
    //uses a ternary operation to create a host or guest session on start up
    //private NetworkConnection connection = isServer? createServer(): createClient();
    
    private NetworkConnection connection; 
    
    public static String gHostIpAddress, gHostPortNumber;

    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tiles = new Group();
    private Group pieces = new Group();
    private MoveValidator validator = new MoveValidator();
    private boolean turn = isServer;
    private String color = isServer? "b": "r";
    
    //public TextFields for button access
  	public TextField textFieldHostIP = new TextField();
  	public TextField textFieldHostPort = new TextField();
  	private String s, p;
  	Stage window;
	Scene sceneLauncher, sceneCheckers;

    private Parent createContent(){
    	/* creates initial checkerboard w/ tiles
    	 */
    	//System.out.print("isServer: " + this.isServer);
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
        Tile target = get_tile(x, y);
        Piece piece = target.get_piece();
        target.set_piece(null);
        piece.setVisible(false);
    }

    

    @Override
    public void start(Stage primaryStage) throws Exception{
    	/* required JavaFX call to set the current scene
    	 */
    	this.window = primaryStage;
    	/********************************
    	 * Launcher scene 
    	 ********************************/

		//set up menuBar
		MenuBar mb = new MenuBar();
		//create menu(s)
		Menu menu1 = new Menu("File");
		//create menuItems
		MenuItem m1File = new MenuItem("Exit Application");
		menu1.getItems().add(m1File);
		
		//event handler for menuItems
		EventHandler<ActionEvent> menuEvent = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String mText = ((MenuItem)e.getSource()).getText();
				//define menu option behavior via if else
				if(mText.equals("Exit Application")) {
					System.out.println("Application Closed");
					System.exit(0);
				}
			}	
        };
		//add events to menuItems
		m1File.setOnAction(menuEvent);
		
		//create buttons
		Button button1 = new Button("Host Checkers Game");
		Button button2 = new Button("Join Checkers Game");
		Button button3 = new Button("View Information");
		Button button4 = new Button("Exit");
		
		//add button listeners
		button1.setOnAction(action -> {
			//button1 - host a game
			/* tells the program that you want the server.
			 * sets connection information and attempts to
			 * start a connection. 
			 */
			this.isServer = true;
			this.turn = this.isServer;
			this.connection = this.createServer();	
			
			//checkers scene is set so that createContent() isn't called early or duplicated
			this.sceneCheckers = new Scene(createContent());
			window.setScene(this.sceneCheckers);
			//will open a new window showing Host network info
			this.createHostInfoWindow();
			Platform.runLater(()->{
	            try {
	                connection.startConnection();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
			connection.addListener(this);
		});
		button2.setOnAction(action -> {
			//button2 - join a game
			/* tells the program that you want a client
			 * sets connection information and attempts to
			 * start a connection. 
			 */
			this.isServer = false;
			this.turn = this.isServer;
			this.connection = 
					this.createClientViaLauncer(this.textFieldHostIP.getText(), Integer.parseInt(this.textFieldHostPort.getText()));
			
			//checkers scene is set so that createContent() isn't called early or duplicated
			this.sceneCheckers = new Scene(createContent());
			window.setScene(this.sceneCheckers);
			Platform.runLater(()->{
	            try {
	                connection.startConnection();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
			this.createClientInfoWindow();
			connection.addListener(this);
		});
		button3.setOnAction(action -> {
			//button3 - View Application information
			this.createGeneralInfoWindow();
		});
		button4.setOnAction(action -> {
			//button4 - Exit game
			System.exit(0);
		});
		
		mb.getMenus().add(menu1);
		
		Label label1 = new Label("Host a game:");
		Label label2 = new Label("\nJoin a game - ");
		Label label3 = new Label("Enter Host's IP address:");
		Label label4 = new Label("Enter Hosts's Port number: ");
		
		VBox vb = new VBox(mb,
				label1,
				button1,
				label2,
				label3,
				this.textFieldHostIP,
				label4,
				this.textFieldHostPort, 
				button2, 
				new Label("\n"),
				button3, 
				button4
		);	
		this.sceneLauncher = new Scene(vb);
		
		//this will duplicate the scene
		//this.sceneCheckers = new Scene(createContent());
		
        window.setScene(this.sceneLauncher);
        window.setTitle("Checkers");
        window.show();
//        connection.send_data("WORKING IT IS MERLNI");
    }
    
    public void createHostInfoWindow() {
    	//create a new window for host information
    	Stage stageNetworkInfo = new Stage();
    	stageNetworkInfo.setTitle("Host Information");
    	Label labelHost = new Label("Host IP Address:");
    	//display information in textfields for easy copy and paste
    	TextField textFieldHost = new TextField(Checkers.gHostIpAddress);
    	textFieldHost.setEditable(false);
    	Label labelPort = new Label("Host Port:");
    	TextField textFieldPort = new TextField(Checkers.gHostPortNumber);
    	textFieldPort.setEditable(false);
    	VBox vbox = new VBox(labelHost, 
    			textFieldHost, 
    			labelPort, 
    			textFieldPort
    			);
    	Scene sceneInfo = new Scene(vbox);
    	stageNetworkInfo.setScene(sceneInfo);
    	stageNetworkInfo.setHeight(125);
    	stageNetworkInfo.setWidth(270);
    	stageNetworkInfo.setResizable(false);
    	stageNetworkInfo.show();
    }
    
    public void createClientInfoWindow() {
    	//create a window displaying info for when client joins a game
    	Stage stageClientInfo = new Stage();
    	stageClientInfo.setTitle("Client Information");
    	Label label = new Label("Joined game at " + this.s);
    	Label label1 = new Label("Using port: " + this.p);
    	
    	VBox vbox = new VBox(label, label1);
    	Scene scene = new Scene(vbox);
    	stageClientInfo.setScene(scene);
    	stageClientInfo.setHeight(125);
    	stageClientInfo.setWidth(270);
    	stageClientInfo.setResizable(false);
    	stageClientInfo.show();
    }
    
    public void createGeneralInfoWindow(){
    	//create a new info to view app information (help and/or credits)
    	String info = 
    			"This application allows two people to play a game of Checkers together."
    			+ "\n\nTo move pieces, click and drag your pieces to the desired space."
    			+ "\nIf you place a piece over an occupied space or backwards when it's not a king, "
    			+ "then the piece will return to its original position."
    			+ "\n\nThe game ends when one of the players wins.";
    	Stage infoStage = new Stage();
    	Label label1 = new Label(info);
    	label1.setWrapText(true);
    	label1.setPrefWidth(395);
    	
    	VBox vbox = new VBox(label1);
    	Scene scene = new Scene(vbox);
    	
    	infoStage.setScene(scene);
    	infoStage.setTitle("Information");
    	infoStage.setHeight(200);
    	infoStage.setWidth(400);
    	infoStage.setResizable(false);
    	infoStage.show();
	}

    public MyClient createClient(){
    	/* method for creating a client using
    	 * console input for IP and port values
    	 */
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
    
    public MyClient createClientViaLauncer(String host, int port) {
    	this.isServer = false;
    	/* method to make it easier to create
    	 * a client via launcher, to read in
    	 * the host IP and port number at once.
    	 */
    	this.s = host;
    	this.p = Integer.toString(port);
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
    	this.isServer = true;
    	/* Return a server with a 
    	 * default port value (first arg)
    	 */
        return new MyServer(55555, data -> {
            Platform.runLater(() -> {

            });
        });
    }

    public void toggleCanMove(){
        for(int i = 0; i < this.pieces.getChildren().size(); i++){
            Piece cur = (Piece) pieces.getChildren().get(i);
            String pieceColor = cur.get_type().getColor();
            if(pieceColor.equals(color)){
                cur.setCanMove(turn);
            }
        }
    }

public void checkAndSetKing(String player, int yend, Piece piece) {
    if (player.contains("b") && yend == 0) {
        piece.setKing(true);
    } else if (player.contains("r") && yend == 7) {
        piece.setKing(true);
    }
}

public void isWin() {
    int blackPieces = 0;
    int redPieces = 0;
    for (int i = 0; i < this.pieces.getChildren().size(); i++) {
        Piece cur = (Piece) pieces.getChildren().get(i);
        String pieceColor = cur.get_type().getColor();
        Boolean isVisible = cur.isVisible();
        if (pieceColor.equals("b")) {
            if (isVisible) {
                blackPieces += 1;
            }
        } else {
            if (isVisible) {
                 redPieces += 1;
            }

        }
    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Checkers");
    alert.setHeaderText("Game Ended");
    if (redPieces == 0 || blackPieces == 0) {
        if (isServer && redPieces == 0) {
            System.out.println("BLACK WON!");
            alert.setContentText("BLACK WINS!");
            alert.showAndWait();
        }
        if (isServer && blackPieces == 0) {
            System.out.println("RED WINS!");
            alert.setContentText("RED WINS!");
            alert.showAndWait();
        }
        if (!isServer && blackPieces == 0) {
            System.out.println("RED WINS!");
            alert.setContentText("RED WINS!");
            alert.showAndWait();
        }
        if (!isServer && redPieces == 0) {
            System.out.println("BLACK WON!");
            alert.setContentText("BLACK WINS!");
            alert.showAndWait();
        }
    }
}

    public boolean checkMove(MoveMessage move) {
        if(validator.validateMove(move)){
            validator.applyMove(move);
            validator.displayBoard();
            if(turn){
                Piece piece = get_tile(move.xstart, move.ystart).get_piece();
                piece.move_piece(move.xend, move.yend);
                Tile start = get_tile(move.xstart, move.ystart);
                start.set_piece(null);
                Tile end = get_tile(move.xend, move.yend);
                end.set_piece(piece);
                checkAndSetKing(move.player, move.yend, piece);
                try {
                    connection.send(move);
                } catch (Exception e){
                    System.out.println(e);
                }
                turn = !turn;
            } else {
                Piece piece = get_tile(move.xstart, move.ystart).get_piece();
                piece.move_piece(move.xend, move.yend);
                Tile start = get_tile(move.xstart, move.ystart);
                start.set_piece(null);
                Tile end = get_tile(move.xend, move.yend);
                end.set_piece(piece);
                checkAndSetKing(move.player, move.yend, piece);
                turn = !turn;
            }
            if(move.jump){
                remove_piece(move.xjump, move.yjump);
            }
            toggleCanMove();
            isWin();
            return true;
        }
        return false;
    }
    public static void main(String[] args){ 
        launch(args);
    }

}
