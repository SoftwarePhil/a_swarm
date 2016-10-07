package simulation.field.grassField;

import simulation.common.PolarCoordinate;

public class RandomData {
	
	public float generateSpeed() {
		return (float) (Math.random()*3);	}

	public int generateAngle(){
		return (int) (Math.random()*(359));
		}
	
	public PolarCoordinate getRandomPolarCoordinate(){
		return new PolarCoordinate(generateSpeed(), generateAngle());
	}
	
	public PolarCoordinate[] getArray(){
		int size = (int)(Math.random() * 31);
		PolarCoordinate[] p = new PolarCoordinate[size];
		
		for(int i = 0; i < size; i++){
			p[i] = getRandomPolarCoordinate();
		}
		return p;
	}

}
