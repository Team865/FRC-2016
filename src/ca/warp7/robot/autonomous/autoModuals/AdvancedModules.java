package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class AdvancedModules {
	
	/**
	 * 0 is where you are so you need negative to
	 * curve left(looking from battery to intakes side)
	 * 
	 * @param x in millimeters
	 * @param y in millimeters
	 */
	public static void curve(double x, double y){
		
	}
	
	/**
	 * Direction is for which way to turn
	 * 
	 * @param direction use constants for direction
	 *        (Constants.LEFT, Constants.RIGHT)
	 */
	public static void shoot(int direction, Drive drive, Shooter shooter, Intake intake){
		/*
		if(!targetFound){
			
		}else{
			if(!alignedTarget){
				
			}else{
				// do shooting things
			}
		}
		*/
	}

	public static void hardstopShoot(Shooter shooter, Intake intake){
		
	}
	
	/**
	 * Direction is for which way to turn
	 * 
	 * @param direction use constants for direction
	 *        (Constants.LEFT, Constants.RIGHT)
	 */
	public static void turnToGoal(int direction, Drive drive){
		/*
		if(!targetFound){
			
		}else{
			if(!alignedTarget){
				
			}
		}
		*/
	}
}
