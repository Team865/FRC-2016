package ca.warp7.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {	
	
	private static CANTalon flyWheel;
	private static Encoder encoder;
	private static final double STARTING_SPEED = 0.7;
	private static int numberOfChanges;
	
	/**
	 * @param motor controller Should be a TalonSRX
	 */
	public static void init(CANTalon motor, Encoder enc){
		flyWheel = motor;
		encoder = enc;
		numberOfChanges = 0;
	}
	
	public static double getPosition(){
		return encoder.getRate();
	}
	
	public static void fire(){
		//TODO ARI  double wantedRPM = MATH;
		double wantedRPM = 5;
		prepareToFire(wantedRPM);
		if(readyToFire(wantedRPM)){
			Intakes.intake(false);
		}
	}
	
	private static boolean readyToFire(double wantedRPM) {
		//TODO TEST double currentRPM = encoder.getRate() * CONVERSION;
		//TODO TEST double error = ERROR;
		/*TODO
		if(currentRPM <= wantedRPM-error && currentRPM >= wantedRPM+error){
			return true;
		}else{
			return false;
		}
		*/
		return true;
	}

	private static void prepareToFire(double wantedRPM){
		/*TODO
		if(!(readyToFire(wantedRPM))){
			int multiplier = 1;
			double currentRPM = encoder.getRate() * CONVERSION;
			double RPM_Error = wantedRPM - currentRPM;
			double interval = 0.005 * RPM_Error/Math.abs(RPM_Error);
			flyWheel.set(flyWheel.get() + interval);
			
			if(flyWheel.get() == 0.0) flyWheel.set(0.7);
		}
		*/
	}
	
	public static void set(double speed){
		flyWheel.set(speed);
	}
	
	public static void stop(){
		flyWheel.set(0);
	}
}
