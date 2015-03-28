package model;

import java.awt.Point;

/**
 * @Shaun Rain
 * 2014
 */
public class TetrisBoard {
	
	public static final int EMPTY = 0;
	public static final int FILLED = 1;
	public static final int MOVING = 2;
	
	public static final int PLACE_OK = 3;				//placement successful
	public static final int PLACE_ROW_FILLED = 4;		//at least one row successful
	public static final int PLACE_OUT_OF_BOUNDS = 5;	//part of the piece if out of bound
	public static final int PLACE_BAD = 6;				//piece overlaps with some already filled
	
	public static int score = 0;
	private int[][] board;
	private int[][] backup;
	
	//constructor,to init the board
	public TetrisBoard(int width, int height){
		board = new int[width][height];
		backup = new int[width][height];
	}
	
	//place the Piece in the board
	public int place(Piece piece,int x,int y){
		Point[] body = piece.getBody();
		for (Point pt : body){
			//check the bounds first
			if(!inBounds(x+pt.x,y-pt.y))
				return PLACE_OUT_OF_BOUNDS;
			//check whether it is filled
			if(!placeOK(x+pt.x,y-pt.y))
				return PLACE_BAD;
			//it is safe to move to
			board[y - pt.y][x + pt.x] = MOVING;
		}
		
		//check fill a row
		boolean fillRow = true;
		return fillRow ? PLACE_ROW_FILLED : PLACE_OK;
		
	}
	
	private boolean placeOK(int x,int y) {
		if(board[y][x] != EMPTY)
			return false;
		return true;
	}
	
	private boolean inBounds(int x,int y) {
		if(x < 0 || x>=board[0].length || y < 0 || y >= board.length)
			return false;
		return true;
	}
	
	public void undo() {
		for(int i = 0; i < board.length; i++)
			System.arraycopy(backup[i], 0, board[i], 0, backup[i].length);
	}
	
	public void commit() {
		for(int r = 0;r < board.length;r++){
			for(int c = 0;c< board[r].length;c++){
				if(board[r][c] == MOVING)
					board[r][c] = FILLED;
			}
		}
		
		for(int i = 0;i < board.length;i++)
			System.arraycopy(board[i], 0, backup[i], 0, board[i].length);
	}
	
	public void clearRows() {
		boolean [] notFullRows = new boolean[board.length];
		for (int r = board.length - 1; r >= 0;r--){
			for(int c = 0;c< board[r].length && !notFullRows[r];c++) {
				if(board[r][c] == EMPTY)
					notFullRows[r] = true;
				
			}
		}
		
		boolean temp = false;
		for(boolean b : notFullRows) {
			if(!b){
				temp = true;
				score+=(TetrisBoard.score/100 + 1)*10;
			}
				
		}
		if(temp) {
			int pointerNew = board.length - 1;
			int[][] newBoard = new int[board.length][board[0].length];
			
			for(int i = notFullRows.length - 1; i >= 0; i--){
				if(notFullRows[i])
					System.arraycopy(board[i], 0, newBoard[pointerNew--], 0, board[i].length);
			}
			board = newBoard;
			commit();
		}
		
	}
	
	public int dropHeight(Piece piece,int x) {
		return 1;
	}
	
	public int [][] getBoard() {
		return board;
	}
	

}
