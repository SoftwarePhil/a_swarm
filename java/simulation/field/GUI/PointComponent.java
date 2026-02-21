package simulation.field.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import simulation.field.grassField.ActualRobot;
import simulation.field.grassField.GrassNode;

@SuppressWarnings("serial")
public class PointComponent extends JComponent {

Point.Double[] p; 
ActualRobot[] a;

float x;
float y;
static int SCALER; //= 2; //default is 5
private static float ROBOTSIZE;// = 4*SCALER;
static float ANGLEINDICATORSIZE;// = SCALER;
private ArrayList<GrassNode> grass; 

public PointComponent(ActualRobot[] a,  float x, float y, List<GrassNode> grass, int scaler){
	super();
	SCALER = scaler;
	ROBOTSIZE = 4*SCALER;
	ANGLEINDICATORSIZE = SCALER;
	this.a = a;
	this.x = x;
	this.y = y;
	this.grass = (ArrayList<GrassNode>) grass;
}

public void setValues(ActualRobot[] a, float x, float y, List<GrassNode> grass){
	this.a = a;
	this.x = x;
	this.y = y;
	this.grass = (ArrayList<GrassNode>) grass;
}

public void paintComponent(Graphics g){
	Graphics2D g2 = (Graphics2D) g;

//drawing grass
for(GrassNode gr : grass){
	Ellipse2D.Double point = new Ellipse2D.Double((gr.x * SCALER) - ROBOTSIZE/4, (gr.y * SCALER) - ROBOTSIZE/4, ROBOTSIZE/2, ROBOTSIZE/2);
	g2.setColor(gr.color);
	g2.draw(point);
}	
	
//drawing agents
for(int i = 0; i < a.length; i++){
	//draw circle
	double x = (a[i].absoluteXPos * SCALER) - ROBOTSIZE/2;
	double y = (a[i].absoluteYPos * SCALER) - ROBOTSIZE/2;
	
	Ellipse2D.Double point = new Ellipse2D.Double(x, y, ROBOTSIZE, ROBOTSIZE);
	if(a[i].getCrashed()){
		g2.setColor(Color.RED);
	}
	else{
		g2.setColor(a[i].color);
	}
	
	g2.fill(point);
	g2.draw(point);
	
	//draw angle indicator
	double xIndicator = ((a[i].absoluteXPos * SCALER) - ANGLEINDICATORSIZE/2) + (ROBOTSIZE/2 *Math.cos(Math.toRadians(a[i].newAngle)));
	double yIndicator = (a[i].absoluteYPos * SCALER) - ANGLEINDICATORSIZE/2 + (ROBOTSIZE/2 *Math.sin(Math.toRadians(a[i].newAngle)));
	
	Rectangle2D.Double angleIndicator = new Rectangle2D.Double(xIndicator, yIndicator, ANGLEINDICATORSIZE, ANGLEINDICATORSIZE);
	
	g2.setColor(Color.ORANGE);
	g2.fill(angleIndicator);
	g2.draw(angleIndicator);
}

	//boundary, fix this at some point
	g2.setColor(Color.BLACK);
	Line2D.Double line1 = new Line2D.Double(0,y*SCALER  + 20, x*SCALER + 20, y*SCALER + 20);
	g2.fill(line1);
	g2.draw(line1);
	Line2D.Double line2 = new Line2D.Double(x*SCALER + 20, 0, x*SCALER + 20, y*SCALER + 20);
	g2.fill(line2);
	g2.draw(line2);
}
}
