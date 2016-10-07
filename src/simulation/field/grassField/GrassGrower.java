package simulation.field.grassField;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import simulation.common.AVector;
import simulation.common.Node;
import simulation.common.HistoryNode;

public class GrassGrower implements Runnable {

private List<GrassNode> grass;
private int growTime;
private boolean grow;
private int xSize;
private int ySize;
private List<HistoryNode> history;

public GrassGrower(List<GrassNode> grass, int growTime, boolean grow, int xSize, int ySize){
	this.grass = grass;
	this.growTime = growTime;
	this.grow = grow;
	this.xSize = xSize;
	this.ySize = ySize;
	history = new ArrayList<HistoryNode>(5000);
}

public float getPercentOfGrassCut(){
	int amountOfGrass = grass.size();
	int amountOfGrassCut = 0;
	
	for(GrassNode node : grass){
		if(node.getGrassHeight() == 0){
			amountOfGrassCut++;
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

public void recordHistory(long step){
	float percentCut = getPercentOfGrassCut();
	history.add(new HistoryNode(step, percentCut));
	System.out.println(percentCut);
	if(percentCut >= 99.99){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		Date date = new Date();
		
		File f = new File("simulation/swarm_"+ dateFormat.format(date)+".csv");
		FileWriter w;
		try {
			w = new FileWriter(f);
		
		
		StringBuilder s = new StringBuilder();
		
		CSVPrinter p = new CSVPrinter(s, CSVFormat.EXCEL.withHeader("step", "percent cut"));
		
		for(HistoryNode point : history){
			p.printRecord(point.step, point.grassCut);
		}
		w.append(s);
		w.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("over --- shutting down");
		System.exit(0);
	}
}

}
