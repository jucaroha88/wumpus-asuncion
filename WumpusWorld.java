package info2.wumpusworld;

import info2.wumpusworld.environment.WumpusEnvironment;
import info2.wumpusworld.environment.WumpusHardcodedAgent;
import info2.wumpusworld.environment.WumpusLocTrackEnvView;
import info2.wumpusworld.map.BidirectionalAStarRouteCalculator;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;

public class WumpusWorld {
	public static void main(String[] args) {
		String problemsfile = "";
		String osmfile = "";
		String mappingfile = "";
		// TODO cargar mundo de wumpus desde archivo
		Environment env = new WumpusEnvironment();
		WumpusLocTrackEnvView view= new WumpusLocTrackEnvView();
		env.addEnvironmentView(view);
		env.addAgent(new WumpusHardcodedAgent());
		env.stepUntilDone();
		
		System.out.print(view.toString());
		

		// TODO generar lista de MapNode y calcular ruta
		/*BidirectionalAStarRouteCalculator rc = new BidirectionalAStarRouteCalculator();
		rc.calculateRoute(view.toMapNodeList(), map, waySelection);*/
		
	}
}
