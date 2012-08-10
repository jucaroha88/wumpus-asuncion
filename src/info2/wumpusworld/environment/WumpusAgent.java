package info2.wumpusworld.environment;

import aima.core.agent.impl.AbstractAgent;

public abstract class WumpusAgent extends AbstractAgent {
	boolean flecha;
	public WumpusAgent() {
		super();
		flecha=true;
	}
	public boolean hasFlecha() {
		return flecha;
	}
	public void setFlecha(boolean flecha) {
		this.flecha = flecha;
	}
	
}
