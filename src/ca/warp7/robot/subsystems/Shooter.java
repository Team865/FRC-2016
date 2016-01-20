package ca.warp7.robot.subsystems;

import ca.warp7.robot.vision.ShooterHelper;
import edu.wpi.first.wpilibj.TalonSRX;

public class Shooter {

	TalonSRX flyWheel;
	
	
	public Shooter(TalonSRX motor){
		flyWheel = motor;
	}
	
	public void fire(Intakes intakes){
		prepareToFire(5);
		intakes.intake(false);
	}
	
	public void prepareToFire(double distance){
		flyWheel.set(ShooterHelper.getDesiredRPM(distance));
	}
}
