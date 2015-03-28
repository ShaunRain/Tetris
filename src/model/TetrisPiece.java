package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Shaun Rain
 * 2014
 */
public class TetrisPiece extends Piece{
	
	private Point [] data;
	private Point [] skirts;//?
	private Color color;
	private int width;
	
	private static int[] delimiters = {4,1,2,4,4,2,2};
	private static Piece[] pieces = new Piece[delimiters.length];
	private static Point[][] vertices = {
		//T
		{new Point(0,0),new Point(1,0),new Point(2,0),new Point(1,1)},
		{new Point(0,0),new Point(0,1),new Point(1,1),new Point(0,2)},
		{new Point(1,0),new Point(0,1),new Point(1,1),new Point(2,1)},
		{new Point(0,1),new Point(1,0),new Point(1,1),new Point(1,2)},
		
		//Square
		{new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1)},
		
		//Stick
		{new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0)},
		{new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3)},
		
		//LLeft
		{new Point(0,0),new Point(1,0),new Point(1,1),new Point(1,2)},
		{new Point(0,1),new Point(1,1),new Point(2,1),new Point(2,0)},
		{new Point(0,0),new Point(0,1),new Point(0,2),new Point(1,2)},
		{new Point(0,1),new Point(0,0),new Point(1,0),new Point(2,0)},
		
		//RLeft
		{new Point(1,0),new Point(0,0),new Point(0,1),new Point(0,2)},
		{new Point(0,0),new Point(0,1),new Point(1,1),new Point(2,1)},
		{new Point(1,0),new Point(1,1),new Point(0,2),new Point(1,2)},
		{new Point(0,0),new Point(1,0),new Point(2,0),new Point(2,1)},
		
		//LDog
		{new Point(1,0),new Point(1,1),new Point(0,1),new Point(0,2)},
		{new Point(0,0),new Point(1,0),new Point(1,1),new Point(2,1)},
		
		//RDog
		{new Point(0,0),new Point(0,1),new Point(1,1),new Point(1,2)},
		{new Point(0,1),new Point(1,1),new Point(1,0),new Point(2,0)}
		
	};
	
	static {
		//a temp to store all the pieces
		Piece[] temp = new Piece[vertices.length];
		//init all the pieces
		for(int i = 0;i< vertices.length;i++){
			temp[i] = new TetrisPiece(vertices[i]);	
		}
		//init the circular linked list
		int index = 0;
		int pieceIndex = 0;
		for(int rotationsNum : delimiters) {
			for(int i = 0;i < rotationsNum;i++){
				temp[index + i].setNext(temp[index + (i + 1) % rotationsNum]);
				temp[index + i].setColor(Piece.pieceColors[pieceIndex]);
			}
			pieces[pieceIndex++] = temp[index];
			index += rotationsNum;
		}
	}
	
	private TetrisPiece(Point[] points){
		data = points;
	}
	public static Piece[] getPieces(){
		return pieces;
	}
	
	public Piece parsePoints(String string){
		
		if(string==null)
			throw new IllegalArgumentException("string to parse cannot be null!");
		for(int i = 0;i < pieceStrings.length;i++){
			if(string.equals(pieceStrings[i]))
				return pieces[i];
		}
		return null;
	}
	
	public Piece nextRotation(){
		return this.getNext();
	}
	
	public Point[] getBody(){
		return data;
	}
	
	public Point[] getSkirt(){
		if(skirts == null) {
			List<Point>list = new ArrayList<Point>();
			for(Point pt : data){
				if(pt.y == 0)
					list.add(pt);
			}
			Object[] arr = list.toArray();
			skirts = Arrays.copyOf(arr, arr.length,Point[].class);
		}
		return skirts;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public int getWidth(){
		if(width == 0){
			for(Point pt :data) {
				if(pt.x > width)
					width = pt.x;
			}
			width++;
		}
		return width;
	}
	
	
	
	
}
