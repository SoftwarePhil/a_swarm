package simulation.common;

import java.io.IOException;

import simulation.agent.behaviors.OnBoundaryBehaviorAttraction;
import simulation.agent.behaviors.SwarmBehaviorNode;
import simulation.field.grassField.Field;

public class startSimulation {

static int amountOfAgents = 100;

public static void main(String[] args) throws IOException{
	
	Field f = new Field(0, amountOfAgents, //0, amountOfAgents = 33, 500, 350, 3, true
						200, 200 , 
						3,//larger value for scaler, make size smaller 
						new SwarmBehaviorNode(true), 
						new OnBoundaryBehaviorAttraction(),
						true
						); 
	
	new Thread(f).start();
}
}
