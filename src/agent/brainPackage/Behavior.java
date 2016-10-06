package agent.brainPackage;

import java.util.List;

import simulation.common.Node;
import simulation.common.Speed;
import simulation.common.State;

public abstract class Behavior {

protected List<DeltaPostion> newPostions;
protected List<Node> newNodes;

public Behavior(){
}

public State getNextState(List<DeltaPostion> newPostions, List<Node> newNodes) {
	//System.out.println("get next state called!");
	this.newPostions = newPostions;
	this.newNodes = newNodes;
	int theta = generateAngle();
	Speed speed = generateSpeed();
	
	return new State(theta, speed);
	}

public abstract int generateAngle();

public abstract Speed generateSpeed();
}

