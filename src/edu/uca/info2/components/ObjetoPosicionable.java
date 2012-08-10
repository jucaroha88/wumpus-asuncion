package edu.uca.info2.components;

public abstract class ObjetoPosicionable {
	
	
	private int row;
	private int col;
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
