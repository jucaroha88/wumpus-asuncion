package info2.wumpusworld.environment;

import java.util.Set;

import aima.core.agent.EnvironmentObject;
import aima.core.agent.impl.DynamicPercept;

public class WumpusEnvPercept extends DynamicPercept{
	public static final String ATTRIBUTE_BRISA="brisa";
	public static final String ATTRIBUTE_HEDOR="hedor";
	public static final String ATTRIBUTE_BRILLO="brillo";
	public static final String ATTRIBUTE_GRITO="grito";
	
	public WumpusEnvPercept(boolean brisa, boolean hedor, boolean brillo, boolean grito) {
		super();
		setAttribute(ATTRIBUTE_BRISA, brisa);
		setAttribute(ATTRIBUTE_HEDOR, hedor);
		setAttribute(ATTRIBUTE_BRILLO, brillo);
		setAttribute(ATTRIBUTE_GRITO, grito);
	}
	
}
