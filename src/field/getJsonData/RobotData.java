package field.getJsonData;

import simulation.common.Node;
import simulation.common.PolarCoordinate;

public class RobotData {
	
private Node[] nodeList;
private PolarCoordinate[] polarCoordinateList;

public RobotData(Node[] nodeList, PolarCoordinate[] polarCoordinateList){
	this.nodeList = nodeList;
	this.polarCoordinateList = polarCoordinateList;
}

public Node[] getNodeList() {
	return nodeList;
}

public PolarCoordinate[] getPolarCoordinateList() {
	return polarCoordinateList;
}
}
