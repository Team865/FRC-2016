package ca.warp7.robot.hardware;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.activation.MimeType;

import edu.wpi.first.wpilibj.SafePWM;

public class GearBox{

	private Object[] motors;	
	
	@SuppressWarnings("unchecked")
	public void set(double speed){
		for(int i = 0; i < motors.length; i++){
			try {
				Method m = motors.getClass().getDeclaredMethod("set", (Class<? extends SafePWM>[]) motors[i]);
				m.invoke((Class<? extends SafePWM>[]) motors[i], speed);
			} catch(Exception e) {
				System.err.println("Error setting PWM in " + e.getClass().getCanonicalName() 
						+ " because: " + e.getMessage());
			}
		}
	}
	
	public GearBox(Class<? extends SafePWM> motorType, int pins[]){
		if(pins.length >= 1 && pins.length <= 3){
			this.motors = new Object[pins.length];
			for(int i = 0; i < pins.length; i++){
				Constructor cons;
				try {
					cons = motorType.getConstructor(Integer.class);
					this.motors[i] = cons.newInstance(pins[i]);
				} catch (Exception e) {
					System.err.println("Failed to instantiate motor on pin " + pins[i]);
				}
			}
		}else{
			System.err.println("ThreeMotorGearBox initialized with " + pins.length + " motors instead of 3 or 2");
		}
	}
}
