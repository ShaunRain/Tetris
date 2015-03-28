/**
 * 
 */
package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.Timer;

import ScoreUDP.ScoreUDPClient;
import view.TetrisView;
import model.Piece;
import model.TetrisBoard;
import model.TetrisPiece;

/**
 * @Shaun Rain
 * 2014
 */
public class TetrisController {
	
	private TetrisBoard board;
	private TetrisView view;
	ScoreUDPClient client;
	
	private Piece[] pieces;
	private Piece currentPiece;
	
	private int currentPosX;
	private int currentPosY;
	
	private int currentPosXLimit;
	private int currentPosYLimit;
	
	public int prevPosX;
	public int prevPosY;
	
	private Random random = new Random();
	private Queue<Piece> queue = new LinkedList<Piece>();
	
	private static final int WAITING_SEQUENCE_LIMIT = 5;
	private int INTERVAL = 420;
	
	private int columns;
	private int rows;
	public boolean flag = true;
	
	private Timer timer;
	
	public TetrisController() {
		pieces = TetrisPiece.getPieces();
	}
	
	
	//populate the piece queue and maintain it to be numbver of 5
	private Piece populateQueue() {
		for (int i = queue.size(); i< WAITING_SEQUENCE_LIMIT; i++) {
			queue.add(pieces[random.nextInt(pieces.length)]);
		}
		return queue.poll();
	}
	
	public void setBoard(TetrisBoard board) {
		this.board = board;
	}
	
	public void setView(TetrisView view) {
		this.view = view;
	}
	
	//threading method to update the game
	public void update() {
		int [][] data = board.getBoard();
		columns = data[0].length;
		rows = data.length;
		
		currentPiece = getNextPiece();
		timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				prevPosX = currentPosX;
				prevPosY = currentPosY;
				board.undo();
				int state = board.place(currentPiece, currentPosX, currentPosY);
				processPiece(state);
				view.setData(board.getBoard());
				currentPosY++;
			}

		});
		timer.start();
	}
	
	public void gameKeyControls(int keyCode) {
		prevPosX = currentPosX;
		prevPosY = currentPosY;
		
		switch(keyCode) {
		case KeyEvent.VK_UP:
			currentPiece = currentPiece.nextRotation();
			break;
		case KeyEvent.VK_LEFT:
			currentPosX--;
			break;
		case KeyEvent.VK_RIGHT:
			currentPosX++;
			break;
		case KeyEvent.VK_DOWN:
			currentPosY++;
			break;
		case KeyEvent.VK_SPACE:
			flag = !flag;
			if(flag==false){
				timer.stop();
			}else
				timer.start();
			break;
		}
		restrictXY();
		
		//refresh the screen
		board.undo();
		int state = board.place(currentPiece, currentPosX, currentPosY);
		processPiece(state);
		view.setData(board.getBoard());
	}
	
	private void restrictXY() {
		currentPosXLimit = getXLimit();
		currentPosYLimit = getYLimit(currentPosX,currentPosY);
		//restriction on x&y
		if(currentPosX <0)
			currentPosX = 0;
		else if(currentPosX > currentPosXLimit)
			currentPosX = currentPosXLimit;
		
		if(currentPosY >= currentPosYLimit)
			currentPosY = currentPosYLimit;
			
	}
	
	private void processPiece(int state) {
		//reach the bottom or crash with other pieces
		if(state == TetrisBoard.PLACE_OUT_OF_BOUNDS ||
				state == TetrisBoard.PLACE_BAD) {
			board.undo();
			currentPosYLimit = getYLimit(prevPosX,prevPosY);
			System.out.println("Prev X:"+ prevPosX + "\tPrev Y: "+ prevPosY);
			System.out.println("currentX:" + currentPosX + "\tcurrentY:" + currentPosY);
			System.out.println("limit x:" + currentPosXLimit + "\tlimit Y:" + currentPosYLimit);
			currentPosX = prevPosX;
			currentPosY = prevPosY;
			
			if(currentPosY >= currentPosYLimit) {
				board.place(currentPiece, currentPosX, currentPosYLimit - 1);
				board.commit();
				board.clearRows();
				
				currentPiece = getNextPiece();
				view.setPieceQueue(queue);
				
			}else{
				board.place(currentPiece, prevPosX, prevPosY);
			}
		}
	}
	
	
	private Piece getNextPiece() {
		currentPiece = populateQueue();
		currentPosXLimit = getXLimit();
		currentPosX = random.nextInt(currentPosXLimit);
		currentPosY = 0;
		currentPosYLimit = getYLimit(currentPosX,currentPosY);
		return currentPiece;
	}
	
	private int getXLimit() {
		return columns - currentPiece.getWidth();
		
	}
	public int getYLimit( int PosX, int PosY) {
		int y = PosY;
		int state;
		boolean stop =false;
		do{
			board.undo();
			state = board.place(currentPiece, PosX, y++);
			stop = (state == TetrisBoard.PLACE_BAD )||
					(state == TetrisBoard.PLACE_OUT_OF_BOUNDS && y >= rows);
			
		}while(!stop);
		
		if(TetrisBoard.score%100==0 && TetrisBoard.score!=0){
			timer.stop();
			timer = new Timer(INTERVAL-(40*(int)(TetrisBoard.score/100)), new ActionListener(){
				public void actionPerformed(ActionEvent e){
					prevPosX = currentPosX;
					prevPosY = currentPosY;
					board.undo();
					int state = board.place(currentPiece, currentPosX, currentPosY);
					processPiece(state);
					view.setData(board.getBoard());
					currentPosY++;
				}

			});
			timer.start();
		}
			
		
		if(y == 1 ){
			try {
				client = new ScoreUDPClient();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			JFrame f = new JFrame();
			f.setBounds(550, 350, 60, 30);
			f.setBackground(Color.GRAY);
			JTextField t =new JTextField();
			t.setText("Final Score:" + TetrisBoard.score);
			t.setEditable(false);
			t.setFont(new Font("",Font.BOLD,16));
			
			f.setLayout(new BorderLayout());
			f.add(t,BorderLayout.CENTER);
			f.setResizable(false);
			f.pack();
			f.setAlwaysOnTop(true);
			f.setVisible(true);
			try {
				view.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		board.undo();
		return Math.min(rows, y - 1); 
	}
	
	public Queue<Piece> getPieceQueue() {
		return queue;
	}
	
	public int [][] getBoard() {
		return board.getBoard();
		
	}
	

}
