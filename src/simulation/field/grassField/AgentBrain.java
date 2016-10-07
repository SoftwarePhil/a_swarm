package simulation.field.grassField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simulation.agent.brainPackage.BehaviorManager;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.State;

//TODO: this class needs to take all the logic of making local decisions away from AcutalRobot
//right now it is not doing anything
public class AgentBrain {

private BehaviorManager behaviorManager;
private boolean isCrashed = false;

private List<Node> newNodes = new ArrayList<Node>();
private List<PolarCoordinate> newPostions = new ArrayList<PolarCoordinate>();

public AgentBrain(BehaviorManager behaviorManager){
	this.behaviorManager = behaviorManager;
}

public void writeDataToRobot(PolarCoordinate[] p, Node[] n) throws IOException, InterruptedException{
	newPostions = Arrays.asList(p);
	newNodes = Arrays.asList(n);
}

public State calcauteNextRobotState(){
	try {
		behaviorManager.updateDeltaPostions(newPostions);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	behaviorManager.updateNodeList(newNodes);
	
	isCrashed = checkIfCrashed();
	
	if(isCrashed){
		behaviorManager.setCrashedState();
	}
	
	else {
		behaviorManager.setSwarmState();
	}
	
	return behaviorManager.getCurrentState();
}

private boolean checkIfCrashed() {
	if(!newPostions.isEmpty()){
		if(newPostions.get(0).getName().equals("CRASH")){
		return true;
		}
	}
	return false;
}

public void hasCrashed(){
	isCrashed = true;
}

public void notCrashed(){
	isCrashed = false;
}

public boolean getCrashed(){
	return isCrashed;
}
}
