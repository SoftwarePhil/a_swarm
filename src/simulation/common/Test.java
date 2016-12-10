package simulation.common;

import java.util.ArrayList;
import java.util.List;

import simulation.agent.behaviors.SwarmBehavior;

public class Test {
	public static void main(String[] args){
		
		List<PolarCoordinate> pc = new ArrayList<PolarCoordinate>();
		pc.add(new PolarCoordinate(50, 120));
		pc.add(new PolarCoordinate(3, 45));
		pc.add(new PolarCoordinate(4, 90));
		SwarmBehavior s = new SwarmBehavior(false, .999f);
		//System.out.println(s.curveCalculation(pc));
		
		PolarCoordinate r = State.generateRelativePolarCoordinate(14,21, 2,-7, 77);
		
		System.out.println(r);
	}	
}
