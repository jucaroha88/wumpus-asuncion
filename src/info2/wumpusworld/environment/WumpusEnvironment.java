package info2.wumpusworld.environment;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import edu.uca.info2.components.ObjetoPosicionable;
import edu.uca.info2.components.Problem;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentObject;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.xyenv.Wall;
import aima.core.environment.xyenv.XYEnvironment;
import aima.core.util.datastructure.XYLocation;

public class WumpusEnvironment extends AbstractEnvironment {
	private WumpusEnvironmentState envState = null;
	
	boolean terminado = false;
	
	static final int _WIDTH=4;
	static final int _HEIGHT=4;
	
	public static final Action ACTION_AVANZAR = new DynamicAction("Avanzar");
	public static final Action ACTION_DISPARAR = new DynamicAction("Disparar");
	public static final Action ACTION_GIRARIZQUIERDA = new DynamicAction("GirarIzquierda");
	public static final Action ACTION_GIRARDERECHA = new DynamicAction("GirarDerecha");
	public static final Action ACTION_TOMAR = new DynamicAction("Tomar");
	public static final Action ACTION_SALIR = new DynamicAction("Salir");
	
	public static final XYLocation INITIAL_AGENT_LOCATION = new XYLocation(1,4);

	public WumpusEnvironment() {
		envState = new WumpusEnvironmentState(_WIDTH,_HEIGHT);
		
		/* eventualmente tenemos que cargar con el archivo jason...
		 * por el momento los objetos estaran hardcoded
		 */
		//addObjectToLocation(anAgent, new XYLocation(1,4));
		addObjectToLocation(new WumpusEnvObject(), new XYLocation(1, 2));
		addObjectToLocation(new GoldEnvObject(), new XYLocation(2,2));
		addObjectToLocation(new PitEnvObject(), new XYLocation(4,1));
		addObjectToLocation(new PitEnvObject(), new XYLocation(3,2));
		addObjectToLocation(new PitEnvObject(), new XYLocation(3,4));
	}
	
	public WumpusEnvironment(edu.uca.info2.components.Problem problem){
		ObjetoPosicionable op=null;
		op = problem.getWumpus();
		if(op != null)
			addObjectToLocation(new WumpusEnvObject(), WumpusUtils.ObjetoPosicionableToXYLocation(op));
		op=problem.getGold();
		if(op != null)
			addObjectToLocation(new GoldEnvObject(), WumpusUtils.ObjetoPosicionableToXYLocation(op));
		for(ObjetoPosicionable pit : problem.getPits())
			addObjectToLocation(new PitEnvObject(), WumpusUtils.ObjetoPosicionableToXYLocation(pit));
	}
	
	@Override
	public void addAgent(Agent a) {
		addObjectToLocation(a, INITIAL_AGENT_LOCATION);
	}
	

	@Override
	public EnvironmentState getCurrentState() {
		return envState;
	}

	@Override
	public EnvironmentState executeAction(Agent a, Action action) {
		if(ACTION_AVANZAR == action){
			moveObject(a, getAgentDirection(a));
			// SI HAY WUMPUS O PIT EL AGENTE MUERE
			XYLocation curloc = getCurrentLocationFor(a);
			Set<EnvironmentObject> objshere = getObjectsAt(curloc);
			for(EnvironmentObject o : objshere){
				if(o instanceof WumpusEnvObject || o instanceof PitEnvObject){
					// TODO MATAR AGENTE
				}
			}	
		}else if(ACTION_GIRARIZQUIERDA == action){
			rotateAgentLeft(a);
		}else if(ACTION_GIRARDERECHA == action){
			rotateAgentRight(a);
		}else if(ACTION_TOMAR == action){
			XYLocation curloc = getCurrentLocationFor(a);
			Set<EnvironmentObject> objshere = getObjectsAt(curloc);
			EnvironmentObject gold = null;
			for(EnvironmentObject o : objshere){
				if(o instanceof GoldEnvObject){
					gold = o;
				}
			}
			if(gold != null)
				removeEnvironmentObject(gold);
		}else if(ACTION_DISPARAR == action){
			EnvironmentObject wump = getWumpusEnLaMira(a);
			if(wump != null && ((WumpusAgent)a).hasFlecha()){
				((WumpusAgent)a).setFlecha(false);
				removeEnvironmentObject(wump);
			}
		}else if(ACTION_SALIR == action){
			terminar();
		}
		return envState;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		boolean brisa=false,hedor=false,brillo=false,grito=false;
		Set<EnvironmentObject> objsset = getObjectsNear(anAgent, 1);
		//vemos si hay wumpus o pit alrededor
		for(EnvironmentObject eo : objsset){
			if(eo instanceof PitEnvObject){
				brisa=true;
			}
			if(eo instanceof WumpusEnvObject){
				hedor=true;
			}
		}
		//vemos si el oro esta en esta casilla
		objsset=getObjectsAt(getCurrentLocationFor(anAgent));
		for(EnvironmentObject eo : objsset){
			if(eo instanceof GoldEnvObject){
				brillo=true;
			}
		}
		//vemos si hay grito
		if(getGrito()){
			grito=true;
		}
		return new WumpusEnvPercept(brisa,hedor,brillo,grito);
	}

