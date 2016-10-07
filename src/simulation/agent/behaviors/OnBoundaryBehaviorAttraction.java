package simulation.agent.behaviors;
import java.util.ArrayList;
import java.util.List;

import simulation.agent.brainPackage.Behavior;
import simulation.agent.brainPackage.DeltaPostion;
import simulation.common.AVector;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;

public class OnBoundaryBehaviorAttraction extends Behavior {
public List<PolarCoordinate> others;
private float l = .999f;
private float alpha = 1 - l;
private float x = (float)Math.sqrt(l/alpha);
	
public Speed generateSpeed() {
	return Speed.VERYSLOW;
}

public int generateAngle(){
	return getBestAngle();
	//return 21;
}

private int getAttractionAngle(){
	ArrayList<AVector> vector = new ArrayList<AVector>();
	List<PolarCoordinate> temp = new ArrayList<PolarCoordinate>();
	for(DeltaPostion d : newPostions){
		if(d.getCurrent().getAttraction()){
			//System.out.println(d.getAngle() + " -CRASHED ANGLES LIST");
			temp.add(d.getCurrent());
		}
	}
	others = temp;
	
	for(PolarCoordinate p : temp){
		float atemp = (float)(p.getR() - Math.sqrt(x));
		float a = (float)((alpha/l)*(Math.pow(atemp,2)));
		
		//System.out.println("Curve output " + a);
		
		vector.add(new AVector((float)(a*(Math.sin(Math.toRadians(p.getTheta())))),(float)(a*Math.cos(Math.toRadians(p.getTheta())))));
	}
	
	return getVectorAngle(vector);
}

private int getBestAngle(){
	return getAttractionAngle();
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
