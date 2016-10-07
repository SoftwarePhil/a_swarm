package simulation.agent.behaviors;

import simulation.agent.brainPackage.Behavior;
import simulation.agent.brainPackage.DeltaPostion;
import simulation.common.Speed;

public class AngleBehavior extends Behavior {

@Override
public int generateAngle() {
	
	return findAverageAngle();
}

@Override
public Speed generateSpeed() {
	return Speed.VERYSLOW;
}

public int findAverageAngle(){
	int avg = 0;
	for(DeltaPostion dp : newPostions){
		if(dp.getVector().getAttraction()){
			avg =+ dp.getVector().getTheta();
		}
	}
	
	avg = avg/newPostions.size();
	
	if(avg < 0){
		avg = 360 + avg;
	}
	
	return avg;
}

}
