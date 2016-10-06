package agent.behaviors;

import agent.brainPackage.Behavior;
import simulation.common.Speed;

public class OnBoundaryBehavior extends Behavior {


public Speed generateSpeed() {
	return Speed.SLOW;
}

public int generateAngle(){
	return 21;
}
}
