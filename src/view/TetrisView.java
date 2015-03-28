/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import listener.TetrisKeyListener;
import model.Piece;
import model.TetrisBoard;
import controller.TetrisController;

/**
 * @Shaun Rain
 * 2014
 */
@SuppressWarnings("serial")
public class TetrisView extends JFrame implements Runnable {
	private static int INTERVAL = 20;
	private TetrisController controller;
	private int[][] data = new int [10][20];
	
	private BufferedImage img;
	private Queue<Piece> pieceQueue;
	
	private static final int WIDTH = 240;
	private static final int HEIGHT = 480;
	private static final int SCORE_BOARD_WIDTH = 120;
	//private static int score = 0; 
	private static final int PADDING = 2;
	
	private int boxWidth;
	
	public Timer timer;
	
	public TetrisView(){
		this(WIDTH + SCORE_BOARD_WIDTH,HEIGHT);
		
	}
	public TetrisView(int width, int height) {
		this.setTitle("Tetris");
		this.setSize(width, height);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setAlwaysOnTop(true);
		
		img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
	}


	public void start() {
		this.addKeyListener(new TetrisKeyListener(controller));
		this.pieceQueue = controller.getPieceQueue();
		
		data = controller.getBoard();
		boxWidth = this.getHeight() / data.length;
		
		this.setVisible(true);
		SwingUtilities.invokeLater(this);		
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = img.createGraphics();
		g2d.setBackground(Color.GRAY);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());//clearRect?
		
		drawGrid(g2d);
		drawScore(g2d);
		drawPieceQueue(g2d);
		
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(),this);
		
	}
	
	public void drawGrid(Graphics g){
			Graphics g2d = (Graphics2D)g.create();
			for(int r = 0;r<data.length;r++){
				for(int c = 0;c<data[r].length;c++){
					switch(data[r][c]){
						case TetrisBoard.EMPTY:g2d.setColor(Color.DARK_GRAY);break;
						case TetrisBoard.FILLED:g2d.setColor(Color.GRAY);break;
						case TetrisBoard.MOVING:g2d.setColor(pieceQueue.peek().getColor());break;
				
					}
					g2d.fill3DRect(boxWidth * c + PADDING,boxWidth * r + PADDING,boxWidth - PADDING,boxWidth - PADDING,true);
			//fill3DRect
				}
			}
			g2d.dispose();
		}
	
	public void drawScore(Graphics g){
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score:"+ TetrisBoard.score, WIDTH + 30, 40);
		g2d.drawString("Level:"+ (int)(TetrisBoard.score/100 + 1),WIDTH + 30, 450);
		g2d.dispose();
		
	}
	
	public void drawPieceQueue(Graphics g){
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setColor(Color.white);
		
		int offsetX = WIDTH + 20;
		int offsetY = this.getHeight() / 5;
		for (Piece piece : pieceQueue) {
			g2d.setColor(piece.getColor());
			Point [] body = piece.getBody();
			for(Point pt : body) {
				g2d.fill3DRect(offsetX + pt.x * boxWidth + PADDING, offsetY - pt.y * boxWidth + PADDING, boxWidth - PADDING, boxWidth - PADDING, true);
			}
			offsetY += this.getHeight() / 5;
		}
		g2d.dispose();
	}
	
	public void setController(TetrisController controller) {
		this.controller = controller;
	}
	
	public void setData(int[][] data) {
		this.data = data;
	}
	
	public void setPieceQueue(Queue<Piece> pieceQueue) {
		this.pieceQueue = pieceQueue;
	}
	
	@Override
	public void run() {
		timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				repaint();
			}
		});
		timer.start();
	}
}
