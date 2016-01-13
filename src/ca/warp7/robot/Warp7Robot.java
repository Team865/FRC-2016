package ca.warp7.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Warp7Robot extends SampleRobot {
	Talon leftSide;
	Talon rightSide;
	
	Talon shooter;
	
	Joystick xboxCtl;
	
	double shooterCounter;
	
	boolean dPadHeld;
	
	boolean aBtnHeld;
	
	
	
	public Warp7Robot() {
		leftSide = new Talon(0);
		rightSide = new Talon(1);
		shooter = new Talon(2);
		xboxCtl = new Joystick(0);
		shooterCounter = 0;
		dPadHeld = false;
		aBtnHeld = false;
	}
	
	public void robotInit() {
		
	}
	
	public void disablesd() {
		
		while(isDisabled()) {
			leftSide.set(0);
			rightSide.set(0);
			shooter.set(0);
			dPadHeld = false;
			aBtnHeld = false;
		}
	}
	
	public void autonomous() {
		
		while(isAutonomous()) {
			
		}
	}
	
	public void operatorControl() {
		
		while(isOperatorControl()) {
			Timer.delay(0.05);
			leftSide.set(xboxCtl.getRawAxis(1));
			rightSide.set(-xboxCtl.getRawAxis(5));
			if(xboxCtl.getRawButton(1)) shooter.set(shooterCounter);
			if(xboxCtl.getPOV(0) == 0 && !dPadHeld && shooterCounter < 1.0) {
					shooterCounter += 0.1; 
					System.out.println("Shooter speed increased to " + shooterCounter);
			}
			else if(xboxCtl.getPOV(0) == 4 && !dPadHeld && shooterCounter > -1.0) { 
				shooterCounter -= 0.1; 
				System.out.println("Shooter speed decreased to " + shooterCounter);
			}
			if(xboxCtl.getPOV(0) == -1) dPadHeld = false;
			else dPadHeld = true;
			if(xboxCtl.getRawButton(2)) shooterCounter = 0;
			}
	}
	
	public void test() {
		
	}
		
	
}
