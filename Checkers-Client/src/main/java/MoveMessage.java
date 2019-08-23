import java.io.Serializable;

public class MoveMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean valid = true;
    public String player;
    public int xstart;
    public int ystart;
    public int xend;
    public int yend;
    public boolean jump;
    public int xjump;
    public int yjump;

    public MoveMessage(String player, int xstart, int ystart, int xend, int yend){
        this.player = player;
        this.xstart = xstart;
        this.ystart = ystart;
        this.xend = xend;
        this.yend = yend;
    }

}
