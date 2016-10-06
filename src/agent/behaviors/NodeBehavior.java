package agent.behaviors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import agent.brainPackage.Behavior;
import simulation.common.Node;
import simulation.common.Speed;

public class NodeBehavior extends Behavior{

public int generateAngle() {

	return pickBestNodeAngle();
}


public Speed generateSpeed() {
	
	return Speed.VERYSLOW;
}

public int pickBestNodeAngle(){
	List<Node> goodNodes = new ArrayList<Node>();
	for(Node n : newNodes){
		if(n.attribute != 0 && !n.boundary && !n.collision && n.object.equals("grass") && !(n.theta > 60 && n.theta < 300)){ //90 270
			goodNodes.add(n);
		}
	}
	
	//System.out.println("SORT");
	long seed = System.nanoTime();
	Collections.shuffle(goodNodes, new Random(seed));
	
	Comparator<Node> comp = (Node a, Node b) ->{
		return a.compareTo(b);
	};
	
	Collections.sort(goodNodes, comp);
/*
	for(Node n : goodNodes){
		System.out.println(n);
	}
	
	System.out.println();
*/
	
	if(goodNodes.isEmpty()){
		return 0;
	}
	else{
		int bestNodeAngle = 420;
		
		for(Node n : goodNodes){	
			if(n.theta == 0){
				bestNodeAngle = 0;
			}
			
			if(/*(*/n.theta < 10 /*|| n.theta > 290)*/ && bestNodeAngle != 0){
					bestNodeAngle = n.theta;
			}
		}
		if(bestNodeAngle == 420){
			return goodNodes.get(goodNodes.size()-1).theta;
		}
		else return bestNodeAngle;
	}
	
}

}
