/*
 * Name: Aidan Robbins
 * Email: arobbin7@u.rochester.edu
 * Project: Knight's Tour
 * Last Edited: 11/27/23
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Tour extends JPanel{
private JLabel counter;
private JButton restart;

	public static void main(String[] args) {
		JFrame win = new JFrame ("Knight's Tour");
		Tour tour = new Tour();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		win.setContentPane(tour);
		win.pack();
		//setting up the window
	}
	
	public Tour() {
		setLayout(null);
		setPreferredSize(new Dimension(600, 660));
		Game game = new Game();
		add(game);
		add(counter);
		add(restart);
		game.setBounds(0, 60, 600, 600);
		counter.setBounds(0, 30, 600, 30);
		restart.setBounds(0,0, 600, 30);
		//initializing the game and setting up the bounds for ui features
	}
	

public class Move{
	int fromR;
	int fromC;
	int toR;
	int toC;
	
	Move(int fromR, int fromC, int toR, int toC){
		this.fromR = fromR;
		this.fromC = fromC;
		this.toR = toR;
		this.toC = toC;
	}

	public int getFromR() {
		return fromR;
	}

	public int getFromC() {
		return fromC;
	}

	public int getToR() {
		return toR;
	}

	public int getToC() {
		return toC;
	}
	// a simple class that stores coordinates of moves so we can use it in an array to track legal moves
	
}
	
public class Tiles{
	final int open = 0;
	final int occupied = 1;
	final int visited = 2;
	int[][] board;
	Random r = new Random();
	
	void setUp() {
		for(int row = 0; row<8; row++) {
			for(int col = 0; col <8; col++) {
				board[row][col] = 0;
			}
		}
		board[r.nextInt(8)][r.nextInt(8)] = occupied;
		//initializing all the tiles on the board
	}//end set up
	
	Tiles(){
		board = new int[8][8];
		setUp();
	}
	
	int tileStatus(int r, int c) {
		return board[r][c];
	}
	
	public void Move(Move move) {

		board[move.getToR()][move.getToC()] = occupied;
		board[move.getFromR()][move.getFromC()] = visited;
		
	}
	
	boolean canMove(int r1, int c1, int r2, int c2) {
		if(r2 >= 8 || r2 <0 || c2 >= 8 || c2 <0) {
			return false; //off board
		}
		else {
			return true; //legal
		}
	}
	
	Move[] getLegals() {
		int kRow = -1;
		int kCol = -1;
		ArrayList<Move> moves = new ArrayList<Move>(); 
		for(int row =0; row<8; row++) {
			for(int col =0; col<8; col++) {
				if(board[row][col] == occupied) {
					kRow = row;
					kCol = col;
				}
			}
		}
		
		//checking all of the possible moves a knight can make
				if(canMove(kRow, kCol, kRow + 2, kCol +1)) {
					moves.add(new Move(kRow, kCol, kRow +2, kCol +1));
				}
				if(canMove(kRow, kCol, kRow + 2, kCol -1)) {
					moves.add(new Move(kRow, kCol, kRow +2, kCol -1));
				}
				if(canMove(kRow, kCol, kRow - 2, kCol +1)) {
					moves.add(new Move(kRow, kCol, kRow -2, kCol +1));
				}
				if(canMove(kRow, kCol, kRow - 2, kCol -1)) {
					moves.add(new Move(kRow, kCol, kRow -2, kCol -1));
				}
				if(canMove(kRow, kCol, kRow + 1, kCol +2)) {
					moves.add(new Move(kRow, kCol, kRow +1, kCol +2));
				}
				if(canMove(kRow, kCol, kRow - 1, kCol +2)) {
					moves.add(new Move(kRow, kCol, kRow -1, kCol +2));
				}
				if(canMove(kRow, kCol, kRow + 1, kCol -2)) {
					moves.add(new Move(kRow, kCol, kRow +1, kCol -2));
				}
				if(canMove(kRow, kCol, kRow - 1, kCol -2)) {
					moves.add(new Move(kRow, kCol, kRow -1, kCol -2));
				}
			
		if(moves.size() ==0) {
			return null;
		}
		else {
			Move[] movesArray = new Move[moves.size()];
			for(int i = 0; i <moves.size(); i++) {
				movesArray[i] = moves.get(i);
			}
			return movesArray;
			}
		//returns an array of all legal moves
		
	}//end get legals
	
	
	
	
	}//end Tiles class

private class Game extends JPanel implements MouseListener, ActionListener{
	Tiles game;
	boolean inProgress;
	Move[] legals;
	int visits = 1;
	ArrayList<Point> points = new ArrayList<Point>();
	
	
	public Game() {
		setBackground(Color.BLACK);
		addMouseListener(this);
		counter = new JLabel("", JLabel.CENTER);
		restart = new JButton("Restart");
		restart.addActionListener(this);
		game = new Tiles(); 
		doNew(); 	
	}
	
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Restart":
			if(points.size() > 0) {
				points.clear();
			}
			visits = 1;
			inProgress = false;
			doNew();
			// button which restarts the game, sets visits back to one and clears the point arraylist
		}
	}
	
	
	public void doNew() {
		if(inProgress != true) {
			game.setUp();
			legals = game.getLegals();
			counter.setText("Tiles Visited: " + visits);
			inProgress = true;
			repaint();
		}
	} // end do new
	
	public void endGame() {
		counter.setText("Congratulations! You have completed the tour in " + visits + " moves.");
		inProgress= false;
	}
	
	public void click(int r, int c) {
		//checks if a click matches a legal move and if so does that move
		legals = game.getLegals();
		for(int i =0; i < legals.length; i++) {
			if(legals[i].getToR() == r && legals[i].getToC() == c) {
				doMove(legals[i]);
			}
		}
	}
	
	public void doMove(Move move) {
		game.Move(move);
		legals = game.getLegals();
		visits = visits + 1;
		counter.setText("Tiles Visited: " + visits);
		hasWon();
		points.add(new Point(move.getFromC() , move.getFromR() ));
		repaint(); 
	}
	
	public void hasWon() {
		if(inProgress) {
		for(int row =0; row <8; row++) {
			for(int col =0; col <8; col++) {
				if(game.tileStatus(row, col) == game.open) {
					return;
				}
			}
			}
		}
		inProgress = false;
		endGame();
	}
	
	public void paintComponent(Graphics g) {
		int OccCol = -1;
		int OccRow = -1;
		//first draws a checkerboard
		for(int row =0; row <8; row ++) {
			for(int col =0; col <8; col++) {
				if(row%2 == col%2) {
					g.setColor(Color.GRAY);
				}
				else {
					g.setColor(Color.WHITE);
				}
				g.fillRect(col * 75, row * 75, 75, 75);
				
				//draws the knight and saves the coordinates
				if(game.tileStatus(row, col) == game.occupied) {
					OccCol = col;
					OccRow = row;
					g.setColor(Color.BLACK);
					g.fillRect( col *75 +10 , row *75 + 55, 52, 15);
					g.fillRect( col *75 +25 , row *75 + 20, 20, 50);
					g.fillRect( col *75 +45 , row *75 + 20, 10, 10);
					g.fillRect( col *75 +45 , row *75 + 30, 20, 10);
					g.fillRect( col *75 +25 , row *75 + 10, 10, 10);
				}
			}
		}
		
		if (inProgress) {
			//highlighting the legal moves
			for(int i = 0; i < legals.length; i++) {
				g.setColor(Color.GREEN);
				g.fillRect(legals[i].getToC() *75 , legals[i].getToR() * 75, 75, 75);
				if(legals[i].getToR()%2 == legals[i].getToC()%2) {
					g.setColor(Color.GRAY);
					g.fillRect(legals[i].getToC() *75 +5  , legals[i].getToR() * 75 + 5, 65, 65);
					
				}
				else {
					g.setColor(Color.WHITE);
					g.fillRect(legals[i].getToC() *75 +5  , legals[i].getToR() * 75 + 5, 65, 65);
				}
				
				}
				
			}
		
		if( points != null) {
			//drawing the lines to track the path also draws blue dots to denote previously visited tiles. Then draws a line to the current position and redraws the knight in case
			//it is at a previously visited tile otherwise it would be covered by a circle
			g.setColor(Color.BLACK);
			
			for(int k =0; k < points.size(); k++) {
				if(k - 1 > 0) {
					g.drawLine((points.get(k).getX() * 75) +37 , (points.get(k).getY() * 75) + 37, (points.get(k -1).getX() * 75) +37 , (points.get(k -1).getY() * 75) +37 );
					g.drawLine(points.get(0).getX() *75 + 37, points.get(0).getY() * 75 +37, points.get(1).getX() *75 + 37, points.get(1).getY() * 75 +37);
				}
				if(points.size() == 2) {
					g.drawLine(points.get(0).getX() *75 + 37, points.get(0).getY() * 75 +37, points.get(1).getX() *75 + 37, points.get(1).getY() * 75 +37);
				}
				g.setColor(Color.BLUE);
				g.fillOval((points.get(k).getX() * 75) +25 , (points.get(k).getY() * 75) + 25, 20, 20);
				g.setColor(Color.BLACK);
				g.drawLine(points.get(points.size() -1).getX() * 75 + 37, points.get(points.size() -1).getY() * 75 + 37, OccCol * 75 + 37, OccRow * 75 + 37);
				g.setColor(Color.BLACK);
				g.fillRect( OccCol *75 +10 , OccRow *75 + 55, 52, 15);
				g.fillRect( OccCol *75 +25 , OccRow *75 + 20, 20, 50);
				g.fillRect( OccCol *75 +45 , OccRow *75 + 20, 10, 10);
				g.fillRect( OccCol *75 +45 , OccRow *75 + 30, 20, 10);
				g.fillRect( OccCol *75 +25 , OccRow *75 + 10, 10, 10);
			}
		}
		
		
		}// end paint
	
	public void mousePressed(MouseEvent e) {
		if(inProgress) {
			int col = e.getX() / 75;
			int row = e.getY() / 75;
			if(col >= 0 && col < 8 && row >= 0 && row < 8) {
				click(row, col);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
	}
    public void mouseClicked(MouseEvent e) { 
    }
    public void mouseEntered(MouseEvent e) { 
    }
    public void mouseExited(MouseEvent e) { 
    }
	
	
} // end game class

public class Point{
	//simple point class for the points arraylist
	int x;
	int y; 
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
}
}
