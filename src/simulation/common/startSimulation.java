package simulation.common;

import java.io.IOException;
import agent.behaviors.OnBoundaryBehaviorAttraction;
import agent.behaviors.SwarmBehaviorNode;
import field.grassField.Field;

public class startSimulation {

static int amountOfAgents = 100;

//:TODO make mode where screen is not dawn
public static void main(String[] args) throws IOException{
	
	Field f = new Field(0, amountOfAgents, 200, 200 , 3, new SwarmBehaviorNode(true), new OnBoundaryBehaviorAttraction()); //larger value for scaler, make size smaller //0,33,500,350,3
	new Thread(f).start();
}
}
