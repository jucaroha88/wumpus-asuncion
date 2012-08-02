package info2.wumpusworld.plan;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class WumpusPlans {
	List<WumpusPlan> plans;
	public WumpusPlans() {
		plans = new ArrayList<WumpusPlan>();
	}
	public void addPlan(WumpusPlan plan){
		plans.add(plan);
	}
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
