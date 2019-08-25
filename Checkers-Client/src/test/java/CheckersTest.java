import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckersTest {

    @Test
    public void testServer(){
        MyServer server = new MyServer(44444, null);
        assertEquals(server.getPort(), 44444);
        assertEquals(server.getIP(), null);
        assertEquals(server.isServer(), true);
    }

    @Test
    public void testClient(){
        MyClient client = new MyClient("10:184.194.57", 44444, null);
        assertEquals(client.getPort(), 44444);
        assertEquals(client.getIP(), "10:184.194.57");
        assertEquals(client.isServer(), false);
    }

}
