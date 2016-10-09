package simulation.common;

import java.io.IOException;

import simulation.agent.behaviors.OnBoundaryBehaviorAttraction;
import simulation.agent.behaviors.SwarmBehavior;
import simulation.agent.behaviors.SwarmBehaviorNode;
import simulation.field.grassField.Field;

public class startSimulation {

static int amountOfAgents = 50;

public static void main(String[] args) throws IOException{
	
	Field f = new Field(0, amountOfAgents,
						150, 150, //length, width 
						1,//larger value for scaler, make size smaller 
						new SwarmBehaviorNode(true), //pick a behavior for the swarm 
						new OnBoundaryBehaviorAttraction(), //pick an edge behavior
						true //draw GUI?
						); 
	
	new Thread(f).start();
}
}
