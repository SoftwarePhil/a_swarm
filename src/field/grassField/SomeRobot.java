package field.grassField;

import java.awt.Color;
import java.io.IOException;

import simulation.common.State;

public abstract class SomeRobot implements Runnable {

State state;

float xPos;
float yPos;
float absoluteXPos = 0;
float absoluteYPos = 0;
int newAngle;
float newDistance;
String robotID = "";
static int count;
Color color;
private static int SLEEP;

public SomeRobot(int sleepTime){
	robotID = "Ro" + count;
	count++;
	color = new Color((int)(Math.random() * 0x1000000));
	SLEEP = sleepTime;
}

public void setState(State state){
	this.state = state;
}


public synchronized void move() throws IOException{
	getValues();
	updateXPos();
	updateYPos();
}

public void run() {
	while(true){
		//System.out.println("ROBOT LOOP RUNNING");
		
		try {
			move();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	try {
		Thread.sleep(SLEEP);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	}
	
}


public abstract void updateXPos();
public abstract void updateYPos();


public void getValues(){
	int angle = state.getAngle();
	newDistance = state.getSpeed();
	newAngle = 0;
	
	if(angle <= 90){
		newAngle =  90 - angle;
	}
	else if(angle <= 180){
		newAngle = 360 - (angle - 90);
	}
	else if(angle <= 270){
		newAngle = 180 + (90 - (angle - 180)); 
	}
	
	else if (angle <= 360){
		newAngle = 90 + (90 - (angle - 270));
	}
	
}

public String toString(){
	return robotID + " " + "xPos " + absoluteXPos + " :: " + "yPos " + absoluteYPos;
}

public String getRobotName(){
	return robotID;
}
}
