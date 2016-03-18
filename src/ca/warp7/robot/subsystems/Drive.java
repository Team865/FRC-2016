package ca.warp7.robot.subsystems;

import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class Drive {

    //https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
    private static int direction;
    private static GearBox rightGearBox;
    private static GearBox leftGearBox;
    private static Solenoid PTO;
    private static Solenoid gearChange;
    private ADXRS453Gyro gyro;
	private DataPool pool;

    public Drive(GearBox right, GearBox left, Solenoid PTO_, Solenoid gearChange_, Compressor comp) {
        rightGearBox = right;
        leftGearBox = left;
        direction = 1;
        PTO = PTO_;
        gearChange = gearChange_;
        PTO.set(false);
        gearChange.set(false);
        gyro = new ADXRS453Gyro();
        gyro.startThread();
        pool = new DataPool("Drive");
    }

    public void changeDirection() {
        direction *= -1;
    }
    
    public void setGear(boolean gear){
    	PTO.set(gear); // TODO gear pto swap
    }
    
    public void setDirection(int direction_){
    	direction = direction_;
    }

    public static void tankDrive(double left, double right) {
        left *= direction;
        right *= direction;

        left = createDeadband(left);
        right = createDeadband(right);

        move(left, right);
    }

    public static void cheesyDrive(double left, double right, boolean quickTurn) {
        double throttle = left;
        double wheel = right;

        throttle *= direction;
        wheel *= direction;

        throttle = createDeadband(throttle);
        wheel = createDeadband(wheel);

        if (throttle < 0 && !quickTurn) wheel *= -1; // chandler's modification
        else if (quickTurn && direction != -1) wheel *= -1; // my + chandler's modification

        if(!quickTurn) wheel = Math.max(-0.6, Math.min(0.6, wheel)); //  chandler's modification again
        if(!gearChange.get()) throttle = Math.max(-1, Math.min(1, throttle)); // limit power
        
        double angular_power = 0.0;
        double overPower = 0.0;
        double sensitivity = 1.5;
        double rPower = 0.0;
        double lPower = 0.0;
        if (quickTurn) {
            overPower = .25;
            sensitivity = .30;//used to be 0.75
            angular_power = wheel;
        } else {
            overPower = 0.0;
            angular_power = Math.abs(throttle) * wheel * sensitivity;
        }
        rPower = lPower = throttle;
        lPower += angular_power;
        rPower -= angular_power;
        if (lPower > 1.0) {
            rPower -= overPower * (lPower - 1.0);
            lPower = 1.0;
        } else if (rPower > 1.0) {
            lPower -= overPower * (rPower - 1.0);
            rPower = 1.0;
        } else if (lPower < -1.0) {
            rPower += overPower * (-1.0 - lPower);
            lPower = -1.0;
        } else if (rPower < -1.0) {
            lPower += overPower * (-1.0 - rPower);
            rPower = -1.0;
        }
        move(lPower, rPower);
    }

    private static void move(double left, double right) {
        //right *= 0.94;
    	//right *= 0.94;
    	rightGearBox.set(right * (-1));
        leftGearBox.set((left));
    }

    private static double createDeadband(double num) {
        if (0.13 >= Math.abs(num)) {
            num = 0;
        }

        num = Math.pow(num, 3);

        return num;
    }

    public void stop() {
        rightGearBox.set(0);
        leftGearBox.set(0);
    }

    public void changeGear() {
        PTO.set(!(PTO.get()));
    }

    public void changePTO() {
        gearChange.set(!(gearChange.get()));
    }

	public void overrideMotors(double d) {
		move(d, d);
	}
	
	public void anglePID(double goalAngle) {
		double error = (goalAngle - getRotation());
		double power = error * 0.1;
		move(power, -power);
	}
	public double getRotation() {
		return gyro.getAngle();
	}

	public void slowPeriodic() {
		pool.logDouble("gyro angle", getRotation());
	}
}
