public class MoveValidator {
    private String[][] board = new String[8][8];

    public MoveValidator(){
        initBoard();
    }

    /*
        Helper to fill a row with a given player color
        @param row, the row of a board to fill
        @param color, the color to fill spaces with "r" or "w"
        @param start, the integer value of where in the row to start filling
        @return a String[] which has every other place filled in with color starting from start
     */
    private String[] buildRow(String[] row, String color, int start){
        //
        String[] tmprow = row;
        for(int i = start; i < 8; i+=2){
            tmprow[i] = color;
        }
        return tmprow;
    }

    /*
        Initialize the String[][] board with "w" and "r" pieces to the basic starting board
     */
    private void initBoard(){
        for(int i = 0; i < 3; i++){
            board[i] = buildRow(board[i],"w",(i + 1) % 2 );
            board[7-i] = buildRow(board[7-i],"r", i %2);
        }
    }

    /*
        Check if move is diagonal on the board, in order to be diagonal the absolute value of change in x and change in
        y must be equal to one another.
        @param xstart, x value where move is starting from
        @param ystart, y value where move is starting from
        @param xend, x value where move is going to
        @param yend, y value where move is going to
        @return true if positions are diagonal else false
     */
    private boolean diagonal(int xstart, int ystart, int xend, int yend){
        if(Math.abs(xstart - xend) == Math.abs(ystart - yend)){
            return true;
        }
        return false;
    }

    /*
        Check if a jump is legal, meaning that the target square must be empty and the middle square must have the
        opposite color from the player.
        @param player, "r" or "w" for who is moving
        @param xstart, x value where move is starting from
        @param ystart, y value where move is starting from
        @param xend, x value where move is going to
        @param yend, y value where move is going to
        @return true if jump is legal else false
     */
    private boolean checkJump(String player, int xstart, int ystart, int xend, int yend){
        int xdir = (xend - xstart) / 2;
        int ydir = (yend - ystart) / 2;

        String op;

        //Get opposite player
        if(player.equals("r")){
            op = "w";
        } else {
            op = "r";
        }

        String midPoint = this.board[yend + ydir][xend + xdir];
        if(midPoint != null){
            return midPoint.equals(op);
        }
        return false;
    }

    /*
        Main funciton that handles calling helpers and quick checks to validate a move by player
        @param player, "r" or "w" for who is moving
        @param xstart, x value where move is starting from
        @param ystart, y value where move is starting from
        @param xend, x value where move is going to
        @param yend, y value where move is going to
        @return true if none of the branches return false
     */
    public boolean validateMove(String player, int xstart, int ystart, int xend, int yend){
        //If starting space is null fail move attempt
        if(this.board[ystart][xstart] == null){
            return false;
        }


        //If player doesn't have a piece on start square fail move attempt
        if(!this.board[ystart][xstart].equals(player)){
            return false;
        }

        //If target space isn't empty fail move attempt
        if(this.board[yend][xend] != null){
            return false;
        }

        //If move isn't diagonal fail move attempt
        if(!diagonal(xstart, ystart, xend, yend)){
            return false;
        }

        //If jumping check square being jumped for opposite player color
        if(Math.abs(xstart - xend) == 2){
            if(!checkJump(player, xstart, ystart, xend, yend)){
                return false;
            }
        }

        return true;
    }

    /*
        helper to print current board to screen for debugging. prints board with "r", "w" or " ".
     */
    public void displayBoard(){
        for(int i = 0; i < this.board.length; i++){
            String footer = "";
            String line = "";
            for(int j = 0; j < this.board[i].length; j++){
                String space = this.board[i][j];
                if(space == null){
                    space = " ";
                }
                line += "| " + space + "  ";
                footer += " --- ";
            }
            System.out.println(line + "|");
            System.out.println(footer);
        }
    }

    /*
        Displays the x,y coords of any String[][] saved in this.board
     */
    public void displayBoardSpaces(){
        for(int y = 0; y < this.board.length; y++){
            String footer = "";
            String line = "";
            for(int x = 0; x < this.board[y].length; x++){
                line += "| " + x + "," + y + "  ";
                footer += " --- ";
            }
            System.out.println(line + "|");
            System.out.println(footer);
        }
    }
}
