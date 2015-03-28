package model;

import java.awt.Color;
import java.awt.Point;

/**
 * @Shaun Rain
 * 2014
 */
public abstract class Piece {
	
	public static final Color [] pieceColors = {
		Color.BLUE, Color.CYAN, Color.ORANGE, Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW
	};
	
	public  static String [] pieceStrings = {
		"T","Square","Stick","LLeft","LRight","LDog","RDog"
	};
	
	private Piece next;
	
	public abstract Piece parsePoints(String string);
	public abstract Piece nextRotation();
	
	public abstract Point[] getBody();
	public abstract Point[] getSkirt();
	
	public abstract Color getColor();
	public abstract void setColor(Color color);
	public abstract int getWidth();
	
	public Piece getNext() {
		return next;
	}
	
	public void setNext(Piece next){
		this.next = next;
	}
	

}
