package field.grassField;

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

@SuppressWarnings("serial")
public class PointComponent extends JComponent {

Point.Double[] p; 
SimpleRobot[] r;
ActualRobot[] a;

float x;
float y;
static int SCALER; //= 2; //default is 5
private static float ROBOTSIZE;// = 4*SCALER;
static float ANGLEINDICATORSIZE;// = SCALER;

//private float totalX;
//private float totalY;
//private int totalSize;
private ArrayList<GrassNode> grass; 

public PointComponent(SimpleRobot[] r, ActualRobot[] a,  float x, float y, List<GrassNode> grass, int scaler){
	super();
	SCALER = scaler;
	ROBOTSIZE = 4*SCALER;
	ANGLEINDICATORSIZE = SCALER;
	this.r = r;
	setPoints(r);
	this.a = a;
	this.x = x;
	this.y = y;
	this.grass = (ArrayList<GrassNode>) grass;
	
	//totalX = 0;
	//totalY = 0;
	
	//totalSize = a.length + r.length;
}

public void setValues(SimpleRobot[] r, ActualRobot[] a, float x, float y, List<GrassNode> grass){
	this.r = r;
	setPoints(r);
	this.a = a;
	this.x = x;
	this.y = y;
	this.grass = (ArrayList<GrassNode>) grass;
	
	//totalX = 0;
	//totalY = 0;
	
	//totalSize = a.length + r.length;
}

public void setPoints(SimpleRobot[] r){
	p = new Point.Double[r.length];
	int count = 0;
	
	
	for(SimpleRobot robot : r){
		p[count] = new Point.Double(robot.absoluteXPos * SCALER, robot.absoluteYPos * SCALER);
		count++;
	}
}

public void paintComponent(Graphics g){
	Graphics2D g2 = (Graphics2D) g;

//drawing grass
for(GrassNode gr : grass){
	Ellipse2D.Double point = new Ellipse2D.Double((gr.x * SCALER) - ROBOTSIZE/4, (gr.y * SCALER) - ROBOTSIZE/4, ROBOTSIZE/2, ROBOTSIZE/2);
	g2.setColor(gr.color);
	//g2.fill(point);
	g2.draw(point);
}	

//drawing simple robots
for(int i = 0; i < p.length; i++){
	Ellipse2D.Double point = new Ellipse2D.Double(p[i].x - ROBOTSIZE/2, p[i].y - ROBOTSIZE/2, ROBOTSIZE, ROBOTSIZE);
	//totalX += p[i].x + ROBOTSIZE/2;
	//totalY +=  p[i].y + ROBOTSIZE/2;
	g2.setColor(r[i].color);
	g2.draw(point);
}
	
//drawing agents
for(int i = 0; i < a.length; i++){
	//draw circle
	double x = (a[i].absoluteXPos * SCALER) - ROBOTSIZE/2;
	double y = (a[i].absoluteYPos * SCALER) - ROBOTSIZE/2;
	//totalX += (float) + x;
	//totalY += (float) + y;
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
	//xCenter + rcos(theta), yCenter + rsin(theta) 
	double xIndicator = ((a[i].absoluteXPos * SCALER) - ANGLEINDICATORSIZE/2) + (ROBOTSIZE/2 *Math.cos(Math.toRadians(a[i].newAngle)));
	double yIndicator = (a[i].absoluteYPos * SCALER) - ANGLEINDICATORSIZE/2 + (ROBOTSIZE/2 *Math.sin(Math.toRadians(a[i].newAngle)));
	
	Rectangle2D.Double angleIndicator = new Rectangle2D.Double(xIndicator, yIndicator, ANGLEINDICATORSIZE, ANGLEINDICATORSIZE);
	
	g2.setColor(Color.ORANGE);
	g2.fill(angleIndicator);
	g2.draw(angleIndicator);
}

/*
	//drawing swarm center
	Rectangle2D.Double center = new Rectangle2D.Double((totalX/totalSize) + 5, (totalY/totalSize) + 5, 10, 10);
	g2.setColor(Color.CYAN);
	g2.fill(center);
	g2.draw(center);
*/
	
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