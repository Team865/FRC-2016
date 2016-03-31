package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;

public class BasicModules extends ModuleBase{

	private static int zero = 0;
	
	/**
	 * @param distance
	 *            in centimeters
	 */
	public static boolean move(double distance, Drive drive) {
		double driveSpeed = 0.3 * Math.signum(distance);
		if(!moving){
			zero = drive.leftEncoder.get();
			drive.autoMove(driveSpeed, driveSpeed);
			moving = true;
		}
		if(Math.abs(drive.leftEncoder.get()-zero) >= Math.abs(distance/100) && moving){
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
			zero = (int) drive.gyro.getAngle();
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
	 */
	public static boolean absoluteTurn(double degrees, Drive drive) {
		double direction = degrees-drive.gyro.getAngle();
		double turnSpeed = 0.3 * Math.signum(direction);
		double allowableError = 5;
		if(!moving){
			drive.autoMove(turnSpeed, -turnSpeed);
			moving = true;
		}
		if(drive.gyro.getAngle() >= (degrees-allowableError) && drive.gyro.getAngle() <= (degrees+allowableError) && moving){
			drive.stop();
			moving = false;
			return true;
		}
		return false;
	}
}
