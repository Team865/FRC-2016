package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {
    private static double INTAKE_SPEED = 1.0;
    private Solenoid initialArm;
    private Solenoid adjustingArm;
    private GearBox box;

    public Intake(GearBox motor, Solenoid initialArm_, Solenoid adjustingArm_) {
        box = motor;
        initialArm = initialArm_;
        adjustingArm = adjustingArm_;
        initialArm.set(false);
        adjustingArm.set(false);
    }

    public void reset(){
    	initialArm.set(true);
    }
    
    public void toggleInitialArm() {
        initialArm.set(!initialArm.get());
    }

    public void toggleAdjustingArm() {
        adjustingArm.set(!adjustingArm.get());
    }

    /**
     * @param sensorReading This is the photo-sensor reading
     *                      if it is false it will run
     */
    public void intake(boolean sensorReading) {
    	//put ! before sensorReading to fix
        if (sensorReading) {
            box.set(-INTAKE_SPEED);
        } else {
            box.set(0);
        }
    }

    public void outake() {
        box.set(INTAKE_SPEED);
    }

    public void stop() {
        box.set(0);
        initialArm.set(false);
        adjustingArm.set(false);
    }

    public void set(double speed) {
        box.set(speed);
    }

    public boolean adjustedArmRetracted() {
        return adjustingArm.get() == false;
    }

	public void stopIntake() {
		box.set(0);
	}

	public void raisePortculus(boolean b) {
		adjustingArm.set(!b);
		initialArm.set(!b);
	}

	public void lowerAdjustingArm() {
		adjustingArm.set(true);
		initialArm.set(true);
	}
}
