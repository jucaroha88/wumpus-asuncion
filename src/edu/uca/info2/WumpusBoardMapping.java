package edu.uca.info2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.uca.info2.components.BoardNodeRelation;
import edu.uca.info2.components.BoardPosition;

public class WumpusBoardMapping {
	
	private List <BoardNodeRelation> mapping = new ArrayList <BoardNodeRelation> ();
	
	public void addMapping(BoardNodeRelation m) {		
		mapping.add(m);
	}

	public void setMapping(List <BoardNodeRelation> mapping) {
		this.mapping = mapping;
	}

	public List <BoardNodeRelation> getMapping() {
		return mapping;
	}
	
	public static void main(String[] args) {
		WumpusBoardMapping maps = new WumpusBoardMapping();
		
		
		BoardNodeRelation r = new BoardNodeRelation(new BoardPosition(1, 1), 3242324);		
		maps.addMapping(r);
		
		r = new BoardNodeRelation(new BoardPosition(1, 2), 432423);
		maps.addMapping(r);
		
		r = new BoardNodeRelation(new BoardPosition(1, 3), 424324132);		
		maps.addMapping(r);		
		
		
		Gson gson = new Gson();
		String json = gson.toJson(maps);
		
		System.out.println(json);
	}
}
