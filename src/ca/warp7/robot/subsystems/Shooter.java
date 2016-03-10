package ca.warp7.robot.subsystems;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.subsystems.shooterComponents.FlyWheel;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {
	
    private CANTalon hood;
    private FlyWheel flyWheel;
    private boolean fireAccess;
    private boolean hardStopShot;
    
    /**
     * @param motor controller Should be a TalonSRX
     */
    public Shooter(CANTalon hood_, Encoder enc, GearBox motor) {
    	flyWheel = new FlyWheel(motor, enc);
        hood = hood_;
        fireAccess = false;
        hardStopShot = false;
        
        //hood.changeControlMode(TalonControlMode.Position);
        //hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
        //hood.setPosition(0);
    }

    public double getSpeed() {
        return flyWheel.getRate();
    }
    
    public boolean atTargetRPM(){
    	return flyWheel.atTargetRPM();
    }
    
    public void periodic(double wantedRPM, boolean firing, Drive drive){
    	if(firing){
    		flyWheel.firing();
    	}else{
    		flyWheel.notFiring();
    	}
    	
    	if(fireAccess){
    		if(hardStopShot){
    			flyWheel.prepareToFire(Constants.HARDSTOP_RPM);
    		}else{
    			flyWheel.prepareToFire(wantedRPM);
    		}
    	}else{
    		flyWheel.prepareToFire(0.0);
    	}
    }
    
    public void set(double speed) {
        //flyWheel.set(speed);
    }

    public void stop() {
        //flyWheel.set(0);
    }

    public void safety() {
        // TODO if the hood is past some safety distance set the speed to 0
    }

    public void setHood(double degrees) {
        hood.set(degrees);
    }

	public void fireAccessGranted() {
		fireAccess = true;
	}

	public void fireAccessDenied() {
		fireAccess = false;
	}

	public void hardStop(boolean b) {
		hardStopShot = b;
	}
}
