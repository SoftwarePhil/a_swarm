package field.grassField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jfree.ui.RefineryUtilities;

public class FieldGUI {

private JFrame f;
private JPanel p;
private PointComponent lastComponent;
private float x;
private float y;
private List<GrassNode> grass;
private JLabel grassText;
private int stepCount = 0;
private int scaler;
private GrassCutOverTimeGraph chart; 
private long count = 0;
private List<PointOnChart> pointList = new ArrayList<PointOnChart>(20000);

public FieldGUI(float x, float y, List<GrassNode> grass, int scaler){
	this.x= x;
	this.y = y;
	this.grass = grass;
	this.scaler = scaler;
	f = new JFrame();
	
	p = new JPanel(new FlowLayout());
	grassText = new JLabel();
	
	f.setBackground(Color.BLUE);
	f.setSize(800, 800);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//making chart ..
	chart = new GrassCutOverTimeGraph(
		      "grass cut vs time" ,
		      "percent of grass cut vs number of simulation steps");

	chart.pack( );
	RefineryUtilities.centerFrameOnScreen(chart);
	chart.setVisible(true);
}

public void drawFrame(SimpleRobot[] r, ActualRobot[] a){
	/*
	if(lastComponent != null){
		f.remove(lastComponent);
	}
	 
	PointComponent p = new PointComponent(r, a, t, x, y);
	lastComponent = p;
	
	f.add(p);
	f.setVisible(true);
	*/
	
	if(lastComponent == null){
		int borderSize = 30;
		PointComponent pc = new PointComponent(r, a, x, y, grass, scaler);
		lastComponent = pc;
		lastComponent.setPreferredSize(new Dimension((int)x*scaler + borderSize*2, (int)y*scaler + borderSize*2));
		
		grassText.setText("Percent of grass cut : " + getPercentOfGrassCut());
		
		lastComponent.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
		p.add(lastComponent);
		p.add(grassText);
		f.add(p);
		
		p.setVisible(true);
		f.setVisible(true);
	}
	
	else{
		lastComponent.setValues(r, a, x, y, grass);
		grassText.setText("<html>Percent of Grass Cut : " + String.format("%.3f", getPercentOfGrassCut()) +"<br>" + "\nSimulation Steps : " + stepCount +"</html>");
		lastComponent.repaint();
	}
}

public float getPercentOfGrassCut(){
	float amountOfGrass = grass.size();
	int amountOfGrassCut = 0;
	stepCount++;

	for(GrassNode node : grass){
		if(node.getGrassHeight() == 0){
			amountOfGrassCut++;
		}
	}
	float grassCutPercent = amountOfGrassCut/amountOfGrass * 100;
	
	if(count % 5 == 0){
		pointList.add(new PointOnChart(stepCount, grassCutPercent));
		chart.setDataPointList(grassCutPercent, stepCount);
	}
	
	count++;
	
	if(grassCutPercent >= 99.99){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
		
		File f = new File("simulation/swarm_"+ dateFormat.format(date)+".csv");
		FileWriter w;
		try {
			w = new FileWriter(f);
		
		
		StringBuilder s = new StringBuilder();
		
		CSVPrinter p = new CSVPrinter(s, CSVFormat.EXCEL.withHeader("step", "percent cut"));
		
		for(PointOnChart point : pointList){
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
	
	return (grassCutPercent);
}
	
}
