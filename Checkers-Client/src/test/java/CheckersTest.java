import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

public class CheckersTest {

    @Test
    public void testServer() {
        MyServer server = new MyServer(44444, null);
        assertEquals(server.getPort(), 44444);
        assertEquals(server.getIP(), null);
        assertEquals(server.isServer(), true);
    }

    @Test
    public void testClient() {
        MyClient client = new MyClient("10:184.194.57", 44444, null);
        assertEquals(client.getPort(), 44444);
        assertEquals(client.getIP(), "10:184.194.57");
        assertEquals(client.isServer(), false);
    }


    @Test
    public void testPiece() {
        PieceType type = PieceType.DARK;
        Piece piece = new Piece(type, 3, 5, true);
        assertEquals(piece.get_type(), PieceType.DARK);
        assertEquals("b", type.getColor());

        piece.setCanMove(true);
        List<MoveListener> listeners = new ArrayList<>();
        MoveListener one = null;
        listeners.add(one);
        piece.addListener(one);
        assertEquals(1, listeners.size());
        double x = 3;
        double y = 5;
        piece.move_piece(x, y);
    }

    @Test
    public void testTile() {
        Piece piece = new Piece(PieceType.DARK, 3, 5, true);
        Tile tile = new Tile(true, 5, 5);
        tile.set_piece(piece);
        assertEquals(tile.get_piece(), piece);
        assertTrue(tile.has_piece() == true);
    }

    @Test
    public void testCheckers() {
        Checkers checkers = new Checkers();
        Tile[][] board = new Tile[checkers.WIDTH][checkers.HEIGHT];
        assertTrue(checkers.get_tile(5, 5) == board[5][5]);
    }

    @Test
    public void testMoveMessage() {
        MoveMessage moveMessage = new MoveMessage("b", 4, 4, 5, 5);
        moveMessage.player = "b";
        moveMessage.xstart = 4;
        moveMessage.ystart = 4;
        moveMessage.xend = 5;
        moveMessage.yend = 5;
    }

    @Test
    public void testMoveValidator() {
        MoveMessage moveMessage = new MoveMessage("b", 4, 4, 5, 5);
        MoveValidator mv = new MoveValidator();
        String[][] board = new String[8][8];
        int xstart = 4;
        int ystart = 4;
        int xend = 5;
        int yend = 5;
        mv.isMoveBackwards("b", xstart, ystart, xend, yend);
        assertTrue((yend - ystart) > 0);
        //assertTrue(board[4][4].equals(moveMessage.player));
        mv.validateMove(moveMessage);
        assertTrue(moveMessage.valid == false);
    }


}




