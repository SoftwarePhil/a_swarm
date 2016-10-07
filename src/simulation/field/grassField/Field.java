package simulation.field.grassField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulation.agent.brainPackage.Behavior;
import simulation.agent.brainPackage.BehaviorManager;
import simulation.common.Node;
import simulation.common.PolarCoordinate;
import simulation.common.State;
import simulation.field.GUI.FieldGUI;

public class Field implements Runnable {

float xSize;
float ySize;
private SimpleRobot[] simpleRobots;

private Map<String, GrassNode> grass = new HashMap<>();
private List<ActualRobot> agents = new ArrayList<ActualRobot>();

private GrassGrower grassGrower;
int grassHeight = 10;
private static final int GROWTIME = 10000;
private static final boolean GROW = false;
private static final int UPDATETIME = 10;
private FieldGUI fieldGUI;
private boolean GUI;

private int count = 1; //used to place robots in field
private long simulationSteps = 0; 

public Field(int amount, int numOfAgents, float xSize, float ySize, int scaler, Behavior b1, Behavior b2, boolean GUI) throws IOException{
	this.xSize = xSize;
	this.ySize = ySize;
	this.GUI = GUI;
	
	for(int i = 1; i < xSize; i++){
		for(int j = 1; j < ySize; j++){
			//make grass random height
			//int grassHeight = (int)(Math.random() * 3) + 7;
			grass.put(i + " " + j, new GrassNode(i,j, grassHeight));
		}
	}
	
	//sets top row as boundary
	for(int i = 1; i < xSize; i++){
		GrassNode n = grass.get(i +" " + 1);
		n.setBoundaryTrue();
		n.setBoundaryColor();
		
		GrassNode n2 = grass.get(i +" " + 2);
		n2.setBoundaryTrue();
		n2.setBoundaryColor();
	}
	
	//sets top bottom as boundary
	for(int i = 1; i < xSize; i++){
		GrassNode n = grass.get(i +" " + (int)(ySize - 1));
		n.setBoundaryTrue();
		n.setBoundaryColor();
		
		GrassNode n2 = grass.get(i +" " + (int)(ySize - 2));
		n2.setBoundaryTrue();
		n2.setBoundaryColor();
	}
	
	//left column row as boundary
	for(int i = 1; i < ySize; i++){
		GrassNode n = grass.get(1 +" " + i);
		n.setBoundaryTrue();
		n.setBoundaryColor();
		
		GrassNode n2 = grass.get(2 +" " + i);
		n2.setBoundaryTrue();
		n2.setBoundaryColor();
	}
	
	//right column row as boundary
	for(int i = 1; i < ySize; i++){
		GrassNode n = grass.get((int)(xSize - 1) + " " + i);
		n.setBoundaryTrue();
		n.setBoundaryColor();
		
		GrassNode n2 = grass.get((int)(xSize - 2) + " " + i);
		n2.setBoundaryTrue();
		n2.setBoundaryColor();
	}
	
	
	List<GrassNode> grassList = new ArrayList<GrassNode>();
	for(Map.Entry<String, GrassNode> grass : grass.entrySet()){
		grassList.add(grass.getValue());
	}
	
	if(GUI){
		fieldGUI = new FieldGUI(xSize, ySize, grassList, scaler);
	}
	
	grassGrower = new GrassGrower(grassList, GROWTIME, GROW, (int)xSize, (int)ySize);
	new Thread(grassGrower).start();
	
	simpleRobots = new SimpleRobot[amount];
	
	for(int i = 0; i < simpleRobots.length; i++){
		simpleRobots[i] = new SimpleRobot(UPDATETIME);
		simpleRobots[i].changeState();
		new Thread(simpleRobots[i]).start();
	}

	for(int i = 0; i <numOfAgents; i++){
		addRobot(new ActualRobot(UPDATETIME, new BehaviorManager(b1, b2)));
	}
}

public void addRobot(ActualRobot r){
	agents.add(r);
	//determines the positions of the robots in the field
	r.absoluteXPos = xSize - (count*5);
	r.absoluteYPos = (ySize - ySize/7) - (count*5);

	r.lastPostionX = r.absoluteXPos;
	r.lastPostionY = r.absoluteYPos;
	count++;
}

public void run() {
	//TODO:look how simple robots move .. copy && get rid of swarm manager ..
	while(true){
		synchronized(this){
		grassGrower.recordHistory(simulationSteps);
		
		try {
			Thread.sleep(UPDATETIME);
			//System.out.println("field has worken up");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for(ActualRobot a : agents){
			a.move();
		}
		
		//checks if in bounds, have to find a way to tell when robots hit into each other and when a real robot goes out of bounds
		for(SimpleRobot r : simpleRobots){
			if(r.absoluteYPos > ySize){
				r.changeState();
				r.absoluteYPos = ySize;
			}
			if(r.absoluteYPos < 0){
				r.changeState();
				r.absoluteYPos = 0;
			}
			if(r.absoluteXPos > xSize){
				r.changeState();
				r.absoluteXPos = xSize;
			}
			if(r.absoluteXPos < 0){
				r.changeState();
				r.absoluteXPos = 0;
			}
		}
		
		//this will check if agents go out of bounds and will send that a crash has occurred back to agent
			for(ActualRobot a : agents){
				a.notCrashed();
				if(a.absoluteYPos  > ySize){
					a.hasCrashed();
				}
				if(a.absoluteYPos < 0){
					a.hasCrashed();
				}
				if(a.absoluteXPos > xSize){
					a.hasCrashed();
				}
				if(a.absoluteXPos < 0){
					a.hasCrashed();
				}
				
			List<String> keys = new ArrayList<String>();
			
			String pos = Math.round(a.absoluteXPos) + " " + Math.round(a.absoluteYPos);
			String pos2 = Math.round(a.lastPostionX) + " " + Math.round(a.lastPostionY);
			
			boolean temp = grass.containsKey(pos);
			boolean temp2 = grass.containsKey(pos2);
			
			//might fix edge issue, where if one goes out of bounds grass is not cut from start point to out of bounds point
			if(temp && temp2){
					GrassNode grassNode1 = grass.get(pos);
					GrassNode grassNode2 = grass.get(pos2);
					keys = grassGrower.getCutPath(grassNode2, grassNode1);
			}
			
			for(String str : keys){
				if(grass.containsKey(str))
					{
					grass.get(str).cutGrass();
					}
				}
			}
		
		try {
			if(agents.size() > 0){
				for(ActualRobot a: agents){
						//System.out.println("data being written to robot.. " + a.getRobotName());
						a.writeDataToRobot(getListOfRelativePolarCoordinates(a), getListOfRelativeNodes(a));
				}
			} 
		}catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if(GUI){
			fieldGUI.drawFrame(simpleRobots, ((List<ActualRobot>)agents).toArray(new ActualRobot[agents.size()]));
		}
		simulationSteps++;
	}
		
	}
}

public PolarCoordinate[] getListOfRelativePolarCoordinates(ActualRobot robot) throws IOException{
	List<PolarCoordinate> pL = new ArrayList<PolarCoordinate>();
	
	if(robot.getCrashed()){
		pL.add(new PolarCoordinate(0,0));
		pL.get(0).setName("CRASH");
	}
		for(int i = 0; i < simpleRobots.length; i++){
			pL.add(State.generateRelativePolarCoordinate(robot.absoluteXPos, robot.absoluteYPos, simpleRobots[i].absoluteXPos, simpleRobots[i].absoluteYPos, robot.getRelativeRobotAngle()));
			pL.get(pL.size() - 1).setName("AGENT " + simpleRobots[i].getRobotName());
		}
	
		//this has to get the position of the other actual robots around and create the PolarCoordinates array
		for(ActualRobot a : agents){
			if(a.getRobotName().equals(robot.getRobotName())){
				
			}
			else{
				pL.add(State.generateRelativePolarCoordinate(robot.absoluteXPos, robot.absoluteYPos, a.absoluteXPos, a.absoluteYPos, robot.getRelativeRobotAngle()));;
				int last = pL.size() - 1;
				pL.get(last).setName(a.getRobotName());
				if(a.getCrashed()){
					pL.get(last).setAttractionFalse();
					pL.get(last).setName("C" + a.getRobotName());
				}
				//System.out.println(pL.get(last) + "  ::" + robot);
		}
	}
	return pL.toArray(new PolarCoordinate[pL.size()]);

}

public Node[] getListOfRelativeNodes(ActualRobot robot){
	List<Node> nodes = new ArrayList<Node>();
	int x = Math.round(robot.absoluteXPos);
	int y = Math.round(robot.absoluteYPos);
	Node closestNode;
	String key = Math.round(x) + " " + Math.round(y);
	
	//if the robots is near a node in the map get the relative angle and distance from that node
	if(grass.containsKey(key)){
		
		closestNode = grass.get(key);
		List<Node> tempNodes = grassGrower.getNearestNodes(closestNode);
		
		for(Node g : tempNodes){
			Node tempNode = grass.get(g.x + " " + g.y);
			//System.out.println(tempNode);
			PolarCoordinate tempPC = State.generateRelativePolarCoordinate(robot.absoluteXPos, robot.absoluteYPos, g.x, g.y, robot.getRelativeRobotAngle());
			//System.out.println(tempPC);
			tempNode.setRTheta(tempPC.getR(), tempPC.getTheta());
			nodes.add(tempNode);
		}
	}
	return nodes.toArray(new Node[nodes.size()]);
}

public List<ActualRobot> getAgents(){
	return agents;
}

public long getNumberOSteps(){
	return simulationSteps;
}

}
