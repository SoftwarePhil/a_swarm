package agent.behaviors;

import java.util.ArrayList;
import java.util.List;

import agent.brainPackage.Behavior;
import simulation.common.Speed;

public class MetaBehavior extends Behavior{

List<Behavior> behaviors = new ArrayList<Behavior>();

public MetaBehavior(List<Behavior> b){
	System.out.println(b.isEmpty());
	behaviors.addAll(b);
}

@Override
public int generateAngle() {
	int metaAngle = 0;
	
	for(Behavior b : behaviors){
		metaAngle = metaAngle +  b.getNextState(newPostions, newNodes).getAngle();
	}
	
	return metaAngle/behaviors.size();
}

@Override
public Speed generateSpeed() {
	// TODO Auto-generated method stub
	return Speed.VERYSLOW;
}



}
