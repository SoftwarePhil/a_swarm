package simulation.field.grassField;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;

import simulation.agent.brainPackage.AgentBrain;
import simulation.agent.brainPackage.BehaviorManager;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
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
private int absoluteAngle = 0;
private int relativeAngle = 0;
float lastPostionX;
float lastPostionY;

private AgentBrain agentBrain;

public ActualRobot(BehaviorManager behaviorManager){
	robotID = "Swarm" + count;
	count++;
	
	color = Color.GREEN;
	for(int i = 0; i < count; i++){
		color = color.darker();
	}

	agentBrain = new AgentBrain(behaviorManager);
}

public int getRelativeRobotAngle(){
	return absoluteAngle;
}

public void updateState(){
	relativeAngle = state.getAngle();
	newDistance = state.getSpeed();
}

public void writeDataToRobot(PolarCoordinate[] p, Node[] n) throws IOException, InterruptedException{	
	agentBrain.writeDataToRobot(Arrays.asList(p), Arrays.asList(n));
}

public synchronized void move(){
	state = agentBrain.calcauteNextRobotState();
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
	agentBrain.hasCrashed();
	
}

public void notCrashed(){
	agentBrain.notCrashed();
}

public boolean getCrashed(){
	return agentBrain.getCrashed();
}

public State calcauteNextRobotState(){
	return agentBrain.calcauteNextRobotState();
}

public String getRobotName(){
	return robotID;
}
}
