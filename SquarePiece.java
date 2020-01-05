
public class SquarePiece implements Cloneable {
	OthelloPiece piece = null;
	
	public SquarePiece() {}
			
	public SquarePiece(OthelloPiece piece) {
		if ( piece == null ) {
			this.piece = null;
			return;
		}
			
		this.piece = new OthelloPiece(piece.getColor());
	}
	
	public static SquarePiece cloneSquarePiece(SquarePiece sq) {
		return new SquarePiece(sq.piece);
	}
}

