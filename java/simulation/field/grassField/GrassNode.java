package simulation.field.grassField;

import java.awt.Color;

import simulation.common.Node;

public class GrassNode extends Node {

private static final int RED = 149;
private static final int GREEN = 250;
private static final int  BLUE = 235;

static final int maxGrassHeight = 10;
private static final int growthIntervalRed = RED/maxGrassHeight;
private static final int growthIntervalGreen = GREEN/maxGrassHeight;

//transient used to stop attribute from being serialized
public transient Color color;
private transient  Color cut;

public GrassNode(int x, int y, int grassHeight){
	object = "grass";
	//unCut = new Color(149, 0, 235);
	cut = new Color(0, GREEN, BLUE);
	this.x = x;
	this.y = y;
	
	this.attribute = grassHeight;
	color = new Color(grassHeight * growthIntervalRed, 255 - (grassHeight*growthIntervalGreen), BLUE);
}

public void growGrass(){
	if(attribute < maxGrassHeight){
		attribute++;
	
		int greenValue = color.getGreen() - growthIntervalGreen;
		int redValue = color.getRed() + growthIntervalRed;
		
		if(greenValue < 0){
			greenValue = 0;
		}
		
		if(redValue > RED){
			redValue = RED;
		}
		
		color = new Color(redValue, greenValue, color.getBlue());
		//System.out.println("grass has been grown " + toString());
	}
}

public void cutGrass(){
	attribute = 0;
	color = cut;
}

public int getGrassHeight(){
	return attribute;
}

public String toString(){
	return x + " " + y;
}

public void setBoundaryColor(){
	color = color.darker().darker().darker();
}
}
