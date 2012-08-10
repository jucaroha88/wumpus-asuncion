package edu.uca.info2.components;

public class BoardNodeRelation {
	private BoardPosition boardPosition;
	private long nodeId;
		
	public BoardNodeRelation(BoardPosition boardPosition, long nodeId) {
		setBoardPosition(boardPosition);
		setNodeId(nodeId);
	}
	
	public void setBoardPosition(BoardPosition boardPosition) {
		this.boardPosition = boardPosition;		
	}
	
	public BoardPosition getBoardPosition() {
		return boardPosition;
	}
	
	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
	
	public long getNodeId() {
		return nodeId;
	}

}
