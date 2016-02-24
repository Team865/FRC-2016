package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.subsystems.shooterComponents.FlyWheel;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class Shooter {
	
    private CANTalon hood;
    private FlyWheel flyWheel;

    /**
     * @param motor controller Should be a TalonSRX
     */
    public Shooter(CANTalon hood_, Encoder enc, GearBox motor) {
        flyWheel = new FlyWheel(motor, enc);
        hood = hood_;
        
        //hood.changeControlMode(TalonControlMode.Position);
        //hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
        //hood.setPosition(0);
    }

    public double getSpeed() {
        return flyWheel.getRate();
    }
    
    public void periodic(double wantedRPM){
    	flyWheel.prepareToFire(wantedRPM);
    }
    
    public void set(double speed) {
        flyWheel.set(speed);
    }

    public void stop() {
        flyWheel.set(0);
    }

    public void safety() {
        // TODO if the hood is past some safety distance set the speed to 0
    }

    public void setHood(double degrees) {
        hood.set(degrees);
    }
}
