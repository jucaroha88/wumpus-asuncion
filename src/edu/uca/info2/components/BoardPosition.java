package edu.uca.info2.components;

public class BoardPosition {
	private int row;
	private int col;
	
	public BoardPosition(int row, int col) {
		setRow(row);
		setCol(col);
	}
	
	
	public void setRow(int row) {
		this.row = row;
	}
	public int getRow() {
		return row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getCol() {
		return col;
	}
	
}
