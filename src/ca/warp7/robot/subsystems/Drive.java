package ca.warp7.robot.subsystems;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.hardware.GearBox;

public class Drive {

	//https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
	private static int direction;
	private static GearBox rightGearBox;
	private static GearBox leftGearBox;
	
	public static void init(GearBox right, GearBox left){
		rightGearBox = right;
		leftGearBox = left;
		direction = 1;
	}
	
	public static void changeDirection(){
		direction *= -1;
	}
	
	public static void tankDrive() {
		double left = Warp7Robot.driver.getLeftY();
		double right = Warp7Robot.driver.getRightY();
		
		left = createDeadband(left);
		right = createDeadband(right);
		
		move(left, right);
	}
	
	public static void cheesyDrive() {
        double throttle = Warp7Robot.driver.getLeftY();
        double wheel = Warp7Robot.driver.getRightX();
        
        throttle = createDeadband(throttle);
        wheel = createDeadband(wheel);
        
        boolean quickTurn = Warp7Robot.driver.getLeftBumperbutton();
        
        if(throttle < 0 && !quickTurn)wheel *= -1; // chandler's modification
		
		double angular_power = 0.0;
        double overPower = 0.0;
        double sensitivity = 1.5;
        double rPower = 0.0;
        double lPower = 0.0;
        if (quickTurn) {
            overPower = .25;
            sensitivity = .75;
            angular_power = wheel;
        } else {
            overPower = 0.0;
            angular_power = Math.abs(throttle) * wheel * sensitivity;
        }
        rPower = lPower = throttle;
        lPower += angular_power;
        rPower -= angular_power;
        if (lPower > 1.0) {
            rPower -= overPower * (lPower - 1.0);
            lPower = 1.0;
        } else if (rPower > 1.0) {
            lPower -= overPower * (rPower - 1.0);
            rPower = 1.0;
        } else if (lPower < -1.0) {
            rPower += overPower * (-1.0 - lPower);
            lPower = -1.0;
        } else if (rPower < -1.0) {
            lPower += overPower * (-1.0 - rPower);
            rPower = -1.0;
        }
        move(lPower, rPower);
    }

	private static void move(double left, double right) {
		right *= direction;
		left *= direction;
		
		rightGearBox.set(right*(-1));
		leftGearBox.set((left));
	}
	
	private static double createDeadband(double num){
		if(0.13 >= Math.abs(num)){
			num = 0;
		}
		
		num = Math.pow(num, 3);
		
		return num;
	}

	public static void stop() {
		rightGearBox.set(0);
		leftGearBox.set(0);
	}
}
