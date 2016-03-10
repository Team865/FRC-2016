package ca.warp7.robot.subsystems.shooterComponents;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.Encoder;

public class EncoderThread implements Runnable {
	
	Encoder encoder;
	GearBox gb;
	float wantedRPM = 0;
	volatile double currentRPM = 0;
	
	public EncoderThread(Encoder enc, GearBox gearbox) {
		encoder = enc;
		gb = gearbox;
	}

	public void run() {
		
		while(true) {
			currentRPM = encoder.getRate();
		 	if(wantedRPM == 0){
				gb.set(0.0);
			}else{
						if(currentRPM < wantedRPM) {
							gb.set(1.0);
						} else {
							gb.set(0.0);
						}
				}
			}
		}	
}
