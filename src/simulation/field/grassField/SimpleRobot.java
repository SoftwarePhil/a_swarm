package simulation.field.grassField;

import java.awt.Color;

import simulation.common.State;
import simulation.common.Speed;

public class SimpleRobot extends SomeRobot implements Runnable {

RandomData jd = new RandomData();

public SimpleRobot(int sleepTime){
	super(sleepTime);
	robotID = "Robot" + count;
	count++;
	color = new Color((int)(Math.random() * 0x1000000));
}

public void changeState(){
	this.state = getNextState();
}

private State getNextState(){
	return new State(jd.generateAngle(), Speed.VERYSLOW);
}

public void updateXPos(){
	xPos = (float)(newDistance*(Math.cos(Math.toRadians(newAngle))));
	if(xPos < .025f && xPos > -.025f){xPos = 0;}
	absoluteXPos = absoluteXPos + xPos;
}

public void updateYPos(){
	yPos = (float)(newDistance*(Math.sin(Math.toRadians(newAngle))));
	if(yPos < .05f && yPos > -.05f){yPos = 0;}
	absoluteYPos = absoluteYPos + yPos;
}

}
