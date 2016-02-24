package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Shooter {

    static double integral = 0.0;
    static double prevError = 0.0;
    static double prevTime;
    private CANTalon hood;
    private Encoder encoder;
    private GearBox flyWheel;

    /**
     * @param motor controller Should be a TalonSRX
     */
    public Shooter(CANTalon hood_, Encoder enc, GearBox motor) {
        flyWheel = motor;
        encoder = enc;
        hood = hood_;
        encoder.setReverseDirection(true);
        encoder.setDistancePerPulse(3);
        
        prevTime = Timer.getFPGATimestamp();
        //hood.changeControlMode(TalonControlMode.Position);
        //hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
        //hood.setPosition(0);

    }

    public double getSpeed() {
        return encoder.getRate();
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

    public void prepareToFire(double wantedRPM) {
        double Kp = 0.01;
        double Ki = 0.0;
        double Kd = 0.0;
        double setpoint = wantedRPM;
        double PV = encoder.getRate() * -1;
        double currTime = Timer.getFPGATimestamp();
        double Dt = currTime - prevTime;
        /*
		   * Pseudo code (source Wikipedia)
		   * 
		     previous_error = 0
		     integral = 0 
		   start:
		     error = setpoint � PV [actual_position]
		     integral = integral + error*dt
		     derivative = (error - previous_error)/dt
		     output = Kp*error + Ki*integral + Kd*derivative
		     previous_error = error
		     wait(dt)
		     goto start
		   */
        // calculate the difference between
        // the desired value and the actual value
        double error = setpoint - PV;
        // track error over time, scaled to the timer interval
        integral = integral + (error * Dt);
        // determine the amount of change from the last time checked
        double derivative = (error - prevError) / Dt;
        // calculate how much to drive the output in order to get to the
        // desired setpoint.
        double output = (Kp * error) + (Ki * integral) + (Kd * derivative);
        // remember the error for the next time around.
        prevError = error;
        prevTime = currTime;
        flyWheel.set(output);
		/*
		double currentRPM = encoder.getRate();
		double RPM_Error = currentRPM - wantedRPM;
		double interval = 0.005 * RPM_Error/Math.abs(RPM_Error);
		flyWheel.set(flyWheel.get() + interval);
		
		if(flyWheel.get() == 0.0 && wantedRPM != 0) flyWheel.set(0.7);
		if(wantedRPM == 0)flyWheel.set(0.0);
		*/
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
