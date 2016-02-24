package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.Solenoid;

public class Intakes {
	private static GearBox box;
	private static final double SPEED = 1.0;
	public static Solenoid initialArm;
	public static Solenoid adjustingArm;
	
	public static void init(GearBox motor, Solenoid initialArm_, Solenoid adjustingArm_){
		box = motor;
		initialArm = initialArm_;
		adjustingArm = adjustingArm_;
		initialArm.set(false);
		adjustingArm.set(false);
	}
	
	public static void moveInitialArm(){
		initialArm.set(!initialArm.get());
	}
	
	public static void moveAdjustingArm(){
		adjustingArm.set(!adjustingArm.get());
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
			box.set(0);
		}
	}
	
	public static void outake(){
		box.set(SPEED);
	}
	
	public static void stopIntake(){
		box.set(0);
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
