package ca.warp7.robot.subsystems;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.hardware.GearBox;
import edu.wpi.first.wpilibj.Solenoid;

public class Drive {

    //https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
    private static int direction;
    private static GearBox rightGearBox;
    private static GearBox leftGearBox;
    private static Solenoid PTO;
    private static Solenoid gearChange;

    public Drive(GearBox right, GearBox left, Solenoid PTO_, Solenoid gearChange_) {
        rightGearBox = right;
        leftGearBox = left;
        direction = 1;
        PTO = PTO_;
        gearChange = gearChange_;
        PTO.set(false);
        gearChange.set(false);
    }

    public void changeDirection() {
        direction *= -1;
    }

    public void tankDrive() {
        double left = Warp7Robot.driver.getLeftY();
        double right = Warp7Robot.driver.getRightY();

        left *= direction;
        right *= direction;

        left = createDeadband(left);
        right = createDeadband(right);

        move(left, right);
    }

    public void cheesyDrive() {
        double throttle = Warp7Robot.driver.getLeftY();
        double wheel = Warp7Robot.driver.getRightX();

        throttle *= direction;
        wheel *= direction;

        throttle = createDeadband(throttle);
        wheel = createDeadband(wheel);

        boolean quickTurn = Warp7Robot.driver.getLeftBumperbutton();

        if (throttle < 0 && !quickTurn) wheel *= -1; // chandler's modification
        else if (quickTurn && direction != -1) wheel *= -1; // my + chandler's modification

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

    private void move(double left, double right) {
        right *= 0.94;
        rightGearBox.set(right * (-1));
        leftGearBox.set((left));
    }

    private double createDeadband(double num) {
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
}
