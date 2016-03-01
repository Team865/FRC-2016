package ca.warp7.robot.subsystems.shooterComponents;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class FlyWheel {

	private GearBox flyWheel;
    private Encoder encoder;
    static double integral = 0.0;
    static double prevError = 0.0;
    static double prevTime = 0.0;
    private static int count;
    private boolean firing;
    private boolean atTargetRPM;
    
	public FlyWheel(GearBox motor, Encoder enc) {
		// TODO Auto-generated constructor stub
		firing = false;
		count = 0;
		flyWheel = motor;
		encoder = enc;
        encoder.setReverseDirection(true);
        encoder.setDistancePerPulse(3);
        prevTime = Timer.getFPGATimestamp();
        atTargetRPM = false;
	}
	
	public void prepareToFire(double wantedRPM) {
		double currentRPM = encoder.getRate();
		double percent = flyWheel.get();
	 	double RPM_Error = wantedRPM - currentRPM;
	 	
	 	if(wantedRPM == 0){
			flyWheel.set(0.0);
		}else{
			if(!firing){
				count++;
				if(wantedRPM <= 4000 || count >= 8 && wantedRPM <= 5000 || count >= 11 && wantedRPM <= 6000){
					//System.out.println(" Wanted RPM = " + wantedRPM);
					//if(percent == 1)System.out.println("Current RPM = " + currentRPM);
					System.out.println("Current RPM = " + currentRPM);
					System.out.println("         % is : " + percent*100);
					count = 0;
				
					
					double wantedError = 150;
					double increase = 0.005;
					
					if(RPM_Error >= 1500)increase = 0.02;
					
				    if(currentRPM <= wantedRPM+wantedError && currentRPM >= wantedRPM-wantedError){
				    	atTargetRPM = true;
				    }else{
				    	atTargetRPM = false;
				    	double interval = increase * RPM_Error/Math.abs(RPM_Error);
				   	 	double toChange = flyWheel.get() + interval;
				   	 	toChange = Math.max(-1, Math.min(1, toChange));
				    	flyWheel.set(toChange);
			        }
				}
			}
		}
		/*
		double currentRPM = encoder.getRate();
		double RPM_Error = currentRPM - wantedRPM;
		double interval = 0.005 * RPM_Error/Math.abs(RPM_Error);
		flyWheel.set(flyWheel.get() + interval);
		
		if(flyWheel.get() == 0.0 && wantedRPM != 0) flyWheel.set(0.7);
		if(wantedRPM == 0)flyWheel.set(0.0);
		*/
    }
	
	public boolean atTargetRPM(){
		return atTargetRPM;
	}

	public void set(double speed) {
		flyWheel.set(speed);
	}

	public double getRate() {
		return encoder.getRate();
	}
	
	public void firing(){
		firing = true;
	}
	
	public void notFiring(){
		firing = false;
	}
	
	
	/*
    public void fire() {
        double wantedRPM = Warp7Robot.wantedRPM * -1;
        if (readyToFire(wantedRPM)) {
            Intake.intake(false);
        }
    }
    private boolean readyToFire(double wantedRPM) {
        double currentRPM = encoder.getRate();
        double error = 200;

        return currentRPM >= wantedRPM - error && currentRPM <= wantedRPM + error;
    }
*/ // TODOimpl this

}
