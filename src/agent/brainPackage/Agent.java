package agent.brainPackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import field.getJsonData.RobotData;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;
import simulation.common.State;
import agent.behaviors.BehaviorManager;

public class Agent implements Runnable{

private State state;
private boolean crashed;
private List<PolarCoordinate> newPostions = new ArrayList<PolarCoordinate>();
private List<Node> newNodes = new ArrayList<Node>();

private BehaviorManager behaviorManager;

public Agent(String linkData, int socketNumber, BehaviorManager behaviorManager) throws IOException {
	state = new State(0, Speed.VERYSLOW);
	crashed = false;
	
	this.behaviorManager = behaviorManager;
}

public void run(){
	while(true){
		try {
			synchronized(this){ 
				this.wait();
			}
		
			behaviorManager.updateDeltaPostions(newPostions);
			behaviorManager.updateNodeList(newNodes);
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		crashed = checkIfCrashed();
		if(crashed){
				behaviorManager.setCrashedState();
			}
		
		else {
			behaviorManager.setSwarmState();
			
		}
		state = behaviorManager.getCurrentState();
	}
	}

private boolean checkIfCrashed() {
		if(!newPostions.isEmpty()){
			if(newPostions.get(0).getName().equals("CRASH")){
			return true;
			}
		}
		return false;
	}
	
public State getRobotState(){
	System.out.println("state was gotton .. ");
	return state;
}

public void updateRobotData(RobotData robotData){
	//System.out.println("updating robot data ..");
	newPostions = Arrays.asList(robotData.getPolarCoordinateList());
	newNodes = Arrays.asList(robotData.getNodeList());
	//System.out.println(newPostions.get(0));
}
}


