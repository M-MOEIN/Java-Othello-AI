import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

final public class AI {
	static final int minimaxDepth = 4;
	private static final double [][] valueBoard = { 
	            {120, -20 , 20 , 5}
	            ,{-20, -40 ,-5 , -5}
	            ,{ 20, -5 , 15 , 3 }
	            ,{ 5 , -5 , 3 , 3} };
	
	
	static double getValue(int row , int col) { /**uses valueBoard to return value of valueBoard[row][col]*/
		if ( row > 3)
	        row = 7 - row;
	    if ( col > 3 )
	        col = 7 - col;
	    return valueBoard[row][col];
	}
	
	public static void evaluatingFunction( Board gameBoard  ) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		findPossibleMoves(possibleMoves, gameBoard);
		if (possibleMoves.isEmpty())  /**means there's no possible moves available*/
			return;
		
		
		if ( gameBoard.getDEPTH() == 0) {  
			for (Iterator<Move> iterator = possibleMoves.iterator(); iterator.hasNext();) {
				Move move =  iterator.next();
				if ( gameBoard.bestMove == null || move.getReward()  > gameBoard.bestMove.getReward() ) /**using single layered MiniMax algorithm */
					gameBoard.bestMove = move;
			}
			return;
		}
		
		
		int i = 0;
		for ( i = 0 ; i< possibleMoves.size() ; i++) { /**sets children for Board for each possible move*/
			gameBoard.children.add( new Board( Board.copyBoard(gameBoard.myBoard) , gameBoard.getDEPTH() , gameBoard.getColor() ) );
			gameBoard.children.get(i).reduceDepth();
			AI.applyChanges(possibleMoves.get(i), gameBoard.children.get(i)); /** changes copyBoard for playing "move" */ 
			gameBoard.children.get(i).start(); /**initiating the board to find bestMove for Board's children */
		}
		
		i = 0;
		
