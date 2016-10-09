package simulation.field.grassField;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simulation.agent.brainPackage.BehaviorManager;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;
import simulation.common.State;

public class ActualRobot {

State state;	

float xPos;
float yPos;
public float absoluteXPos = 0;
public float absoluteYPos = 0;
public int newAngle;
float newDistance;
String robotID = "";
static int count;
public Color color;

//old attributes of SomeRobot above
private int absoluteAngle = 0;
private int relativeAngle = 0;
private boolean isCrashed = false;
private boolean ready = false;
float lastPostionX;
float lastPostionY;


private boolean crashed;
private List<PolarCoordinate> newPostions = new ArrayList<PolarCoordinate>();
private List<Node> newNodes = new ArrayList<Node>();
private BehaviorManager behaviorManager;

//TODO: unlink the dependence on this class to SomeRobot, it should probably be indpendent 
public ActualRobot(BehaviorManager behaviorManager){
	robotID = "Swarm" + count;
	count++;
	
	color = Color.GREEN;
	for(int i = 0; i < count; i++){
		color = color.darker();
	}
	
	state = new State(0, Speed.VERYSLOW);
	crashed = false;
	this.behaviorManager = behaviorManager;
}

public int getRelativeRobotAngle(){
	return absoluteAngle;
}

public void updateState(){
	relativeAngle = state.getAngle();
	newDistance = state.getSpeed();
}

public void writeDataToRobot(PolarCoordinate[] p, Node[] n) throws IOException, InterruptedException{
	newPostions = Arrays.asList(p);
	newNodes = Arrays.asList(n);
}

public synchronized void move(){
	state = calcauteNextRobotState();
	updateState();
	getRelativeValues();
	updateXPos();
	updateYPos();
}

public void updateXPos(){
	lastPostionX = absoluteXPos;
	xPos = (float)(newDistance*(Math.cos(Math.toRadians(newAngle))));
	
	if(xPos < .025f && xPos > -.025f){xPos = 0;}
	
	absoluteXPos = absoluteXPos + xPos;
}

public void updateYPos(){
	lastPostionY = absoluteYPos;
	yPos = (float)(newDistance*(Math.sin(Math.toRadians(newAngle))));
	
	if(yPos < .05f && yPos > -.05f){yPos = 0;}
	
	absoluteYPos = absoluteYPos + yPos;
}

public void getRelativeValues(){
	absoluteAngle = relativeAngle + absoluteAngle;
	
	if(absoluteAngle > 360){
		absoluteAngle = absoluteAngle - 360;
	}
	
	newDistance = state.getSpeed();

	if(absoluteAngle < 90){
		newAngle =  90 - absoluteAngle;
	}
	else if(absoluteAngle < 180){
		newAngle = 360 - (absoluteAngle - 90);
	}
	else if(absoluteAngle < 270){
		newAngle = 180 + (90 - (absoluteAngle - 180)); 
	}
	else if (absoluteAngle <= 360){
		newAngle = 90 + (90 - (absoluteAngle - 270));
	}
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

public State calcauteNextRobotState(){
	try {
		behaviorManager.updateDeltaPostions(newPostions);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	behaviorManager.updateNodeList(newNodes);
	
	crashed = checkIfCrashed();
	
	if(crashed){
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

public String getRobotName(){
	return robotID;
}
}
