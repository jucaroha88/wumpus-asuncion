package info2.wumpusworld.environment;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.test.core.unit.agent.AgentTestSuite;

public class WumpusPrologAgent extends WumpusAgent{
	public WumpusPrologAgent() {
		this.program = new WumpusPrologAgentProgram();
	}
}

class WumpusPrologAgentProgram implements AgentProgram {

	@Override
	public Action execute(Percept percept) {
		// TODO Auto-generated method stub
		return null;
	}
	
}