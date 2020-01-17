import java.util.ArrayList;

public class ComputerCCPlayer extends CCPlayer {
	
	private final int pVal;
	public ComputerCCPlayer(int player) {
		super(player);
		//System.out.println("new CPU for player " + player);
		pVal = player;
	}
	
	public Hole selectPiece(Hole from, Board theBoard) {
		//System.out.println("current player = " + pVal);
		ArrayList<Hole> myHoles = new ArrayList<Hole>();
		for (Hole hole : theBoard.getHoles()) {
			if (hole.getStatus() == pVal) {
				myHoles.add(hole);
			}
		}
		
		Hole output;
		ArrayList<Hole> nextMoves;
		do {
			output = myHoles.get((int)(Math.random() * 10));
			nextMoves = theBoard.getNextMoves(output, false);
		} while (nextMoves.isEmpty());
		
		return output;
	}
	public Hole movePiece(Hole to, Board theBoard) {
		ArrayList<Hole> nextMoves = theBoard.getNextMoves(to, false);
		//System.out.println("next moves for " + to.x() + "," + to.y());
//		for (Hole nextMove : nextMoves) {
//			System.out.println(nextMove.x() + "," + nextMove.y());
//		}
		Hole[] destHoles = {theBoard.getHole(4,-8), theBoard.getHole(-4,8), theBoard.getHole(8,-4), theBoard.getHole(-8,4), theBoard.getHole(4,4), theBoard.getHole(-4,-4)};
		int goal;
		if (pVal % 2 == 1)
			goal = pVal + 1;
		else
			goal = pVal - 1;
		Hole goalHole = destHoles[goal - 1];
		double least = Double.MAX_VALUE;
		Hole output = null;
		for (Hole move : nextMoves) {
			double dist = dist(move, goalHole);
			if (dist < least) {
				least = dist;
				output = move;
			}
		}
		return output;
	}
	
	private double dist(Hole from, Hole to) {
		int[] coords1 = from.toRect(ChineseCheckers.imgWidth);
		int[] coords2 = to.toRect(ChineseCheckers.imgWidth);
		int dy = coords2[1] - coords1[1];
		int dx = coords2[0] - coords1[0];
		return Math.sqrt(dy*dy + dx*dx);
	}
}