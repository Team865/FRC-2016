package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class AdvancedModules extends ModuleBase{

	public static final int END_ANGLE_DOESNT_MATTER = 865;
	private static double zeroLeft = 0.0;
	private static double zeroRight = 0.0;
	private static double zeroAngle = 0.0;
	private static double currentX = 0.0;
	private static double currentY = 0.0;
	private static int step = 1;
	
	/**
	 * 0 is where you are so you need negative to curve left(looking from
	 * battery to intakes side)
	 * 
	 * @param x  Left/Right  positive is to the right
	 *            in centimeters 
	 * @param y	 Forward/backward  positive is forward(battery side)
	 *            in centimeters
	 * @param endAngle    Relative to where the curve was started
	 * 			  			t++++++++++++++++++++++++++++++++++++++++++he angle you end up in after you finish the curve
	 */
	public static boolean curve(double x, double y, int endAngle, Drive drive) {
		double driveSpeed = 0.3 * Math.signum(y);
		if(!moving){
			zeroLeft = drive.leftEncoder.getDistance();
			zeroRight = drive.rightEncoder.getDistance();
			zeroAngle = drive.gyro.getAngle();
			moving = true;
		}
		if(moving){
			switch(step){
			case 1:
				// initial curve
				
				return false;
			case 2:
				// move
				
				return false;
			case 3:
				// 2nd initial curve
				
				return false;
			case 4:
				// move
				
				return false;
			case 5:
				// last curve
				
				return false;
			case 6:
				//move
				
				if(Math.signum(x) == (Math.signum(endAngle)) * Math.signum(y)){
					
				}else{
					step++;
				}
				return false;
			case 7:
				// turn for correction
				
				return false;
			default:
				return false;
			}
		}
		return false;
		
		/*
		if(Math.abs(drive.leftEncoder.getDistance()-zero) >= Math.abs(distance/100) && moving){
			drive.stop();
			moving = false;
			zero = 0;
			return true;
		}
		*/
	}

	/**
	 * Direction is for which way to turn
	 * 
	 * @param direction
	 *            use constants for direction (Constants.LEFT, Constants.RIGHT)
	 */
	public static void shoot(int direction, Drive drive, Shooter shooter, Intake intake) {
		/*
		 * if(!targetFound){
		 * 
		 * }else{ if(!alignedTarget){
		 * 
		 * }else{ // do shooting things } }
		 */
	}

	/**
	 * Spins up the fly wheel and shoots for the hardstop shot
	 * 
	 */
	public static void hardstopShoot(Shooter shooter, Intake intake) {

	}

	/**
	 * Direction is for which way to turn
	 * 
	 * @param direction
	 *            use constants for direction (Constants.LEFT, Constants.RIGHT)
	 */
	public static void turnToGoal(int direction, Drive drive) {
		/*
		 * if(!targetFound){
		 * 
		 * }else{ if(!alignedTarget){
		 * 
		 * } }
		 */
	}
}