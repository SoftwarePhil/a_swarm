package simulation.common;

public class AVector {

public float i;
public float j;

public AVector(float i, float j){
	this.i = i;
	this.j = j;
}

public float length(){
	return (float)Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
}

public static int getVectorAngle(AVector vector){
	float i = vector.i;
	float j = vector.j;
	
	float distance = 0;
	int angle = 0;
	
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
	
	if(angle == 360){
		angle = 0;
	}
	return angle;
}

public static AVector addVector(AVector a, AVector b){
	return new AVector(a.i + b.i, a.j + b.j);
}

}