	public void addObjectToLocation(EnvironmentObject eo, XYLocation loc) {
		moveObjectToAbsoluteLocation(eo, loc);
		if(eo instanceof Agent){
			envState.setAgentDirection((Agent)eo, XYLocation.Direction.North);
		}
	}
	
	@Override
	public void removeEnvironmentObject(EnvironmentObject eo) {
		super.removeEnvironmentObject(eo);
		envState.removeObject(eo);
	}

	public void moveObjectToAbsoluteLocation(EnvironmentObject eo,
			XYLocation loc) {
		// Ensure the object is not already at a location
		envState.moveObjectToAbsoluteLocation(eo, loc);

		// Ensure is added to the environment
		addEnvironmentObject(eo);
	}

	public void moveObject(EnvironmentObject eo, XYLocation.Direction direction) {
		XYLocation presentLocation = envState.getCurrentLocationFor(eo);

		if (null != presentLocation) {
			XYLocation locationToMoveTo = presentLocation.locationAt(direction);
			if (!(isBlocked(locationToMoveTo))) {
				moveObjectToAbsoluteLocation(eo, locationToMoveTo);
			}
		}
	}

	public XYLocation getCurrentLocationFor(EnvironmentObject eo) {
		return envState.getCurrentLocationFor(eo);
	}

	public Set<EnvironmentObject> getObjectsAt(XYLocation loc) {
		return envState.getObjectsAt(loc);
	}

	public Set<EnvironmentObject> getObjectsNear(Agent agent, int radius) {
		return envState.getObjectsNear(agent, radius);
	}
	
	public void matarAgente(Agent a){
		a.setAlive(false);
	}
	
	public void terminar(){
		this.terminado=true;
	}
	
	@Override
	public boolean isDone() {
		return super.isDone() || this.terminado;
	}
	
	public void rotateAgentRight(Agent anAgent){
		XYLocation.Direction dir = getAgentDirection(anAgent);
		if(dir.equals(XYLocation.Direction.North)){
			setAgentDirection(anAgent, XYLocation.Direction.East);
		}else if(dir.equals(XYLocation.Direction.East)){
			setAgentDirection(anAgent, XYLocation.Direction.South);
		}else if(dir.equals(XYLocation.Direction.South)){
			setAgentDirection(anAgent, XYLocation.Direction.West);
		}else if(dir.equals(XYLocation.Direction.West)){
			setAgentDirection(anAgent, XYLocation.Direction.North);
		}
	}
	
	public void rotateAgentLeft(Agent anAgent){
		XYLocation.Direction dir = getAgentDirection(anAgent);
		if(dir.equals(XYLocation.Direction.North)){
			setAgentDirection(anAgent, XYLocation.Direction.West);
		}else if(dir.equals(XYLocation.Direction.East)){
			setAgentDirection(anAgent, XYLocation.Direction.North);
		}else if(dir.equals(XYLocation.Direction.South)){
			setAgentDirection(anAgent, XYLocation.Direction.East);
		}else if(dir.equals(XYLocation.Direction.West)){
			setAgentDirection(anAgent, XYLocation.Direction.South);
		}
	}
	
	public XYLocation.Direction getAgentDirection(Agent anAgent){
		return envState.getAgentDirection(anAgent);
	}
	
	public void setAgentDirection(Agent anAgent, XYLocation.Direction direction){
		envState.setAgentDirection(anAgent, direction);
	}
	
	public boolean getGrito(){
		return envState.getGrito();
	}
	
	public void setGrito(boolean value){
		envState.setGrito(value);
	}
	
	/*
	 * devuelve un WumpusEnvObject, si existe, en la trayectoria de la flecha del agente
	 */
	public EnvironmentObject getWumpusEnLaMira(Agent agent){
		XYLocation actloc = getCurrentLocationFor(agent);
		XYLocation.Direction dir = getAgentDirection(agent);
		while(!isFuera(actloc)){
			for(EnvironmentObject eo : getObjectsAt(actloc)){
				if(eo instanceof WumpusEnvObject){
					return eo;
				}
			}
			actloc = actloc.locationAt(dir);
		}
		return null;
	}
	
	/*
	 * si un XYLocation esta fuera del mapa
	 * TODO VERIFICAR SI ESTAN BIEN LOS RANGOS
	 */
	public boolean isFuera(XYLocation loc) {
		if(		loc.getXCoOrdinate() < 1
				|| loc.getXCoOrdinate() > _WIDTH
				|| loc.getYCoOrdinate() < 1
				|| loc.getYCoOrdinate() > _WIDTH) {
			return true;
		}
		return false;
	}

