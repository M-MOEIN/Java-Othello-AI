
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class OthelloGraphic extends Application {
	
	public void start(Stage theStage) {
		//Color userColor = Color.WHITE;
		System.out.println("THIS PROGRAM WAS CREATED IN KHORDAD 98\nSTILL HAS SOME ERRORS\nIF YOU PLACE YOUR PIECE, AFTER A.I. MOVE, CLOSE THE PROGRAM AND RUN IT AGAIN.");
    	Group board = new Group();
    	Scene theScene = new Scene(board);
    	theStage.setScene(theScene);
    	theStage.setTitle("Othello Game");
    	
    	Canvas canvas = new Canvas(1000, 800);
    	
    	board.getChildren().add(canvas);
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	theStage.show();
    	
    	final int  SIZE = 70 , X= ( (int)gc.getCanvas().getWidth() - SIZE * 8 ) / 2, Y = 30 ;

    	ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Board myGameBoard = AI.loadFile("board.txt");
		
		theStage.show();
    	theScene.setOnMouseClicked(new EventHandler <javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				int tJ = (int) (arg0.getX() - X);
				int tI = (int) (arg0.getY() - Y);
				
				tI /= SIZE;
				tJ /= SIZE;
				Move playerMove = new Move(tI , tJ);
				AI.findPossibleMoves(possibleMoves, myGameBoard );

				boolean containsPlayerMove = false;
				for ( Move temp : possibleMoves) {
					if ( temp.compareByCoordinates(playerMove) ) { 
						containsPlayerMove = true;
						AI.applyChanges(playerMove, myGameBoard);/**moves as user ordered*/
						break;
					}
				}
				
				if (containsPlayerMove == false)  /**selected move is invalid so we wait for another input*/
					return;
				    	
				BoardDrawer drawer = new BoardDrawer(gc, myGameBoard);
				drawer.run();
					
				try {
					myGameBoard.start();
				}catch (Exception e) { System.out.println("\nError Accured in initiating myGameBoard");  }  
				
			}
        });
    	
    	new AnimationTimer() {
			@Override
			public void handle(long arg0) {
		    	myGameBoard.draw(gc);
				if ( myGameBoard.isAlive() == false ) {
					AI.saveBoard(myGameBoard, "board.txt");
				}
				
			} 
		}.start();
    	myGameBoard.draw(gc);
		

    }
	
	public static void main(String[] args) {
		
		launch(args);
		
		//AI.printBoard( AI.loadFile("board.txt") );
		
//		ArrayList<Move> possibleMoves = new ArrayList<Move>();
//		Board b = AI.loadFile("board.txt");
//		AI.findPossibleMoves(possibleMoves,  b );
//		
//		for (Iterator<Move> iterator = possibleMoves.iterator(); iterator.hasNext();) {
//			Move move =  iterator.next();
//			System.out.print(move);
//		}
//		
//		b.start();
//		
//		try {
//			b.join();
//		}catch (InterruptedException e) { System.out.println("BOOM... of the map"); }
//		
//		System.out.println("- - - - - -\n"+ b.bestMove  );
//		System.out.println( b.getColor() );
//		AI.printBoard(b);
//		System.out.println();
//		AI.applyChanges(b.bestMove, b);
//		AI.printBoard(b);
	}
	

}
class BoardDrawer extends Thread{
	private GraphicsContext gc;
	private Board b;
	
	public BoardDrawer( GraphicsContext gc , Board b ) {
		this.gc = gc;
		this.b = b;
	}
	
	
	public void run () {

		final int  T = 5 ,SIZE = 70 , X= ( (int)gc.getCanvas().getWidth() - SIZE * 8 ) / 2, Y = 30 ;
		
		for (int j = 0; j<8; j++) 
			for (int i = 0; i< 8; i ++) {
				if (b.myBoard[j][i].piece == null )
					continue;
				if ( b.myBoard[j][i].piece.getColor() == Color.WHITE) 
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
