package edu.uca.info2.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class Problem {
	private Wumpus wumpus; 
	private Gold gold;	
	private List <Pit> pits;	
	
	public static void main(String[] args) {
		
		Problem p = new Problem();
		
		ObjetoPosicionable w = new Wumpus();
		w.setCol(2);
		w.setRow(3);		
		p.setWumpus((Wumpus)w);
		
		Gold g = new Gold();
		
		g.setCol(1);
		g.setRow(2);

		p.setGold(g);
		
		ArrayList<Pit> pits = new ArrayList<Pit>();
		
		Pit pit = new Pit();
		
		pit.setRow(10);
		pit.setCol(10);		
		pits.add(pit);
		
		pit = new Pit();
		
		pit.setRow(9);
		pit.setCol(9);		
		pits.add(pit);		
		
		p.setPits(pits);
		
		Gson gson = new Gson();
		String json = gson.toJson(p);
		
		System.out.println(json);
		
		Problem p2 = gson.fromJson(json, Problem.class);
		
		System.out.println("Row: " + p2.getWumpus().getRow());		
	}
	

	public void setWumpus(Wumpus wumpus) {
		this.wumpus = wumpus;
	}

	public Wumpus getWumpus() {
		return wumpus;
	}

	public void setGold(Gold gold) {
		this.gold = gold;
	}

	public Gold getGold() {
		return gold;
	}

	public void setPits(List <Pit> pits) {
		this.pits = pits;
	}

	public List <Pit> getPits() {
		return pits;
	}
}
