import java.awt.*;
import java.awt.image.ImageObserver;

public class ChineseCheckers extends FlexiblePictureExplorer implements ImageObserver {
	public static int imgWidth;
	private int holeRad;
	private int buttonSize;
	private int padding;
	private int leftPadding;
	private int center;
	private Board theBoard;
	
	private final Color highlight = new Color(104,64,30);
	private final Color outlineCol = new Color(205,133,63);
	private final Color boardCol = new Color(244,164,96);
	private final Color bkgCol = Color.GRAY;
	private final Color doneCol = Color.DARK_GRAY; // different than other colors
	private final Color[] colors = {outlineCol, Color.RED, Color.BLUE, Color.YELLOW, new Color(50,205,50), Color.BLACK, Color.WHITE, highlight};
	
	private int numPlayers = 2;
	private Color[] bColors = {Color.WHITE, Color.BLUE, Color.GRAY}; // white=player, blue=CPU, gray=not playing
	private int[] bState = {0, 0, 2, 2, 2, 2};
	private int turn = 1;
	private boolean streak = false;
	private Hole selectedHole;
	private boolean gameStarted = false;
	private boolean gameOver = false;
	private CCPlayer[] players;
	
	public ChineseCheckers(int size) {
		super(new Picture(size, size));
		imgWidth = size;
		holeRad = size / 64;
		buttonSize = imgWidth / 8;
		padding = imgWidth / 16;
		leftPadding = imgWidth / 4;
		center = imgWidth / 2;
		
		theBoard = new Board();
		
		drawStartScreen();
	}
	
	private void drawStartScreen() {
		Picture start = new Picture("images/CCtitlescreen.jpg").scale(imgWidth / 640.0, imgWidth / 640.0);
		for (int i = 0; i < 6; i++)
			drawPButton(start, i+1, bColors[bState[i]]);
		setImage(start);
		setTitle("Chinese Checkers");
	}
	
	private void drawPButton(Picture pic, int player, Color col) {
		if (col.equals(Color.GRAY)) {
			bState[player-1] = 2;
			if (player+1 <= 6)
				drawPButton(pic, player+1, Color.GRAY);
		} else if (col.equals(Color.WHITE)) {
			bState[player-1] = 0;
			boolean done = false;
			for (int i = player-1; i > 0 && !done; i--) {
				if (bState[i-1] == 2) {
					drawPButton(pic, i, Color.WHITE);
					done = true;
				}
			}
		} else
			bState[player-1] = 1;
		
		Picture button = new Picture(buttonSize, buttonSize);
		button.setAllPixelsToAColor(col);
		Graphics g = button.getGraphics();
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Calibri", Font.BOLD, imgWidth / 16));
		g.drawString(player+"", (int)(buttonSize / 2.5), (int)(buttonSize / 1.5));
		pic.copy(button, center+((player-1)/3)*(buttonSize+padding), leftPadding + ((player-1)%3)*(buttonSize+padding));
		
		int players = 0;
		for (int i = 0; i < 6; i++) {
			if (bState[i] != 2)
				players++;
		}
		numPlayers = players;
	}
	
	private void drawBoard() {
		players = new CCPlayer[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			if (bState[i] == 0)
				players[i] = new CCPlayer(i+1);
			else
				players[i] = new ComputerCCPlayer(i+1);
		}
		
		Picture disp = new Picture(imgWidth, imgWidth);
		Pixel[][] pixels = disp.getPixels2D();
		double boardRadius = 0.875 * center;
		double outlineRadius = 0.9 * center;
		for (int x = 0; x < imgWidth; x++) {
			for (int y = 0; y < imgWidth; y++) {
				if (distance(x, y, center, center) <= boardRadius)
					pixels[y][x].setColor(boardCol);
				else if (distance(x, y, center, center) <= outlineRadius)
					pixels[y][x].setColor(outlineCol);
				else
					pixels[y][x].setColor(bkgCol);
			}
		}
		for (Hole hole : theBoard.getHoles())
			drawHole(hole, disp, 0);
		
		int i = 1;
		if (numPlayers == 3) {
			for (Hole hole : theBoard.getStartHoles(6)) {
				drawHole (hole, disp, 1);
				hole.setStatus(1);
			}
			i = 2;
		}
		for (; i <= numPlayers; i++) {
			for (Hole hole : theBoard.getStartHoles(i)) {
				drawHole(hole, disp, i);
				hole.setStatus(i);
			}
		}
		drawString(disp, "Player 1's turn");
		setImage(disp);
		setTitle("Chinese Checkers");
	}
	
	private void drawHole(Hole hole, Picture pic, int player) {
		Pixel[][] pixels = pic.getPixels2D();
		int[] coords = hole.toRect(imgWidth);
		for (int x = coords[0] - holeRad; x <= coords[0] + holeRad; x++) {
			for (int y = coords[1] - holeRad; y <= coords[1] + holeRad; y++) {
				if (distance(x, y, coords[0], coords[1]) <= holeRad)
					pixels[y][x].setColor(colors[player]);
			}
		}
		hole.setStatus(player);
	}
	
	private double distance(int x1, int y1, int x2, int y2) {
		int xDist = Math.abs(x2 - x1);
		int yDist = Math.abs(y2 - y1);
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}
	
