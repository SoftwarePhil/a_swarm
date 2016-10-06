package simulation.common;

import java.io.IOException;

import agent.behaviors.BehaviorManager;
import agent.behaviors.NodeBehavior;
import agent.behaviors.OnBoundaryBehaviorAttraction;
import agent.behaviors.SwarmBehavior;
import agent.behaviors.SwarmBehaviorNode;
import agent.brainPackage.Agent;
import field.grassField.Field;

public class startSimulation {

static int amountOfAgents = 100;

public static void main(String[] args) throws IOException{
	

	Field f = new Field(0, amountOfAgents, 200, 200 , 3, new SwarmBehaviorNode(true), new OnBoundaryBehaviorAttraction()); //larger value for scaler, make size smaller //0,33,500,350,3
	//Field f = new Field(0, a, 200,  200, 4, new SwarmBehaviorNode(true), new OnBoundaryBehaviorAttraction());
	new Thread(f).start();
	
}
}
