package simulation.common;

public class State {

private int angle;
private Speed speed;

public State(){
}

public State(int angle, Speed speed){
	this.angle = angle;
	this.speed = speed;
}

public int getAngle() {
	return angle;
}

public void setAngle(int angle){
	this.angle = angle;
}

public float getSpeed() {
	return speed.getSpeed();
}

public String toString(){
	return "Angle : " + getAngle() +"\n" + "Speed : " + getSpeed();
}

public void setSpeed(Speed s){
		this.speed = s;
}

//where x,y is the robot and x2,y2 is the other robot
public static PolarCoordinate generateRelativePolarCoordinate(float x, float y, float x2, float y2, int currentAngle){
	float distance = 0;
	int angle = 0;
	
	float i;
	float j;
	
	i = x2 - x;
	j = y2 - y;
	
	distance = (float) (Math.sqrt(Math.pow((i), 2) + Math.pow((j), 2)));
	angle = (int)(Math.toDegrees(Math.acos(j/distance)));
	
	if(i < 0 && j == 0){
		angle = 360 - angle;
	}
	else if(i < 0 && j > 0){
		angle = 360 - angle;
	}
	else if(i < 0  && j < 0){
		angle = 360  - angle;
	}
	
	if(angle >= currentAngle){
		angle = angle - currentAngle;
	}
	
	else{
		angle = 360 - (currentAngle - angle);
	}
	
	return new PolarCoordinate(distance, angle);
}
}
