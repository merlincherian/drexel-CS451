public enum PieceType {

    DARK(1), LIGHT(-1);

    final int dir;

    PieceType(int dir){
        this.dir = dir;
    }

    public String getColor(){
        if (dir == 1) {
            return "b";
        } else{
            return "r";
        }
    }
}
