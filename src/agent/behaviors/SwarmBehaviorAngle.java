package agent.behaviors;

import java.util.ArrayList;
import java.util.List;

import agent.brainPackage.Behavior;
import agent.brainPackage.DeltaPostion;
import simulation.common.AVector;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;

public class SwarmBehaviorAngle extends Behavior {

NodeBehavior nodeBehavior = new NodeBehavior();
//when limit is false the robots vision and movement is not restricted 
private boolean limit;

public SwarmBehaviorAngle(boolean limit) {
		setX();
		this.limit =limit;
	}

//the larger l, the greater the distance before attraction takes over
private static final float l = .99f;
private static final float alpha = 1 - l;
private static final float SCALAR = 1f; //SCALER .5, l = .09, random node between 270 and 90, only agents between 270 and 90 || SCALER .75
private float x;


public void setX(){
	x = (float) Math.sqrt(l/alpha);
}

public int generateAngle() {
	//nodeBehavior needs to get the updated date from swarmBehavior
	int swarmAngle = curveCalculation(shiftAngles(0));
	nodeBehavior.getNextState(newPostions, newNodes);
	int nodeAngle = nodeBehavior.generateAngle();
	
	if(swarmAngle == 0){
		return nodeAngle;
	}
	
	AVector nodeVector =  new AVector((float)(SCALAR*(Math.sin(Math.toRadians(nodeAngle)))),(float)(SCALAR*Math.cos(Math.toRadians(nodeAngle))));
	AVector swarmVector =  new AVector((float)((Math.sin(Math.toRadians(swarmAngle)))),(float)(Math.cos(Math.toRadians(swarmAngle))));
	AVector vectorSum = AVector.addVector(nodeVector, swarmVector);
	
	int angle = AVector.getVectorAngle(vectorSum);
	
	//System.out.println("  ANGLE " + angle + " OLD ANGLE " + swarmAngle); 
	if((angle < 270 && angle > 90 && limit)){ //if angle is not between 270 and 90, give it the value closest to 270 or 90
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
	return Speed.SLOW;
}

public ArrayList<PolarCoordinate> shiftAngles(int shift){
	ArrayList<PolarCoordinate> p = new ArrayList<PolarCoordinate>();
	
	for(DeltaPostion deltaPostion : newPostions){
		p.add(deltaPostion.getCurrent());
		int angle = p.get(p.size()-1).getTheta();
		int newAngle = angle - shift; 
		
		if(newAngle < 0){
			angle = 360 - Math.abs(newAngle);
		}
		p.get(p.size()-1).setTheta(angle);
	}
	
	return p;
}

private int curveCalculation(List<PolarCoordinate> newPostions){
	ArrayList<AVector> vector = new ArrayList<AVector>();
	
	ArrayList<PolarCoordinate> listA = new ArrayList<PolarCoordinate>();
	ArrayList<PolarCoordinate> listR = new ArrayList<PolarCoordinate>();
	
	for(PolarCoordinate p : newPostions){
		if((((p.getTheta() > 270 || p.getTheta() < 90) && (p.getR() < x*2.5)) && limit) || !limit){ //limits effective vision and angle it can see other robots at
			if(p.getR() > x && p.getAttraction()){
				listA.add(p);
			}
			else if(p.getAttraction()){
				listR.add(p);
			}
		}
	}
	System.out.println(listA.size() +  "  " + listR.size() + " total " + (listA.size() + listR.size()));
	
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
