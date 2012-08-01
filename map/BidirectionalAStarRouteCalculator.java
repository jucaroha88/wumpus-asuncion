package info2.wumpusworld.map;


import info2.wumpusworld.environment.WumpusUtils;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.Map;
import aima.core.search.framework.BidirectionalProblem;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;
import aima.core.util.CancelableThread;
import aimax.osm.data.OsmMap;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.impl.DefaultMapNode;
import aimax.osm.routing.OsmMoveAction;
import aimax.osm.routing.OsmSldHeuristicFunction;
import aimax.osm.routing.RouteFindingProblem;
import aimax.osm.routing.agent.MapAdapter;

public class BidirectionalAStarRouteCalculator {

	/** Returns the names of all supported way selection options. */
	public String[] getWaySelectionOptions() {
		return new String[] { "Distance", "Distance (Car)", "Distance (Bike)" };
	}

	/**
	 * Template method, responsible for shortest path generation between two map
	 * nodes. It searches for way nodes in the vicinity of the given nodes which
	 * comply with the specified way selection, searches for a suitable paths,
	 * and adds the paths as tracks to the provided <code>map</code>. The
	 * three factory methods can be used to override aspects of the default
	 * behavior in subclasses if needed.
	 * 
	 * @param locs
	 *            Nodes, not necessarily way nodes. The first node is used as
	 *            start, last node as finish, all others as via nodes.
	 * @param map
	 *            The information source.
	 * @param waySelection
	 *            Number, indicating which kinds of ways are relevant.
	 */
	public List<Position> calculateRoute(List<MapNode> locs, OsmMap map,int waySelection) { //waySelection=1 es para car
		List<Position> result = new ArrayList<Position>();
		try {
			MapWayFilter wayFilter = createMapWayFilter(map, waySelection);
			boolean ignoreOneways = (waySelection == 0);
			
			/*{	//solo mientras tanto para generar el json
				List<MapNode> ml = new ArrayList<MapNode>();
				for(MapNode mn : locs){
					ml.add(map.getNearestWayNode(new Position(mn), wayFilter));
				}
				WumpusUtils.MappingToFileFromMapNodeList(ml, map);
			}*/
			
			MapNode fromNode = map.getNearestWayNode(new Position(locs
					.get(0)), wayFilter);
			result.add(new Position(fromNode.getLat(), fromNode.getLon()));
			for (int i = 1; i < locs.size()
					&& !CancelableThread.currIsCanceled(); i++) {
				MapNode toNode = map.getNearestWayNode(new Position(locs
						.get(i)), wayFilter);
				
				MapAdapter map_adapter = new MapAdapter(map);
				HeuristicFunction hf = createHeuristicFunction(toNode,
						map_adapter);
				Problem problem = createProblem(fromNode, toNode, map,
						wayFilter, ignoreOneways, waySelection);
				
				Search search = new AStarBidirectionalSearch(hf);
				List<Action> actions = search.search(problem);
				
				if (actions.isEmpty())
					break;
				for (Object action : actions) {
					if (action instanceof OsmMoveAction) {
						OsmMoveAction a = (OsmMoveAction) action;
						for (MapNode node : a.getNodes())
							if (!node.equals(a.getFrom()))
								result.add(new Position(node.getLat(), node
										.getLon()));
					}
				}
				fromNode = toNode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Factory method, responsible for way filter creation. */
	protected MapWayFilter createMapWayFilter(OsmMap map, int waySelection) {
		if (waySelection == 1)
			return MapWayAttFilter.createCarWayFilter();
		else if (waySelection == 2)
			return MapWayAttFilter.createBicycleWayFilter();
		else
			return MapWayAttFilter.createAnyWayFilter();
	}

	/** Factory method, responsible for heuristic function creation. */
	protected HeuristicFunction createHeuristicFunction(MapNode toRNode,
			MapAdapter map_adapter) {
		return new OsmSldHeuristicFunctionAdapter(toRNode, map_adapter);
	}

	/** Factory method, responsible for problem creation. */
	protected Problem createProblem(MapNode fromNode, MapNode toNode,
			OsmMap map, MapWayFilter wayFilter, boolean ignoreOneways,
			int waySelection) {
		/*return new RouteFindingProblem(fromNode, toNode, wayFilter,
				ignoreOneways);*/
		Map map_adapter = new MapAdapter(map);
		String initialstate = Long.toString(fromNode.getId());
		String goalstate = Long.toString(toNode.getId());
		return new BidirectionalMapProblem(map_adapter, initialstate, goalstate);
	}
}

/* adaptador para poder usar como funcion heuristica
 * donde el state sea un lugar de Map
 */
class OsmSldHeuristicFunctionAdapter implements HeuristicFunction{
	OsmSldHeuristicFunction OsmHF;
	MapAdapter map_adapter;
	
	public OsmSldHeuristicFunctionAdapter(MapNode toRNode, MapAdapter map_adapter) {
		this.OsmHF = new OsmSldHeuristicFunction(toRNode);
		this.map_adapter = map_adapter;
	}
	
	@Override
	public double h(Object state) {
		MapNode node = map_adapter.getWayNode(state.toString());
		return OsmHF.h(node);
	}
}