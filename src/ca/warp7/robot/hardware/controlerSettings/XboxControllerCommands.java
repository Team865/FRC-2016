package ca.warp7.robot.hardware.controlerSettings;

public abstract class XboxControllerCommands{
	
	public double LEFT_TRIGGER_Activation = 0.1;
	public double RIGHT_TRIGGER_Activation = 0.1;
	
	
	public abstract void A_Pressed();
	public abstract void A_Held();
	public abstract void A_Released();
	
	public abstract void B_Pressed();
	public abstract void B_Held();
	public abstract void B_Released();
	
	public abstract void X_Pressed();
	public abstract void X_Held();
	public abstract void X_Released();
	
	public abstract void Y_Pressed();
	public abstract void Y_Held();
	public abstract void Y_Released();
	
	public abstract void BACK_Pressed();
	public abstract void BACK_Held();
	public abstract void BACK_Released();
	
	public abstract void START_Pressed();
	public abstract void START_Held();
	public abstract void START_Released();
	
	public abstract void LEFT_BUMPER_Pressed();
	public abstract void LEFT_BUMPER_Held();
	public abstract void LEFT_BUMPER_Released();
	
	public abstract void RIGHT_BUMPER_Pressed();
	public abstract void RIGHT_BUMPER_Held();
	public abstract void RIGHT_BUMPER_Released();
	
	public abstract void LEFT_STICK_Pressed();
	public abstract void LEFT_STICK_Held();
	public abstract void LEFT_STICK_Released();
	
	public abstract void RIGHT_STICK_Pressed();
	public abstract void RIGHT_STICK_Held();
	public abstract void RIGHT_STICK_Released();
	
	public abstract void DPAD_Pressed(int angle);
	public abstract void DPAD_Held(int angle);
	public abstract void DPAD_Released(int angle);
	
	public abstract void LEFT_TRIGGER_Pressed();
	public abstract void LEFT_TRIGGER_Held(double amount);
	public abstract void LEFT_TRIGGER_Held();
	
	public abstract void RIGHT_TRIGGER_Pressed();
	public abstract void RIGHT_TRIGGER_Held(double amount);
	public abstract void RIGHT_TRIGGER_Held();
}
