package info2.wumpusworld.environment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jpl.Atom;
import jpl.Query;
import jpl.Term;
import jpl.Util;
import jpl.Variable;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.test.core.unit.agent.AgentTestSuite;

public class WumpusPrologAgent extends WumpusAgent{
	public WumpusPrologAgent(String plAgentFilename) {
		try{
			this.program = new WumpusPrologAgentProgram(plAgentFilename);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class WumpusPrologAgentProgram implements AgentProgram{
	List<Action> listaAcciones;
	
	public WumpusPrologAgentProgram(String plAgentFilename) throws Exception {
		Query qc = new Query("consult", new Term[] { new Atom(plAgentFilename)});
		if(!qc.query())
			throw new Exception("no se pudo consultar la base de conocimiento "+plAgentFilename);
		Query qi = new Query("iniciar");
		listaAcciones = new ArrayList<Action>();
	}

	@Override
	public Action execute(Percept percept) {
		if(listaAcciones.size()==0){
			WumpusEnvPercept wpercept = (WumpusEnvPercept)percept;
			String olor = ((Boolean)(wpercept.getAttribute(WumpusEnvPercept.ATTRIBUTE_HEDOR))).booleanValue() ? "yes":"no";
			String brisa = ((Boolean)(wpercept.getAttribute(WumpusEnvPercept.ATTRIBUTE_BRISA))).booleanValue() ? "yes":"no";
			String brillo = ((Boolean)(wpercept.getAttribute(WumpusEnvPercept.ATTRIBUTE_BRILLO))).booleanValue() ? "yes":"no";
			Query qe = new Query("enviar_percepciones", new Term[] { new Atom(olor), new Atom(brisa), new Atom(brillo) });
			
			if(!qe.hasSolution()){
				System.err.println("enviar_percepciones devolvio false");
				System.exit(1);
			}
			
			
			Variable varAccionList = new Variable();
			Query qa = new Query("ask_KB", new Term[] { varAccionList });
			
			if(!qa.query()){
				return NoOpAction.NO_OP;
			}

			
			Hashtable<Object, Term> solution = qa.oneSolution();
			Term termino = solution.get(varAccionList);
			/*for(Term t : placcarray){
				listaAcciones.add(prologToAction(t.toString()));
			}*/
//			System.out.print(termino.toString());
		}
		
		Action accion = listaAcciones.get(listaAcciones.size()-1);
		listaAcciones.remove(listaAcciones.size()-1);
		return accion;
	}
	
	Action prologToAction(String prologActionString){
		if(prologActionString.equals("avanzar")){
			return WumpusEnvironment.ACTION_AVANZAR;
		}else if(prologActionString.equals("girarIzq")){
			return WumpusEnvironment.ACTION_GIRARIZQUIERDA;
		}else if(prologActionString.equals("girarDer")){
			return WumpusEnvironment.ACTION_GIRARDERECHA;
		}else if(prologActionString.equals("disparar")){
			return WumpusEnvironment.ACTION_DISPARAR;
		}else if(prologActionString.equals("agarrar")){
			return WumpusEnvironment.ACTION_TOMAR;
		}else{
			//throw new Exception("accion devuelta por prolog no definida");
			System.err.println("prologToAction: accion de prolog no definida");
			System.exit(1);
		}
		return null;
	}
	
}