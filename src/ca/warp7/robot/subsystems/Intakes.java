package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;

public class Intakes {
	private static GearBox box;
	private static final double SPEED = 1.0;
	
	public static void init(GearBox motor){
		box = motor;
	}
	
	/**
	 * 
	 * @param sensorReading This is the photo-sensor reading
	 * 						if it is false it will run
	 */
	public static void intake(boolean sensorReading){
		
		if(!sensorReading){
			box.set(-SPEED);
		}else{
			stop();
		}
	}
	
	public static void outake(){
		box.set(SPEED);
	}
	
	public static void stop(){
		box.set(0);
	}
	
	public static void set(double speed){
		box.set(speed);
	}
}
