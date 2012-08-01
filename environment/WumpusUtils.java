package info2.wumpusworld.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.uca.info2.Problems;
import edu.uca.info2.WumpusBoardMapping;
import edu.uca.info2.components.BoardNodeRelation;
import edu.uca.info2.components.BoardPosition;
import edu.uca.info2.components.Gold;
import edu.uca.info2.components.ObjetoPosicionable;
import edu.uca.info2.components.Pit;
import edu.uca.info2.components.Problem;
import edu.uca.info2.components.Wumpus;
import aima.core.util.datastructure.XYLocation;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.impl.DefaultMapNode;

public abstract class WumpusUtils {
	public static XYLocation ObjetoPosicionableToXYLocation(ObjetoPosicionable op){
		return new XYLocation(op.getCol(), op.getRow());
	}
	
	public static Problems ProblemsFromFile(String filename) throws FileNotFoundException, IOException, JsonSyntaxException{
		Reader fr = new BufferedReader(new FileReader(filename));
		Gson gson = new Gson();
		Problems problems = gson.fromJson(fr, Problems.class);
		return problems;
	}
	
	public static void ProblemsToFile(String filename, Problems problems) throws IOException{
		Writer fw = new BufferedWriter(new FileWriter(filename));
		Gson gson = new Gson();
		fw.write(gson.toJson(problems));
		fw.flush();
	}

	/*
	 * genera el archivo mapping.json a partir del List<DefaultMapNode>, hasta 4x4 nodos.
	 *  el primer elemento es (1,1), el siguiente (1,2), etc. en notacion de matriz (fila, columna)
	 */
	public static void MappingToFileFromMapNodeList(List<MapNode> nodelist, OsmMap map){
		String filename = "mapping.json";
		BoardNodeRelation rel = null;
		//generamos el WumpusBoardMapping
		WumpusBoardMapping bm = new WumpusBoardMapping();
		int fil=1,col=1;
		for(MapNode mn : nodelist){
			rel = new BoardNodeRelation(new BoardPosition(fil, col), mn.getId());
			bm.addMapping(rel);
			col++;
			if(col>4){
				fil++;
				col=1;
			}
			if(fil>4)
				break;
		}
		//escribimos al json
		try{
			Writer fw = new BufferedWriter(new FileWriter(filename));
			Gson gson = new Gson();
			fw.write(gson.toJson(bm));
			fw.flush();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static Problems GenerarProblemsDeEjemplo(){
		Problems problems = new Problems();
		problems.addProblem(GenerarProblemDeEjemplo1());
		problems.addProblem(GenerarProblemDeEjemplo2());
		return problems;
	}
	
	public static Problem GenerarProblemDeEjemplo1(){
		Wumpus wump = null;
		Gold gold = null;
		Pit pit = null;
		ArrayList<Pit> pits = null;
		Problem problem = null;
		
		//wump
		wump = new Wumpus();
		wump.setCol(1);
		wump.setRow(2);
		//gold
		gold = new Gold();
		gold.setCol(2);
		gold.setRow(2);
		//problem
		problem = new Problem();
		problem.setWumpus(wump);
		problem.setGold(gold);
		//pits
		pits=new ArrayList<Pit>();
		//pit 1
		pit = new Pit();
		pit.setCol(4);
		pit.setRow(1);
		pits.add(pit);
		//pit 2
		pit = new Pit();
		pit.setCol(3);
		pit.setRow(2);
		pits.add(pit);
		//pit 3
		pit = new Pit();
		pit.setCol(3);
		pit.setRow(4);
		pits.add(pit);
		//set pits
		problem.setPits(pits);
		
		return problem;	
	}
	
	public static Problem GenerarProblemDeEjemplo2(){
		Wumpus wump = null;
		Gold gold = null;
		Pit pit = null;
		ArrayList<Pit> pits = null;
		Problem problem = null;
		
		//wump
		wump = new Wumpus();
		wump.setCol(1);
		wump.setRow(2);
		//gold
		gold = new Gold();
		gold.setCol(4);
		gold.setRow(3);
		//problem
		problem = new Problem();
		problem.setWumpus(wump);
		problem.setGold(gold);
		//pits
		pits=new ArrayList<Pit>();
		//pit 1
		pit = new Pit();
		pit.setCol(4);
		pit.setRow(1);
		pits.add(pit);
		//pit 2
		pit = new Pit();
		pit.setCol(1);
		pit.setRow(3);
		pits.add(pit);
		//pit 3
		pit = new Pit();
		pit.setCol(3);
		pit.setRow(3);
		pits.add(pit);
		//set pits
		problem.setPits(pits);
		
		return problem;	
	}
	
}
