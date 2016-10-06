package agent.behaviors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import agent.brainPackage.Behavior;
import agent.brainPackage.DeltaPostion;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.State;


public class BehaviorManager {

private Behavior swarmBehavior;
private Behavior crashBehavior;
private Behavior currentBehavior;
private List<DeltaPostion> deltaPostions;
protected List<Node> nodeList;

public BehaviorManager(Behavior swarmBehavior, Behavior crashBehavior){
	this.swarmBehavior = swarmBehavior;
	this.crashBehavior = crashBehavior;
	deltaPostions = new ArrayList<DeltaPostion>();
}

public void setSwarmState(){
	currentBehavior = swarmBehavior;
}

public void setCrashedState(){
	currentBehavior = crashBehavior;
}

public State getCurrentState(){
	return currentBehavior.getNextState(deltaPostions, nodeList);
}

public void updateDeltaPostions(List <PolarCoordinate> polarCoordinates) throws IOException{
	int newSize =  polarCoordinates.size();
	int currentSize = deltaPostions.size();
	 
	if(currentSize == 0){
		for (PolarCoordinate p : polarCoordinates){
			deltaPostions.add(new DeltaPostion(new PolarCoordinate(0,0), p));
		}
	}
	
	else if(newSize > currentSize){
		for(int i = currentSize; i < newSize; i++){
			PolarCoordinate newCoordinate = polarCoordinates.get(i - 1);
			deltaPostions.add(new DeltaPostion(newCoordinate, newCoordinate));
			}
	}
	else if(newSize < currentSize){
		for(int i = newSize; i < currentSize; i++){
			deltaPostions.remove(deltaPostions.size()-1);
		 }
	}
		
	int i = 0;
	for(DeltaPostion dp : deltaPostions){
		dp.newPostion(polarCoordinates.get(i));
		i++;
	}
}

public void updateNodeList(List<Node> nodeList){
	this.nodeList = nodeList;
}
}
