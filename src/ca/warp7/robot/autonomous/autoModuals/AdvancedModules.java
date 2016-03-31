package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class AdvancedModules extends ModuleBase{

	public static final int END_ANGLE_DOESNT_MATTER = 865;
	
	/**
	 * 0 is where you are so you need negative to curve left(looking from
	 * battery to intakes side)
	 * 
	 * @param x
	 *            in centimeters
	 * @param y
	 *            in centimeters
	 * @param endAngle
	 * 			  the angle you end up in after you finish the curve
	 */
	public static void curve(double x, double y, int endAngle, Drive drive) {
		
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