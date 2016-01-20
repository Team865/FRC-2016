package ca.warp7.robot.hardware;

import ca.warp7.robot.Constants;
import edu.wpi.first.wpilibj.SafePWM;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class GearBox{

	private SafePWM[] motors;
	private char[] motorTypes;
	
	
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
}