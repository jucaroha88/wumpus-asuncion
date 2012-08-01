package info2.wumpusworld.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.uca.info2.Problems;
import edu.uca.info2.components.ObjetoPosicionable;
import aima.core.util.datastructure.XYLocation;

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
	}
	
	public static void GenerarProblemsFileDeEjemplo(){
		
	}
}
