package info2.wumpusworld;

import java.io.IOException;
import java.util.List;


import edu.uca.info2.Problems;
import edu.uca.info2.components.Problem;
import info2.wumpusworld.environment.WumpusEnvironment;
import info2.wumpusworld.environment.WumpusHardcodedAgent;
import info2.wumpusworld.environment.WumpusLocTrackEnvView;
import info2.wumpusworld.environment.WumpusPrologAgent;
import info2.wumpusworld.environment.WumpusUtils;
import info2.wumpusworld.map.BidirectionalAStarRouteCalculator;
import info2.wumpusworld.plan.WumpusPlan;
import info2.wumpusworld.plan.WumpusPlans;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;

public class WumpusWorld {
	static final boolean useHardcodedAgent = true;
	public static void main(String[] args) {
		String problemsfile = "problems.json";
		String osmfile = "ulm.osm";
		String mappingfile = "mapping.json";
		String plAgentFile = "wumpus.pl";
		
		// TODO cargar mundo de wumpus desde archivo
		Problems problems = null;
		try{
			problems = WumpusUtils.ProblemsFromFile(problemsfile);
		}catch(IOException e){
			e.printStackTrace();
		}
		List<Problem> pl=problems.getProblems();
		WumpusPlans plans = new WumpusPlans();
		for(Problem problem : pl){
			Environment env = new WumpusEnvironment();
			WumpusLocTrackEnvView view= new WumpusLocTrackEnvView(osmfile, mappingfile);
			env.addEnvironmentView(view);
			if(useHardcodedAgent)
				env.addAgent(new WumpusHardcodedAgent());
			else
				env.addAgent(new WumpusPrologAgent(plAgentFile));
			env.stepUntilDone();
			plans.addPlan(view.getPlan());
//			System.out.println(view.toString());
		}
		System.out.println(plans.toJson());
		

		// TODO generar lista de MapNode y calcular ruta
		/*BidirectionalAStarRouteCalculator rc = new BidirectionalAStarRouteCalculator();
		rc.calculateRoute(view.toMapNodeList(), map, waySelection);*/
		
	}
}
