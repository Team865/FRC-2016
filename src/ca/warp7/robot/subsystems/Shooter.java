package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.HOOD_PIN;
import static ca.warp7.robot.Constants.SHOOTER_CAN_ID;
import static ca.warp7.robot.Constants.SPIKE_PIN;

import ca.warp7.robot.subsystems.shooterComponents.FlyWheel;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {

	private VictorSP hood;
	private FlyWheel flyWheel;
	private Relay flashLight;

	public Shooter() {
		flyWheel = new FlyWheel(new CANTalon(SHOOTER_CAN_ID));
		hood = new VictorSP(HOOD_PIN);
		flashLight = new Relay(SPIKE_PIN);

		// hood.changeControlMode(TalonControlMode.Position);
		// hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		// hood.setPosition(0);
	}


	public void setLight(boolean b){
		if(b)
			if(!flashLight.get().equals(Relay.Value.kForward))
				flashLight.set(Relay.Value.kForward);
		else
			if(!flashLight.get().equals(Relay.Value.kOff))
				flashLight.set(Relay.Value.kOff);
	}
	
	public double getSpeed() {
		return flyWheel.getSpeed();
	}

	public boolean atTargetRPM() {
		return flyWheel.atTargetRPM();
	}

	public void periodic() {
		// wew
	}

	public void set(double speed) {
		// flyWheel.set(speed);
	}

	public void stop() {
		flyWheel.coast();
	}

	public void setHood(double power) {
		hood.set(power);
	}

	public void slowPeriodic() {
		flyWheel.slowPeriodic();
	}

	public void spinUp(double rpm) {
		flyWheel.spinUp(rpm);
	}
}
