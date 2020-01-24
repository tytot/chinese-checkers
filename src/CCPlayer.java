
public class CCPlayer {
	
	@SuppressWarnings("unused")
	private final int player;
	public CCPlayer(int player) {
		this.player = player;
	}
	
	public Hole selectPiece(Hole from, Board theBoard) {return from;}
	public Hole movePiece(Hole to, Board theBoard, boolean hopsOnly) {return to;}
}
