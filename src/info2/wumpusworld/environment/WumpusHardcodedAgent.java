package info2.wumpusworld.environment;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.NoOpAction;


public class WumpusHardcodedAgent extends WumpusAgent{
	public WumpusHardcodedAgent() {
		this.program = new WumpusHardcodedAgentProgram();
	}
}

class WumpusHardcodedAgentProgram implements AgentProgram {
	int paso=0;
	Action[] acciones=	{			WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_DISPARAR,
									WumpusEnvironment.ACTION_GIRARDERECHA,
									WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_GIRARIZQUIERDA,
									WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_TOMAR,
									WumpusEnvironment.ACTION_GIRARIZQUIERDA,
									WumpusEnvironment.ACTION_GIRARIZQUIERDA,
									WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_GIRARDERECHA,
									WumpusEnvironment.ACTION_AVANZAR,
									WumpusEnvironment.ACTION_SALIR
								};

	@Override
	public Action execute(Percept percept) {
//		System.out.println(percept.toString());
		if(paso<acciones.length){
			return acciones[paso++];
		}
		else
			return NoOpAction.NO_OP;
	}
	
}