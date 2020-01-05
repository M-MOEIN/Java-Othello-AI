import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class Board extends Thread{
	private int DEPTH; /** Indicates number of layers of MiniMax algorithm to go through*/
	Move bestMove;
	private Color turn;
	SquarePiece myBoard[][] = new SquarePiece[8][8];
	ArrayList<Board> children = new ArrayList<Board>();

	
	
	public Board( SquarePiece board[][] , int depth , Color Turn) {
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++){
			myBoard[i][j] = SquarePiece.cloneSquarePiece(board[i][j]);
		}
		this.setDEPTH(depth);
		this.turn = Turn;
	}
	
	public int getDEPTH() { return DEPTH; }
	public void setDEPTH(int depth) { DEPTH = depth; }
	public Color getColor () { return turn; }
	public void setColor(Color c) { this.turn = c; }
	public void reduceDepth() { this.DEPTH-- ; }
	
	
	public static SquarePiece[][] copyBoard (SquarePiece myBoard[][]) {  /**makes a copy of given array */
		
		SquarePiece BoardCopy[][] = new SquarePiece[8][8];
		
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++){
				BoardCopy[i][j] = SquarePiece.cloneSquarePiece(myBoard[i][j]);
		}
		
		return BoardCopy;
	}
	

	
	public void run() {
		try {
			Thread.sleep(100);  /**delays showing the AI move (delays so player can see his move)*/
		}catch (InterruptedException e) { System.out.println("exception in Drawe");  }

		AI.evaluatingFunction (  this ); /**this method sets bestMove for each Board*/
		
		if (this.bestMove == null ) {
			this.switchColor();
			return;
		}

		AI.applyChanges(this.bestMove, this);

	}
	
	
	public void switchColor() { 
		turn = ( this.turn == Color.BLACK) ? Color.WHITE : Color.BLACK;
	}
	
	public void draw ( GraphicsContext gc) {
		final int  T = 5 ,SIZE = 70 , X= ( (int)gc.getCanvas().getWidth() - SIZE * 8 ) / 2, Y = 30 ;
		
		for (int j = 0; j<8; j++) 
			for (int i = 0; i< 8; i ++) {
				if (this.myBoard[j][i].piece == null )
					continue;
				if ( this.myBoard[j][i].piece.getColor() == Color.WHITE) 
					gc.setFill(javafx.scene.paint.Color.LIGHTGREEN);
				else
					gc.setFill(javafx.scene.paint.Color.DARKSLATEGRAY);
				
				gc.fillOval(X + SIZE * i + T, Y + SIZE * j + T, SIZE - 2 * T, SIZE - 2 * T);
				
			}
		
		gc.setStroke(javafx.scene.paint.Color.DIMGRAY);
		gc.setLineWidth(2);
		
		for( int i = 0 ; i<9; i++) {
			gc.strokeLine(X, Y + i * SIZE , X + SIZE * 8,  Y + i * SIZE );
			gc.strokeLine(X + i * SIZE, Y  , X + i * SIZE,  Y + 8 * SIZE );
		}
		
	}
	
}