	public boolean isBlocked(XYLocation loc) {
		for (EnvironmentObject eo : envState.getObjectsAt(loc)) {
			if (eo instanceof Wall) {
				return true;
			}
		}
		return false;
	}

	public void makePerimeter() {
		for (int i = 0; i < envState.width; i++) {
			XYLocation loc = new XYLocation(i, 0);
			XYLocation loc2 = new XYLocation(i, envState.height - 1);
			envState.moveObjectToAbsoluteLocation(new Wall(), loc);
			envState.moveObjectToAbsoluteLocation(new Wall(), loc2);
		}

		for (int i = 0; i < envState.height; i++) {
			XYLocation loc = new XYLocation(0, i);
			XYLocation loc2 = new XYLocation(envState.width - 1, i);
			envState.moveObjectToAbsoluteLocation(new Wall(), loc);
			envState.moveObjectToAbsoluteLocation(new Wall(), loc2);
		}
	}

}

class WumpusEnvObject implements EnvironmentObject {
	@Override
	public String toString() {
		return "W";
	}
}
class PitEnvObject implements EnvironmentObject {
	@Override
	public String toString() {
		return "P";
	}
}
class GoldEnvObject implements EnvironmentObject {
	@Override
	public String toString() {
		return "G";
	}
}


class WumpusEnvironmentState implements EnvironmentState {
	int width;
	int height;
	
	boolean grito;

	private Map<XYLocation, Set<EnvironmentObject>> objsAtLocation = new LinkedHashMap<XYLocation, Set<EnvironmentObject>>();
	private Map<Agent, XYLocation.Direction> agentDirection = new LinkedHashMap<Agent, XYLocation.Direction>();

	public WumpusEnvironmentState(int width, int height) {
		this.width = width;
		this.height = height;
		grito=false;
		for (int h = 1; h <= height; h++) {
			for (int w = 1; w <= width; w++) {
				objsAtLocation.put(new XYLocation(h, w),
						new LinkedHashSet<EnvironmentObject>());
			}
		}
	}
	
	public void setAgentDirection(Agent anAgent, XYLocation.Direction direccion){
		agentDirection.put(anAgent, direccion);
	}
	
	public XYLocation.Direction getAgentDirection(Agent anAgent){
		return agentDirection.get(anAgent);
	}
	
	public boolean getGrito(){
		return this.grito;
	}
	
	public void setGrito(boolean value){
		this.grito=value;
	}

	public void moveObjectToAbsoluteLocation(EnvironmentObject eo,
			XYLocation loc) {
		// Ensure is not already at another location
		for (Set<EnvironmentObject> eos : objsAtLocation.values()) {
			if (eos.remove(eo)) {
				break; // Should only every be at 1 location
			}
		}
		// Add it to the location specified
		getObjectsAt(loc).add(eo);
	}
	
	public void removeObject(EnvironmentObject eo){
		for (Set<EnvironmentObject> eos : objsAtLocation.values()) {
			eos.remove(eo);
		}
	}

	public Set<EnvironmentObject> getObjectsAt(XYLocation loc) {
		Set<EnvironmentObject> objectsAt = objsAtLocation.get(loc);
		if (null == objectsAt) {
			// Always ensure an empty Set is returned
			objectsAt = new LinkedHashSet<EnvironmentObject>();
			objsAtLocation.put(loc, objectsAt);
		}
		return objectsAt;
	}

	public XYLocation getCurrentLocationFor(EnvironmentObject eo) {
		for (XYLocation loc : objsAtLocation.keySet()) {
			if (objsAtLocation.get(loc).contains(eo)) {
				return loc;
			}
		}
		return null;
	}

	public Set<EnvironmentObject> getObjectsNear(Agent agent, int radius) {
		Set<EnvironmentObject> objsNear = new LinkedHashSet<EnvironmentObject>();

		XYLocation agentLocation = getCurrentLocationFor(agent);
		for (XYLocation loc : objsAtLocation.keySet()) {
			if (withinRadius(radius, agentLocation, loc)) {
				objsNear.addAll(objsAtLocation.get(loc));
			}
		}
		// Ensure the 'agent' is not included in the Set of
		// objects near
		objsNear.remove(agent);

		return objsNear;
	}

	@Override
	public String toString() {
		return "XYEnvironmentState:" + objsAtLocation.toString();
	}

	//
	// PRIVATE METHODS
	//
	private boolean withinRadius(int radius, XYLocation agentLocation,
			XYLocation objectLocation) {
		int xdifference = agentLocation.getXCoOrdinate()
				- objectLocation.getXCoOrdinate();
		int ydifference = agentLocation.getYCoOrdinate()
				- objectLocation.getYCoOrdinate();
		return Math.sqrt((xdifference * xdifference)
				+ (ydifference * ydifference)) <= radius;
	}
}
