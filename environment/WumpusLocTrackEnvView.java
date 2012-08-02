package info2.wumpusworld.environment;

import info2.wumpusworld.map.BidirectionalAStarRouteCalculator;
import info2.wumpusworld.plan.WumpusPlan;
import info2.wumpusworld.plan.WumpusPlanAction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.uca.info2.WumpusBoardMapping;

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
	WumpusBoardMapping mapping=null;
	ArrayList<XYLocation> listapasos = null;
	WumpusPlan plan = null;
	XYLocation lastloc=null;
	
	/*
	 * @param osmmapfilename nombre del archivo de mapa osm
	 */
	public WumpusLocTrackEnvView(String osmmapfilename,String mappingfilename) {
		listapasos = new ArrayList<XYLocation>();
		plan = new WumpusPlan();
		try{
			InputStream is = new FileInputStream(osmmapfilename);
			readMap(is);
			loadMapping(mappingfilename);
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
			this.osmmap = mapBuilder.buildMap();
		}
		else
			System.err.println("Map reading failed because input stream does not exist.");
	}
	
	public void loadMapping(String mappingfilename){
		Gson gson = new Gson();
		this.mapping=gson.fromJson(mappingfilename, WumpusBoardMapping.class);
	}
	
	public WumpusPlan getPlan(){
		return this.plan;
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
		lastloc = newloc;
	}

	@Override
	public void agentActed(Agent agent, Action action,
			EnvironmentState resultingState) {
		System.out.println(action.toString());
		if(action==WumpusEnvironment.ACTION_AVANZAR){
			XYLocation newloc = ((WumpusEnvironmentState)resultingState).getCurrentLocationFor(agent);
			listapasos.add(newloc);
			
			//agregamos el paso al plan
			MapNode fromNode = WumpusUtils.XYLocationToMapNode(mapping, lastloc);
			MapNode toNode = WumpusUtils.XYLocationToMapNode(mapping, newloc);
			
			BidirectionalAStarRouteCalculator rc = new BidirectionalAStarRouteCalculator();
			List<MapNode> recorrido = rc.findNodePath(fromNode, toNode, osmmap);
			plan.addAccion(new WumpusPlanAction(action, recorrido));
			
			
			lastloc=newloc;
		}else{
			plan.addAccion(new WumpusPlanAction(action));
		}
	}

}
