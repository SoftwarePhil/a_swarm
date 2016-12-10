package simulation.common;

import java.io.IOException;
import simulation.agent.behaviors.OnBoundaryBehaviorAttraction;
import simulation.agent.behaviors.SwarmBehavior;
import simulation.agent.behaviors.SwarmBehaviorNode;
import simulation.field.grassField.Field;

public class startSimulation {

public static void main(String[] args) throws IOException{
	
	Field f = new Field(100, // amount of agents
						500, 500, //length, width 
						1,//larger, things drawn bigger  
						new SwarmBehavior(false, .999f), //pick a behavior for the swarm 
						new OnBoundaryBehaviorAttraction(), //pick an edge behavior
						false //draw GUI?
						); 
	
	new Thread(f).start();
}
}
