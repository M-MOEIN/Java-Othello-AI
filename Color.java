
enum Color{
	BLACK , WHITE;

	public  Color getOpositColor () {
		return ( this == Color.BLACK) ? Color.WHITE : Color.BLACK;
	}
}