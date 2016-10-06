package field.grassField;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class GrassCutOverTimeGraph extends ApplicationFrame{
private static List<PointOnChart> pointList;
static ChartPanel chartPanel;
	
public GrassCutOverTimeGraph(String applicationTitle , String chartTitle){
	super(applicationTitle);
	pointList = new ArrayList<PointOnChart>();
    JFreeChart lineChart = ChartFactory.createXYLineChart(
         chartTitle,
         "percent cut","steps",
         createDataset(),
         PlotOrientation.VERTICAL,
         true,true,false);
    
    chartPanel = new ChartPanel( lineChart );
    chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
    setContentPane( chartPanel );
   }

   private XYDataset createDataset()
   {
      XYSeriesCollection dataset = new XYSeriesCollection();
      XYSeries series = new XYSeries("frist");
      
      for(PointOnChart  p : pointList){
	      if(pointList.size() > 1){
		      series.add(p.step, p.grassCut);
		      //System.out.println(pointList.get(pointList.size() - 1).step);
	      }
      }
      
      dataset.addSeries(series);
     
      return dataset;
   }
   
   public void setDataPointList(float grassCut, int step){
	   pointList.add(new PointOnChart(step, grassCut));
	   refreshChart(chartPanel.getChart().getXYPlot().getDataset());
	
   }
   
   public JFreeChart createChart(){
	   return  ChartFactory.createXYLineChart(
		         "Swarm",
		         "steps","percent cut",
		         createDataset(),
		         PlotOrientation.VERTICAL,
		         true,true,false);
   }
   
   private void refreshChart(XYDataset d) {
	   
	   //chartPanel.getChart().getXYPlot().setDataset(d);
	  // chartPanel.getChart().fireChartChanged();
	   //chartPanel.repaint();
	   
	   	chartPanel.removeAll();
	   	chartPanel.revalidate(); // This removes the old chart
	   	chartPanel = null;
	   	
	   	JFreeChart lineChart = createChart(); 
	   	lineChart.removeLegend(); 
	    chartPanel = new ChartPanel(lineChart);
	    setContentPane( chartPanel );
	    chartPanel.repaint(); // This method makes the new chart appear
	    
	}

}
