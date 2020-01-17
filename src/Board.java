import java.util.ArrayList;

public class Board {
	private static final int[] numHoles = {1,2,3,4,13,12,11,10,9,10,11,12,13,4,3,2,1};
	private static final int[] startXs = {-4,-4,-4,-4,-8,-7,-6,-5,-4,-4,-4,-4,-4,1,2,3,4};
	private ArrayList<Hole> holes = new ArrayList<Hole>(121);
	
	
	public Board() {
		for (int y = 8; y >= -8; y--) {
			int startX = startXs[8 - y];
			for (int x = startX; x < startX + numHoles[8 - y]; x++) {
				Hole hole = new Hole(x, y);
				holes.add(hole);
			}
		}
	}
	public ArrayList<Hole> getHoles() {
		return holes;
	}
	public ArrayList<Hole> getStartHoles(int player) {
		ArrayList<Hole> startHoles = new ArrayList<Hole>();
		if (player == 1) {
			for (int y = -8; y < -4; y++) {
				for (int x = startXs[8 - y]; x < startXs[8 - y] + numHoles[8 - y]; x++)
					startHoles.add(getHole(x, y));
			}
		}
		else if (player == 2) {
			for (int y = 8; y > 4; y--) {
				for (int x = startXs[8 - y]; x < startXs[8 - y] + numHoles[8 - y]; x++)
					startHoles.add(getHole(x, y));
			}
		}
		else if (player == 3) {
			for (int y = -4; y <= -1; y++) {
				for (int x = 5; x < 5 - y; x++)
					startHoles.add(getHole(x, y));
			}
		}
		else if (player == 4) {
			for (int y = 1; y <= 4; y++) {
				for (int x = -4 - y; x < -4; x++)
					startHoles.add(getHole(x, y));
			}
		}
		else if (player == 5) {
			for (int y = 1; y <= 4; y++) {
				for (int x = 5 - y; x < startXs[8 - y] + numHoles[8 - y]; x++)
					startHoles.add(getHole(x, y));
			}
		}
		else {
			for (int y = -4; y <= -1; y++) {
				for (int x = -4; x < -4 - y; x++)
					startHoles.add(getHole(x, y));
			}
		}
		return startHoles;
	}
	public boolean isHole(int x, int y) {
		return holes.contains(getHole(x, y));
	}
	public Hole getHole(int x, int y) {
		for (Hole hole : holes) {
			if (hole.x() == x && hole.y() == y)
				return hole;
		}
		return null;
	}
	public ArrayList<Hole> getNextMoves(Hole hole, boolean hopsOnly) {
		ArrayList<Hole> rolls = new ArrayList<Hole>();
		ArrayList<Hole> hops = new ArrayList<Hole>();
		int x = hole.x();
		int y = hole.y();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!((i == 0 && j == 0) || (i == 1 && j == 1) || (i == -1 && j == -1))) {
					if (isHole(x + i, y + j)) {
						Hole move = getHole(x + i, y + j);
						if (move.getStatus() == 0)
							rolls.add(move);
						else if (isHole(x + 2*i, y + 2*j)) {
							Hole hop = getHole(x + 2*i, y + 2*j);
							if (hop.getStatus() == 0)
								hops.add(hop);
						}
					}
				}
			}
		}
		if (!hopsOnly) {
			rolls.addAll(hops);
			return rolls;
		}
		return hops;
	}
}
