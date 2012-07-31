package info2.wumpusworld.environment;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.util.datastructure.XYLocation;
import aimax.osm.data.entities.MapNode;


/*
 * mantiene la lista de posiciones visitadas por el agente
 * la lista se puede retirar con el metodo getLocationList
 */
public class WumpusLocTrackEnvView implements EnvironmentView{
	ArrayList<XYLocation> listapasos = null;
	public WumpusLocTrackEnvView() {
		listapasos = new ArrayList<XYLocation>();
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
