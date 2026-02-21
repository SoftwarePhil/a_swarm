package simulation.agent.brainPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.State;


public class BehaviorManager {

private Behavior swarmBehavior;
private Behavior crashBehavior;
private Behavior currentBehavior;
protected List<Node> nodeList;
private List<PolarCoordinate> newPostions;

public BehaviorManager(Behavior swarmBehavior, Behavior crashBehavior){
	this.swarmBehavior = swarmBehavior;
	this.crashBehavior = crashBehavior;
	newPostions = new ArrayList<PolarCoordinate>();
}

public void setSwarmState(){
	currentBehavior = swarmBehavior;
}

public void setCrashedState(){
	currentBehavior = crashBehavior;
}

public State getCurrentState(){
	return currentBehavior.getNextState(newPostions, nodeList);
}

public void updateDeltaPostions(List <PolarCoordinate> polarCoordinates) throws IOException{
	newPostions = polarCoordinates;
}

public void updateNodeList(List<Node> nodeList){
	this.nodeList = nodeList;
}
}
