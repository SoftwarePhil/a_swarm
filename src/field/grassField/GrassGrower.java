package field.grassField;
import java.util.ArrayList;
import java.util.List;
import simulation.common.AVector;
import simulation.common.Node;

public class GrassGrower implements Runnable {

private List<GrassNode> grass;
private int growTime;
private boolean grow;
private int xSize;
private int ySize;

public GrassGrower(List<GrassNode> grass, int growTime, boolean grow, int xSize, int ySize){
	this.grass = grass;
	this.growTime = growTime;
	this.grow = grow;
	this.xSize = xSize;
	this.ySize = ySize;
}

public float getPercentOfGrassCut(){
	int amountOfGrass = grass.size();
	int amountOfGrassCut = 0;
	
	for(GrassNode node : grass){
		if(node.getGrassHeight() == 0){
			amountOfGrass++;
		}
	}
	return 100*(amountOfGrassCut/(float)amountOfGrass);
}

@Override
public void run() {
	while(grow){
		try {
			Thread.sleep(growTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(GrassNode node : grass){
			node.growGrass();
		}
	}
}

public List<String> getCutPath(GrassNode start, GrassNode end){
	List<String> nodes = new ArrayList<String>(10);
	AVector vector = new AVector((end.x - start.x), (end.y - start.y));

	float dPlus = 1/vector.length();
	float pointX = start.x;
	float pointY = start.y;
	nodes.add(Math.round(pointX) + " " + Math.round(pointY));
	
	for(float j = 0; j < 1.0f; j = j + dPlus){
		
		pointX = pointX + (dPlus*vector.i);
		pointY = pointY + (dPlus*vector.j);
		
		nodes.add(Math.round(pointX) + " " + Math.round(pointY));
	}
	
	return nodes;
}

public List<Node> getNearestNodes(Node center){
List<Node> nodes = new ArrayList<Node>();

int x = center.x;
int y = center.y;

for(int i = -1; i < 2; i++){
	for(int j = -1; j < 2; j++){
		if(((x+j == x) && (y+i == y))){
			
		}
		else{
			Node n = new Node();
			n.x = x+j;
			n.y = y+i;
			if((n.x < 1) || (n.y < 1) || (n.x > xSize-1) || n.y > ySize-1){
				
			}
			else{
				nodes.add(n);
			}
		}
	}
}
	return nodes;
}

}
