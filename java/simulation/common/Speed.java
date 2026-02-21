package simulation.common;

public enum Speed {
	FAST(15), MEDIUM(10), SLOW(2), VERYSLOW(1), STOPPED(0);
	
	private final float speed;
	
	private Speed(int speed){
		this.speed = speed;
	}
	
	public float getSpeed(){
		return speed;
	}

}
