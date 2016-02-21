package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.Solenoid;

public class Intakes {
	private static GearBox box;
	private static final double SPEED = 1.0;
	private static Solenoid initialArm;
	private static Solenoid adjustingArm;
	
	public static void init(GearBox motor, Solenoid initialArm_, Solenoid adjustingArm_){
		box = motor;
		initialArm = initialArm_;
		adjustingArm = adjustingArm_;
	}
	
	public static void moveInitialArm(boolean bool){
		initialArm.set(bool);
	}
	
	public static void moveAdjustingArm(boolean bool){
		adjustingArm.set(bool);
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
		initialArm.set(false);
		adjustingArm.set(false);
	}
	
	public static void set(double speed){
		box.set(speed);
	}

	public static boolean adjustedArmRetracted() {
		if(adjustingArm.get() == false){
			return true;
		}else{
			return false;
		}
	}
}
