package field.getJsonData;

import agent.brainPackage.Agent;
import field.grassField.GrassNode;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.Speed;

public class RobotServer{
	
private RobotData robotData;
private Agent agent;

simulation.common.State state = new simulation.common.State(0, Speed.STOPPED);


public RobotServer(Agent agent){
	this.agent = agent;

	PolarCoordinate[] c = {new PolarCoordinate(0,0)};
	Node[] n = {new GrassNode(3,3,0)};
	n[0].setRTheta(0, 0);
	
	robotData = new RobotData(n, c);
	}

public void setPolarCoordinates(PolarCoordinate[] r, Node[] n) throws InterruptedException{
	robotData = new RobotData(n, r);	
	agent.updateRobotData(robotData);
	
	synchronized(agent){
		agent.notify();
	}
}
}



