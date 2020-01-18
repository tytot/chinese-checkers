# chinese-checkers
This is a Chinese Checkers picture project built in Java using object-oriented principles. 

ChineseCheckers.java is the main runtime component. It holds an instance of Board.java, which countains information about the current board state. The board consists of multiple holes, each of which are instances of Hole.java. Hole.java contains information about a given hole's location in both rectangular coordinates and hexagonal coordinates and its status (filled by player 1, filled by player 2, not filled, etc.). 

ChineseCheckers.java also holds instances of CCPlayer.java, each of which represent a player. CCPlayer.java is extended by ComputerCCPlayer.java, which represents a CPU. CCPlayer.java represents a human, so it only returns the hole that you pass it because selection and moving of pieces by a human in handled by the onMouseClicked function in ChineseCheckers.java. 

On the other hand, ComputerCCPlayer.java overrides CCPlayer.java's selectPiece and movePiece functions with its own algorithm. ComputerCCPlayer.java aims to move all of its pieces to the tip of the opposite triangle. It picks one of its random pieces that has available moves and moves the piece to the closest available move to the tip of the opposite triangle. Thus, it takes time for the CPU to win, because it does not know what a winning move is. If you lose to a CPU in a one-on-one, you are pathetic.

# Title Screen
- Start the game by running ChineseCheckers.java.
- Use the graphical player selection user interface to select the number of player and which players are CPUs.
  - White = human player
  - Blue = CPU player
  - Gray = not playing
  
 Ex. according to these settings, players 1 and 3 are humans, players 2 and 4 are CPUs, and players 5 and 6 aren't playing.
 ![Example player configuration](https://github.com/tytot/chinese-checkers/blob/master/titleexample.JPG)
- Once you are done, click the "START" button to start the game.

# How to Play
If you were to use the player configuration as shown above, your initial board would look like this:
 ![Initial board](https://github.com/tytot/chinese-checkers/blob/master/play1.JPG)
- Click on a piece to see its possible moves
 ![Possible moves shown](https://github.com/tytot/chinese-checkers/blob/master/play2.JPG)
- Click on a highlighted hole to move a hole to that spot.
- If your move is a hop, you have an option to continue your turn by moving your selected piece further along.
- When you are done, click the gray arrow in the bottom right corner to end your turn.
- To advance a CPUs turn, simply click anywhere on the screen.
- Yes, you can pit a CPU against another CPU. I pray you do not.

Best,

Tyler
