package simulation.agent.brainPackage;

import simulation.common.PolarCoordinate;

public class DeltaPostion {

private PolarCoordinate last;
private PolarCoordinate current;
private PolarCoordinate vector;

public DeltaPostion(PolarCoordinate current, PolarCoordinate last){
	this.current = current;
	this.last = last;
	vector = calculateVector(current, last);
}

public void newPostion(PolarCoordinate polarCoordinate){
	last = current;
	current = polarCoordinate;
	
	vector = calculateVector(current, last);
}

public static PolarCoordinate calculateVector(PolarCoordinate posFinal, PolarCoordinate posInital){
	float r = posFinal.getR() - posInital.getR();
	int theta = posFinal.getTheta() - posInital.getTheta();
	
	return new PolarCoordinate(r, theta);
}

public float getDistance(){
	return current.getR();
}

public double getAngle(){
	return current.getTheta();
}

public PolarCoordinate getCurrent() {
	return current;
}

public PolarCoordinate getVector(){
	return vector;
}

public String toString(){
	return String.format("distance :  %f  Angle :  %d \nlast: %s ,, current: %s", vector.getR(), vector.getTheta(), last.getName(), current.getName());
}
}