	private boolean playerWon(int player) {
		int tri;
		if (player % 2 == 1)
			tri = player + 1;
		else
			tri = player - 1;
		boolean occupied = false;
		for (Hole hole : theBoard.getStartHoles(tri)) {
			if (hole.getStatus() == 0)
				return false;
			if (!occupied && hole.getStatus() == player)
				occupied = true;
		}
		if (!occupied)
			return false;
		return true;
	}
	
	private void drawString(Picture pic, String str) {
		int labelHeight = imgWidth / 20;
		int labelWidth = labelHeight * 4;
		Picture label = new Picture(labelHeight, labelWidth);
		label.setAllPixelsToAColor(boardCol);
		Graphics g = label.getGraphics();
		g.setColor(colors[turn]);
		g.setFont(new Font("Agency FB", Font.BOLD, (int)(labelHeight / 1.5)));
		g.drawString(str, labelHeight / 2, labelHeight / 2);
		
		pic.copy(label, labelHeight * 2, labelWidth * 2);
	}
	
	private void nextButton(Picture pic, boolean draw) {
		int start = imgWidth * 7 / 8;
		int width = imgWidth / 12;
		
		Pixel[][] pixels = pic.getPixels2D();
		for (int x = start; x <= start + width; x++) {
			for (int y = start - width; y <= start + width; y++) {
				if (draw) {
					if (Math.abs(y - start) < 0.6 * ((start + width) - x))
						pixels[y][x].setColor(doneCol);
				} else
					pixels[y][x].setColor(bkgCol);
			}
		}
	}
	
	private void nextTurn(Picture pic) {
		if (playerWon(turn)) {
			drawString(pic, "Player " + turn + " won!!!");
			gameOver = true;
		} else {
			if (turn < numPlayers)
				turn++;
			else
				turn = 1;
			nextButton(pic, false);
			drawString(pic, "Player " + turn + "'s turn");
		}
	}
	
	@Override
	public void mouseClickedAction(DigitalPicture pict, Pixel pix) {
		Picture pic = (Picture)pict;
		if (!gameStarted) {
			if (pix.getRow() > (0.825 * imgWidth)) {
				drawBoard();
				gameStarted = true;
			} else {
				int col = pix.getCol();
				int row = pix.getRow();
				int height = padding + 2*buttonSize;
				int width = 2*padding + 3*buttonSize;
				if (col >= leftPadding && col <= leftPadding + width && row >= center && row <= center+height) {
					col = (int) ((col - leftPadding) / (width / 3.0));
					row = (int) ((row - center) / (height / 2.0));
					int player = 1 + col;
					if (row == 1)
						player += 3;
					
					int color = bState[player-1];
					if (player <= 2) {
						if (color == 0)
							color = 1;
						else
							color = 0;
					} else
						color++;
					drawPButton((Picture)pict, player, bColors[color % 3]);
				}
			}
		} else {
			if (!gameOver) {
				if (bState[turn - 1] == 0) { //if player 
					int[] coords = Hole.toHex(pix, imgWidth);
					if (theBoard.isHole(coords[0], coords[1])) {
						Hole hole = theBoard.getHole(coords[0], coords[1]);
						if (pix.getColor().equals(colors[turn]) && !streak) {
							if (selectedHole != null) {
								for (Hole adjHole : theBoard.getNextMoves(selectedHole, false))
									drawHole(adjHole, pic, 0);
							}
							selectedHole = players[turn - 1].selectPiece(hole, theBoard);
							for (Hole adjHole : theBoard.getNextMoves(selectedHole, streak)) {
								drawHole(adjHole, pic, 7);
								adjHole.setStatus(0);
							}
						} else if (pix.getColor().equals(colors[7])) {
							Hole to = players[turn - 1].movePiece(hole, theBoard);
							drawHole(selectedHole, pic, to.getStatus());
							for (Hole adjHole : theBoard.getNextMoves(selectedHole, false))
								drawHole(adjHole, pic, 0);
							drawHole(to, pic, turn);
							if (Math.abs(selectedHole.x() - to.x()) > 1 || Math.abs(selectedHole.y() - to.y()) > 1) {
								streak = true;
								selectedHole = to;
							} else {
								streak = false;
								selectedHole = null;
							}
							if (selectedHole != null) {
								selectedHole = players[turn - 1].selectPiece(hole, theBoard);
								for (Hole adjHole : theBoard.getNextMoves(selectedHole, false))
									drawHole(adjHole, pic, 0);
								for (Hole adjHole : theBoard.getNextMoves(selectedHole, streak)) {
									drawHole(adjHole, pic, 7);
									adjHole.setStatus(0);
								}
								nextButton(pic, true);
							} else
								nextTurn((Picture)pic);
						}
					} else if (pix.getColor().equals(doneCol)) {
						for (Hole adjHole : theBoard.getNextMoves(selectedHole, false))
							drawHole(adjHole, pic, 0);
						streak = false;
						nextTurn((Picture)pic);
					}
				} else { //if CPU
					selectedHole = players[turn - 1].selectPiece(null, theBoard);
					drawHole(selectedHole, pic, 0);
					//System.out.println("CPU player " + (turn) + " selected " + selectedHole.x() + "," + selectedHole.y());
					Hole dest = players[turn - 1].movePiece(selectedHole, theBoard);
					drawHole(dest, pic, turn);
					if (!streak)
						nextTurn((Picture)pic);
				}
			}
		}
	}

	public static void main(String[] args) {
		new ChineseCheckers(640);
	}
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
}