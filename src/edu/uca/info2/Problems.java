package edu.uca.info2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.uca.info2.components.Gold;
import edu.uca.info2.components.Pit;
import edu.uca.info2.components.Problem;
import edu.uca.info2.components.Wumpus;

public class Problems {
	private ArrayList<Problem> problems;
	
	public Problems() {		
		setProblems(new ArrayList<Problem>());
	}

	public void setProblems(ArrayList<Problem> problems) {
		this.problems = problems;
	}

	public ArrayList<Problem> getProblems() {
		return problems;
	}
	
	public void addProblem(Problem p) {
		problems.add(p);
	}
	
	public static void main(String[] args) {
		Problems test1 = new Problems();
		
		test1.addProblem1();
		test1.addProblem2();
		
		Gson gson = new Gson();
		String json = gson.toJson(test1);
		
		System.out.println(json);
		
	}
	
	
	private void addProblem1(){
		Problem p =new Problem();

		Wumpus w = new Wumpus();
		w.setRow(5);
		w.setCol(6);
		p.setWumpus(w);
		
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
		
		Gold g = new Gold();
		g.setRow(2);
		g.setCol(10);
		
		p.setGold(g);
		
		problems.add(p);	
		
	}
	
	private void addProblem2(){
		Problem p =new Problem();

		Wumpus w = new Wumpus();
		w.setRow(10);
		w.setCol(10);
		p.setWumpus(w);
		
		ArrayList<Pit> pits = new ArrayList<Pit>();
		
		Pit pit = new Pit();
		pit.setRow(1);
		pit.setCol(3);		
		pits.add(pit);		
		pit = new Pit();
		
		pit.setRow(3);
		pit.setCol(1);		
		pits.add(pit);		
		
		p.setPits(pits);	
		
		Gold g = new Gold();
		g.setRow(9);
		g.setCol(10);
		
		p.setGold(g);
		
		problems.add(p);	
		
	}	
}
