package ca.warp7.robot.subsystems.shooterComponents;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class EncoderThread implements Runnable {

	Encoder encoder;
	CANTalon gb;
	float wantedRPM = 0;
	volatile double currentRPM = 0;

	public EncoderThread(Encoder enc, CANTalon motor) {
		encoder = enc;
		gb = motor;
	}

	public void run() {
		while (true) {
			currentRPM = encoder.getRate();
			if (wantedRPM == 0) {
				gb.set(0.0);
			} else {
				if (currentRPM < wantedRPM) {
					gb.set(1.0);
				} else {
					gb.set(0.0);
				}
			}
		}
	}
}
