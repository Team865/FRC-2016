package ca.warp7.robot.hardware;

import ca.warp7.robot.Constants;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SafePWM;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class GearBox{

	private SafePWM[] motors;
	private char[] motorTypes;
	private Encoder encoder;
	private Solenoid gearRatio;
	private Solenoid PTO;
	
	public void set(double speed){
		for(int i = 0; i < motorTypes.length; i++){
			switch(motorTypes[i]){
			case Constants.TALON:
				((Talon) motors[i]).set(speed);
				break;
			case Constants.VICTOR:
				((Victor) motors[i]).set(speed);
				break;
			}
		}
	}
	
	
	public GearBox(int[] pins, char[] motorTypes2){
		initMotors(pins, motorTypes2);
		encoder = null;
	}
	
	public GearBox(int[] pins, char[] motorTypes2, Encoder encoder, Solenoid gearRatio, Solenoid PTO){
		initMotors(pins, motorTypes2);
		this.encoder = encoder;
	}
	
	private void initMotors(int[]pins, char[] motorTypes2){
		if(pins.length == 3 || pins.length == 2 || pins.length == 1){
			motorTypes = motorTypes2;
			motors = new SafePWM[pins.length];
			
			for(int i = 0; i < motors.length; i++){
				switch(motorTypes2[i]){
				case Constants.TALON:
					motors[i] = new Talon(pins[i]);
					break;
				case Constants.VICTOR:
					motors[i] = new Victor(pins[i]);
					break;
				}
			}
		}else{
			System.err.println("ThreeMotorGearBox initialized with " + pins.length + " motors instead of 3 or 2");
		}
	}
	
	public double getDistance(){
		return encoder.getDistance();	
	}
	
	public double getRate(){
		return encoder.getRate();
	}
	
	public int getEncodingScale(){
		return encoder.getEncodingScale();		
	}
	
	public boolean getDirection(){
		return encoder.getDirection();
	}
	
	public void setDisancePerPulse(double distancePerPulse){
		encoder.setDistancePerPulse(distancePerPulse);
	}
	
	public void setReverseDirection(boolean reverseDirection){
		encoder.setReverseDirection(reverseDirection);
	}
	
	public void changePTO(){
		PTO.set(!(PTO.get()));
	}
	
	public void changeGear(){
		gearRatio.set(!(gearRatio.get()));
	}
}