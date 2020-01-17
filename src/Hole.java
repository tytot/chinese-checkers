
public class Hole {
	private final int xPos;
	private final int yPos;
	private boolean filled;
	
	private int status;
	private int goalSpot;
	
	public Hole(int x, int y) {
		xPos = x;
		yPos = y;
		status = 0;
		goalSpot = 0;
	}
	public int x() {
		return xPos;
	}
	public int y() {
		return yPos;
	}
	public boolean isFilled() {
		return filled;
	}
	public int getStatus() {
		return status;
	}
	public int getGoalSpot() {
		return goalSpot;
	}
	public void setStatus(int newStatus) {
		status = newStatus;
	}
	public void setGoalSpot(int newGoalSpot) {
		goalSpot = newGoalSpot;
	}
	
	public int[] toRect(int size) {
		int center = size / 2;
		double yOffset = center / 12.0;
		double y = center - yOffset * yPos;
		
		double xOffset = ((2 * Math.sqrt(3.0)) / 3.0) * yOffset;
		double x = center + xOffset * (xPos + 0.5 * yPos);
		
		int[] coords = {(int) Math.round(x), (int) Math.round(y)};
		return coords;
	}
	public static int[] toHex(Pixel pix, int size) {
		int center = size / 2;
		double yOffset = center / 12.0;
		double xOffset = (2.0 * Math.sqrt(3.0)) / 3.0 * yOffset;
		
		double yPos = (center - pix.getY()) / yOffset;
		double xPos = ((pix.getX() - center) / xOffset) - (0.5 * yPos);
		
		int[] coords = {(int) Math.round(xPos), (int) Math.round(yPos)};
		return coords;
	}
}