		for (Iterator<Board> iterator = gameBoard.children.iterator(); iterator.hasNext();) {	
			Board tempB = iterator.next();
			try {
				tempB.join();
			}catch (InterruptedException e) { System.out.println("Interrupted Error Accured");}
			
			try {
			if ( gameBoard.bestMove == null || possibleMoves.get(i).getReward() - tempB.bestMove.getReward() > gameBoard.bestMove.getReward() ) /**using Minimax algorithm  our reward for the move - opponent reward for his move*/
				gameBoard.bestMove = possibleMoves.get(i).Clone();
			}catch(NullPointerException e) { System.out.println("Exception Accured Where You Don't Like"); }
		}
	}
	
	public static void applyChanges(Move move , Board gameBoard ) {
		
		gameBoard.myBoard[move.getRow()][move.getcol()].piece = new OthelloPiece( gameBoard.getColor() );
		
		for (int i = -1; i<= 1; i++)
	        for (int j= -1; j<=1 ; j++){
	            int tempRow = move.getRow();
	            int tempCol = move.getcol();
	            if (i==j && i == 0)
	                continue;
	            while ( tempRow+i>=0 && tempRow+i<8 &&  tempCol+j>=0 && tempCol+j<8 ){
	                tempCol += j;
	                tempRow += i;
	                if (  gameBoard.myBoard[tempRow][tempCol].piece == null  )
	                    break;

	                if ( gameBoard.myBoard[tempRow][tempCol].piece.getColor() == gameBoard.getColor() ){
	                    while (tempCol != move.getcol() || tempRow != move.getRow()){
	                    	
	                    	gameBoard.myBoard[tempRow][tempCol].piece.setColor(gameBoard.getColor());
	                    	
	                    	tempCol -= j;
	                        tempRow -= i;
	                        
	                    }
	                    break;
	                }                
	                
	            }
	        }
		
		gameBoard.switchColor();
	}
	
	public static void findPossibleMoves(ArrayList<Move> possibleMoves  , Board gameBoard) {
		possibleMoves.clear();
		for (int i=0; i<8; i++)
	        for (int j=0; j<8 ; j++)
	            if ( gameBoard.myBoard[i][j].piece != null && gameBoard.myBoard[i][j].piece.getColor() == gameBoard.getColor())
	            	checkMoveValidity( i , j , possibleMoves , gameBoard) ;

	}
	private static void  checkMoveValidity(int row , int col , ArrayList<Move> possibleMoves ,  Board gameBoard) {
		
		Color opositColor = gameBoard.myBoard[row][col].piece.getColor().getOpositColor();
		
		for (int i = -1; i<= 1; i++)
	        for (int j= -1; j<=1 ; j++){
	            int tempRow = row;
	            int tempCol = col;
	            int flag = 0;
	           
	            while ( tempRow+i>=0 && tempRow+i<8 && tempCol+j>=0 && tempCol+j<8 ){
	                tempCol+=j;
	                tempRow+=i;

	                if ( gameBoard.myBoard[tempRow][tempCol].piece != null && gameBoard.myBoard[tempRow][tempCol].piece.getColor() == opositColor )
	                    flag = 1;
	                
	                if ( gameBoard.myBoard[tempRow][tempCol].piece != null && gameBoard.myBoard[tempRow][tempCol].piece.getColor() == gameBoard.getColor() ) /**considering rules of Othello, this square can't be a valid move */
	                    break;

	                if ( ( gameBoard.myBoard[tempRow][tempCol].piece == null && flag == 0 ) )  //   || possibleMoves.contains(new Move[tempRow][tempCol]) )
	                    break;
	                if (  gameBoard.myBoard[tempRow][tempCol].piece == null && flag == 1){/**if its a valid move, it'll add this move to ArrayList*/
	                	possibleMoves.add(new Move(tempRow ,  tempCol , gameBoard));
	                    break;
	                }
	            }
	            
	        }

	}
	
	
	public static Board loadFile (String filePath) {
		/**in saving file, black = 1 , white = 2 , empty squares = 0, at end the last character represents turn */
		Board board = null;
		SquarePiece myBoard[][] = new SquarePiece[8][8];
		Color turn = null;
		Scanner reader;
		try {
			reader = new Scanner(new File(filePath));
			
		}catch( Exception e) {
			System.out.println("File not found");
			return board;
		}
		String temp = "";
		for (int i = 0; i < 9 ; i++ ) {
		
			try {
				temp = reader.next();	
			}catch( NoSuchElementException e) {
				System.out.println("Error in File loading ");
			}
			
			for (int j = 0; j < 8 ; j++ ) {
				char c = temp.charAt(j);
				
				if ( i == 8 ) {
					switch (c) {
					case '1':
						turn = Color.BLACK ;
						break;
					case '2':
						turn = Color.WHITE ;
						break;
					}
					break;
				}
				
				switch (c) {
				case '0':
					myBoard[i][j] = new SquarePiece();
					break;
				case '1':
					myBoard[i][j] = new SquarePiece(new OthelloPiece(Color.BLACK ));
					break;
				case '2':
					myBoard[i][j] = new SquarePiece(new OthelloPiece(Color.WHITE ));
					break;

				}
				
			}
		}
		
		
		board = new Board( myBoard , AI.minimaxDepth , turn );
		reader.close();
		return board;
	}
	public static void printBoard(Board b) {
		System.out.println();
		for (int i = 0; i < 8 ; i++ , System.out.println() ) {
			for (int j = 0; j < 8 ; j++ ) {
				char r; 
				if (b.myBoard[i][j].piece == null)
					r = '0';
				else
					r = (b.myBoard[i][j].piece.getColor() == Color.WHITE) ? '2':'1';
				System.out.print(r+" ");
			}
		}
		if (b.getColor() == Color.WHITE)
			System.out.print("2");
		else
			System.out.print("1");

	}
	
	public static void saveBoard(Board b , String filePath) {
		Formatter f = null;
		try {
			f = new Formatter(filePath);
		}catch(FileNotFoundException e) {
			System.out.println("FILE NOT FOUND ! ! !");  
			return; 
		}

		for (int i = 0; i < 8 ; i++ , f.format("\n") ) {
			for (int j = 0; j < 8 ; j++ ) {
				char r; 
				if (b.myBoard[i][j].piece == null)
					r = '0';
				else
					r = (b.myBoard[i][j].piece.getColor() == Color.WHITE) ? '2':'1';
				f.format("%c", r);
			}
		}
		if (b.getColor() == Color.WHITE)
			f.format("2");
		else
			f.format("1");
		
		f.close();
	}

}


