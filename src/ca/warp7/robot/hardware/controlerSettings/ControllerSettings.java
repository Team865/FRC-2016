package ca.warp7.robot.hardware.controlerSettings;

public abstract class ControllerSettings {

	abstract public void reset();

	abstract public void periodic();

    public abstract void disabled();
}
