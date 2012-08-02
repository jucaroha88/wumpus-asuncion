package info2.wumpusworld.plan;

import java.util.ArrayList;
import java.util.List;

public class WumpusPlan {
	List<WumpusPlanAction> plan;
	public WumpusPlan() {
		this.plan = new ArrayList<WumpusPlanAction>();
	}
	public void addAccion(WumpusPlanAction accion){
		plan.add(accion);
	}
}
