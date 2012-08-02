package info2.wumpusworld.environment;

import info2.wumpusworld.plan.WumpusPlan;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.util.datastructure.XYLocation;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.data.impl.DefaultMapBuilder;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;
import aimax.osm.routing.agent.MapAdapter;


/*
 * mantiene la lista de posiciones visitadas por el agente
 * la lista se puede retirar con el metodo getLocationList
 */
public class WumpusLocTrackEnvView implements EnvironmentView{
	OsmMap osmmap=null;
	ArrayList<XYLocation> listapasos = null;
	WumpusPlan plan = null;
	
	/*
	 * @param osmmapfilename nombre del archivo de mapa osm
	 */
	public WumpusLocTrackEnvView(String osmmapfilename) {
		listapasos = new ArrayList<XYLocation>();
		plan = new WumpusPlan();
		try{
			InputStream is = new FileInputStream(osmmapfilename);
			readMap(is);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** Reads a map from the specified stream and stores it in {@link #map}. */
	public void readMap(InputStream stream) {
		if (stream != null) {
			MapReader mapReader = new Bz2OsmReader();
			MapBuilder mapBuilder = new DefaultMapBuilder();
			mapReader.readMap(stream, mapBuilder);
			osmmap = mapBuilder.buildMap();
		}
		else
			System.err.println("Map reading failed because input stream does not exist.");
	}
	
	public List<MapNode> toMapNodeList(){
		// TODO
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + listapasos.toString();
	}
	
	public ArrayList<XYLocation> getListaPasos(){
		return listapasos;
	}
	
	@Override
	public void notify(String msg) {
		
	}

	@Override
	public void agentAdded(Agent agent, EnvironmentState resultingState) {
		XYLocation newloc = ((WumpusEnvironmentState)resultingState).getCurrentLocationFor(agent);
		listapasos.add(newloc);
	}

	@Override
	public void agentActed(Agent agent, Action action,
			EnvironmentState resultingState) {
		// TODO Auto-generated method stub
		System.out.println(action.toString());
		if(action==WumpusEnvironment.ACTION_AVANZAR){
			XYLocation newloc = ((WumpusEnvironmentState)resultingState).getCurrentLocationFor(agent);
			listapasos.add(newloc);
		}
	}

}
