public class Move implements Cloneable {
	private int row;
	private int col;
	private double reward;
	Move (int row , int col , Board gameBoard){
		this.row = row;
		this.col = col;
		setReward(gameBoard);
	}
	public Move ( ){  }
	public Move(int r , int c) {
		row = r;
		col = c;
	}
	public int getRow() { return row; }
	public int getcol() { return col; }
	
	public double getReward() { return this.reward; }
	public void setReward(double r) { reward = r; }
	private double setReward( Board gameBoard ) {
	    for (int i = -1; i<= 1; i++)
	        for (int j= -1; j<=1 ; j++){
	            int tempRow = row;
	            int tempCol = col;
	            if (i==j && i == 0)
	                continue;
	            while ( tempRow+i>=0 && tempRow+i<8 &&  tempCol+j>=0 && tempCol+j<8 ){
	                tempCol += j;
	                tempRow += i;
	                if (  gameBoard.myBoard[tempRow][tempCol].piece == null )   //|| AI.Board[tempRow][tempCol] == '*')
	                    break;
	               
	                if ( gameBoard.myBoard[tempRow][tempCol].piece.getColor() == gameBoard.getColor() ){
	                    while (tempCol != col || tempRow != row){
	                        tempCol -= j;
	                        tempRow -= i;
	                       
	                        if (tempRow != row || tempCol != col)
	                            reward += AI.getValue(tempRow, tempCol);
	                        
	                    }
	                    break;
	                }      
	                
	                
	            }
	        }
	    return reward;
	}
	
	public String toString () {
		String result = row + "  " + col + "\n";
		return result;
	}
	
	public Move Clone() {
		Move m = new Move(this.getRow() , this.getcol() );
		m.setReward(this.getReward());
		return m;
	}
	public boolean compareByCoordinates(Move m) {
		if ( m.getcol() == this.col && m.getRow() == this.row)
			return true;
		return false;
	}
}
