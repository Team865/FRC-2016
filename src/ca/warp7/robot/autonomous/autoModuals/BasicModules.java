package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;

public class BasicModules extends ModuleBase{

	private static double zero = 0;
	
	/**
	 * @param distance
	 *            in centimeters
	 */
	public static boolean move(double distance, Drive drive) {
		double driveSpeed = 0.4 * Math.signum(distance);
		if(!moving){
			zero = drive.leftEncoder.getDistance();
			drive.autoMove(driveSpeed, driveSpeed);
			moving = true;
		}
		if(Math.abs(drive.leftEncoder.getDistance()-zero) >= Math.abs(distance/100) && moving){
			drive.stop();
			moving = false;
			zero = 0;
			return true;
		}
		return false;
	}

	/**
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	public static boolean relativeTurn(double degrees, Drive drive) {
		double turnSpeed = 0.3 * Math.signum(degrees);
		double allowableError = 5;
		if(!moving){
			zero = drive.gyro.getAngle();
			drive.autoMove(turnSpeed, -turnSpeed);
			moving = true;
		}
		if(((drive.gyro.getAngle()-zero) >= (degrees-allowableError) && (drive.gyro.getAngle()-zero) <= (degrees+allowableError)) && moving){
			drive.stop();
			moving = false;
			zero = 0;
			return true;
		}
		return false;
	}

	/**
	 * @param degrees
	 *            absolute (0 is set at the start when the robot is booted up)
	 * @param gyroOffset 
	 */
	public static boolean absoluteTurn(double degrees, Drive drive, double gyroOffset) {
		double currentAngle = drive.gyro.getAngle()-gyroOffset;
		while((currentAngle<360 && currentAngle>-360) == false)
			if(currentAngle>=360)
				currentAngle-=360;
			else if(currentAngle <= -360)
				currentAngle+=360;
		
		double direction = 0;
		if(degrees < currentAngle)
			direction = -1;
		else if(degrees >= currentAngle)
			direction = 1;
		
		double turnSpeed = 0.5;
		double turnSetting = Math.abs(turnSpeed) * direction;
		double allowableError = 2;
		
		if(currentAngle >= (degrees-allowableError) && currentAngle <= (degrees+allowableError)){
			drive.autoMove(0.0, 0.0);
			return true;
		}
		
		if(Math.signum(turnSetting) == 1)
			drive.autoMove(-turnSetting, turnSetting);
		else if(Math.signum(turnSetting) == -1)
			drive.autoMove(turnSetting, -turnSetting);
		
		return false;
	}
}
