import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Piece extends StackPane {

    private List<MoveListener> listeners = new ArrayList<MoveListener>();

    public static final int TILE_SIZE = 100;
    private PieceType type;
    private double offsetX, offsetY;
    private double currX, currY;
    private boolean canMove;

    public Piece(PieceType type, int x, int y, boolean isServer){
        this.type = type;
        if(type == PieceType.DARK){
            if(isServer)
            canMove = true;
            else
            canMove = false;
        }
        if(type == PieceType.LIGHT){
            if(isServer)
                canMove = false;
            else
                canMove = true;
        }
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

        if(canMove) {
            setOnMousePressed(e -> {
                offsetX = e.getSceneX();
                offsetY = e.getSceneY();
            });

            setOnMouseDragged(e -> {
                relocate(e.getSceneX() - offsetX + currX, e.getSceneY() - offsetY + currY);
            });

            setOnMouseReleased(e -> {
//            currX = e.getSceneX()- e.getX();
//            currY = e.getSceneY() - e.getY();
                String player = type.getColor();
                int ystart = (int) (offsetY) / 100;
                int xstart = (int) (offsetX) / 100;
                int xend = (int) (e.getSceneX()) / 100;
                int yend = (int) (e.getSceneY()) / 100;

                System.out.println(xstart);
                System.out.println(ystart);
                System.out.println(xend);
                System.out.println(yend);

                MoveMessage move = new MoveMessage(player, xstart, ystart, xend, yend);

                for (MoveListener hl : listeners)
                    //TODO FIX THE values for xstart, ystart, xend, yend
                    if (hl.checkMove(move)) {
                        currX = e.getSceneX() - e.getX();
                        currY = e.getSceneY() - e.getY();
                    } else {
                        relocate(currX, currY);
                    }
            });
        }

    }

    public void move_piece(double x, double y) {
        currX = x * TILE_SIZE;
        currY = y * TILE_SIZE;
        relocate(currX, currY);
    }

    public PieceType get_type() {
        return type;
    }

    public void setCanMove(boolean bool){
        canMove = bool;
    }

    public void addListener(MoveListener toAdd) {
        listeners.add(toAdd);
    }
}
