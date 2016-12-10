package simulation.common;

public class PolarCoordinate {

private static int count = 0;
//@SuppressWarnings("unused")
private int id = 0;
//@SuppressWarnings("unused")
private String name = "";
private int theta;
private float r;
private boolean attraction = true; 


public PolarCoordinate(){}

public PolarCoordinate(float r, int theta){
	count++;
	id = count;
	this.r = r;
	this.theta = theta;
}

public float getR() {
	return r;
}

public int getTheta() {
	return theta;
}

public void setR(float r){
	this.r = r;
}

public void setTheta(int theta){
	this.theta = theta;
}

public void setName(String name){
	this.name = name;
}

public String getName(){
	return name;
}

public void setAttractionFalse(){
	attraction = false;
}

public boolean getAttraction(){
	return attraction;
}
public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

@Override
public String toString() {
  return "r : " + r + " " + "theta: " + theta;
}
}