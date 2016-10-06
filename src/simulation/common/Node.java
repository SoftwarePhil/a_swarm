package simulation.common;

public class Node {

private static int nodeCounter = 0;
public int nodeId;
public float r;
public int theta;
public String object;
public int attribute;
public boolean collision = false;
public boolean boundary = false;

public transient int x;
public transient int y;

public Node(){
	nodeId = nodeCounter;
	nodeCounter++;
}

public void setBoundaryTrue(){
	boundary = true;
}

public void setCollisionTrue(){
	collision = true;
}

public void setRTheta(float r, int theta){
	this.r = r;
	this.theta = theta;
}

public void defaultNode(){
	r = 0;
	theta = 0;
	object = "defaultNode";
	attribute = 0;
}

public String toString(){
	return "Node ID : " + nodeId + " Object : " + object + " x : " + x + " y : " + y ; 
}

public int compareTo(Node b) {
	if(this.attribute < b.attribute){
		return -1;
	}
	return 1;
}

}
