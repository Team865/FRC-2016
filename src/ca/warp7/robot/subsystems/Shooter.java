package ca.warp7.robot.subsystems;

import ca.warp7.robot.vision.ShooterHelper;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {

	public static double currentRPM;
	public static int rotations;
	
	
	private static CANTalon flyWheel;
	private static Encoder encoder;
	
	/**
	 * @param motor controller Should be a TalonSRX
	 */
	public static void init(CANTalon motor, Encoder enc){
		flyWheel = motor;
		encoder = enc;
		rotations = 0;
		currentRPM = 0.0;
	}
	
	public static double getPosition(){
		return encoder.getRate();
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
