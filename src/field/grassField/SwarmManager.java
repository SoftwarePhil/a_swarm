package field.grassField;

import java.util.List;

public class SwarmManager implements Runnable {

private List<ActualRobot> agents;
private Field field;

public SwarmManager(List<ActualRobot> robots, Field field){
	this.agents = robots;
	this.field = field;
}

@Override
public void run() {
	while(true){
		if(isSwarmReady()){
			for(ActualRobot b : agents){
				synchronized (b){
				b.notify();
				}
			}
			
			synchronized(field){
				field.notify();
			}
			System.out.println("swarm ready !!");
		}
	}
}

public synchronized boolean isSwarmReady(){
	int count = 0;
	for(ActualRobot a : agents){
		if(a.getReady()){
			count++;
		}	
	}
	//System.out.println("count " + count + ":: size " + agents.size() );
	if(count == agents.size()){
		//System.out.println("count " + count + ":: size " + agents.size() );
		return true;
	}
	
	else return false;
}

}
