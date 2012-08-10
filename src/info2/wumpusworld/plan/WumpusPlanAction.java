package info2.wumpusworld.plan;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aimax.osm.data.entities.MapNode;

public class WumpusPlanAction {
	Action accion;
	List<WumpusPlanNode> NodosRecorridos;
	public WumpusPlanAction(Action accion) {
		this.accion = accion;
		this.NodosRecorridos = new ArrayList<WumpusPlanNode>();
	}
	public WumpusPlanAction(Action accion, List<MapNode> nodes){
		this(accion);
		for(MapNode node : nodes){
			addNodoRecorrido(node.getId());
		}
	}
	public void addNodoRecorrido(long nodeId){
		NodosRecorridos.add(new WumpusPlanNode(nodeId));
	}
}
