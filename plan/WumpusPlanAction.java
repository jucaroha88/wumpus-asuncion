package info2.wumpusworld.plan;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

public class WumpusPlanAction {
	Action accion;
	List<WumpusPlanNode> NodosRecorridos;
	public WumpusPlanAction(Action accion) {
		this.accion = accion;
		this.NodosRecorridos = new ArrayList<WumpusPlanNode>();
	}
	public void addNodoRecorrido(long nodeId){
		NodosRecorridos.add(new WumpusPlanNode(nodeId));
	}
}
