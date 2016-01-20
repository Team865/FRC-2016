package ca.warp7.robot.subsystems;

import ca.warp7.robot.vision.ShooterHelper;
import edu.wpi.first.wpilibj.CANTalon;

public class Shooter {

	private static CANTalon flyWheel;
	
	/**
	 * @param motor Should be a TalonSRX
	 */
	public static void init(CANTalon motor){
		flyWheel = motor;
	}
	
	public static void fire(){
		prepareToFire(5);
		if(readyToFire()){
			Intakes.intake(false);
		}
	}
	
	private static boolean readyToFire() {
		// TODO Do something with an encoder
		return true;
	}

	public static void prepareToFire(double distance){
		flyWheel.set(ShooterHelper.getDesiredRPM(distance));
	}
	
	public static void set(double speed){
		flyWheel.set(speed);
	}
	
	public static void stop(){
		flyWheel.set(0);
	}
}
