package simulation.agent.behaviors;

import java.util.ArrayList;
import java.util.List;

import simulation.agent.brainPackage.Behavior;
import simulation.common.AVector;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;

public class SwarmBehavior extends Behavior{

boolean limit;	
	
public SwarmBehavior(boolean limit, float l) {
		this.limit = limit;
		SwarmBehavior.l = l;
		alpha = 1 - SwarmBehavior.l;
		setX();
	}

//the larger l, the greater the distance before attraction takes over
private static float l;
private static float alpha;
private float x;

public void setX(){
	x = (float) Math.sqrt(l/alpha);
}

public int generateAngle() {
	int angle = curveCalculation(shiftAngles(0));
	//System.out.println("  ANGLE " + angle);
	//limiting turns between 270 and 90
	if((angle < 270 && angle > 90 && limit)){
		if(angle > 180){
			angle = 270;
		}
		else{
			angle = 90;
		}
	}
	return angle;
}

 
public Speed generateSpeed() {
	return Speed.VERYSLOW;
}

//remove this method
public ArrayList<PolarCoordinate> shiftAngles(int shift){
	ArrayList<PolarCoordinate> p = new ArrayList<PolarCoordinate>();
	
	for(PolarCoordinate deltaPostion : newPostions){
		p.add(deltaPostion);
		int angle = p.get(p.size()-1).getTheta();
		int newAngle = angle - shift; 
		
		if(newAngle < 0){
			angle = 360 - Math.abs(newAngle);
		}
		p.get(p.size()-1).setTheta(angle);
	}
	
	return p;
}

//public for testing only
public int curveCalculation(List<PolarCoordinate> newPostions){
	ArrayList<AVector> vector = new ArrayList<AVector>();
	
	ArrayList<PolarCoordinate> listA = new ArrayList<PolarCoordinate>();
	ArrayList<PolarCoordinate> listR = new ArrayList<PolarCoordinate>();
	
	for(PolarCoordinate p : newPostions){
		if((p.getTheta() > 270 || p.getTheta() < 90 && limit) || !limit){
			if(p.getR() > x && p.getAttraction()){
				listA.add(p);
			}
			else if(p.getAttraction()){
				listR.add(p);
			}
		}
	}
	//System.out.println(listA.size() +  "  " + listR.size() + " total " + (listA.size() + listR.size()));
	
	for(PolarCoordinate p : listA){
		float temp = (float)(p.getR() - Math.sqrt(x));
		float a = (float)((alpha/l)*(Math.pow(temp,2)));
		
		//System.out.println("Curve output " + a);
		
		vector.add(new AVector((float)(a*(Math.sin(Math.toRadians(p.getTheta())))),(float)(a*Math.cos(Math.toRadians(p.getTheta())))));
	}
	
	for(PolarCoordinate p : listR){
		float temp = (float)(-l/(Math.pow(p.getR(), 2)));
		float r = temp + alpha;
		
		vector.add(new AVector((float)(r*(Math.sin(Math.toRadians(p.getTheta())))),(float)(r*Math.cos(Math.toRadians(p.getTheta())))));
	}
	
	return getVectorAngle(vector); 
}

private int getVectorAngle(ArrayList<AVector> vector){
	float iSum = 0;
	float jSum = 0;
	
	for(AVector v : vector){
		iSum = iSum + v.i;
		jSum = jSum + v.j;
	}
	
	//System.out.println(" i " + iSum + "   j " + jSum);
	
	return AVector.getVectorAngle(new AVector(iSum, jSum));
}
}
