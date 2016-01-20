package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Intakes {

	private Victor vic;
	private Talon tal;
	private GearBox box;
	
	
	public Intakes(Victor motor){vic = motor;}
	public Intakes(Talon motor){tal = motor;}
	public Intakes(GearBox motor){box = motor;}
	
	public void intake(boolean sensor){
		
		if(!sensor){
			if(vic != null) vic.set(1);
			if(tal != null) tal.set(1);
			if(box != null) box.set(1);
		}else{
			stop();
		}
	}
	
	public void outake(){
		if(vic != null) vic.set(-1);
		if(tal != null) tal.set(-1);
		if(box != null) box.set(-1);
	}
	
	public void stop(){
		if(vic != null) vic.set(0);
		if(tal != null) tal.set(0);
		if(box != null) box.set(0);
	}
	
	public void set(double speed){
		if(vic != null) vic.set(speed);
		if(tal != null) tal.set(speed);
		if(box != null) box.set(speed);
	}
}
