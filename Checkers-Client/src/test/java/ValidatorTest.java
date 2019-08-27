package test.java;

import org.junit.Test;
import sun.plugin2.message.OverlayWindowMoveMessage;


public class ValidatorTest {
    private MoveValidator mv = new MoveValidator();


    @Test
    public void testSimpleMove(){
        MoveMessage move = new MoveMessage("b", 0, 5, 1, 4);
        assertTrue(mv.validateMove(move));
        assertTrue(move.valid);
    }

    @Test
    public void testJump(){
        mv.applyMove(new MoveMessage("b", 2,5,3,4));
        mv.applyMove(new MoveMessage("r", 1,2,2,3));
        MoveMessage move = new MoveMessage("b",3,4,1,2 );
        assertTrue(mv.validateMove(move));
        assertTrue(move.valid && move.jump);
        assertEquals(move.xjump == 2 && move.yjump ==3);
    }
}
