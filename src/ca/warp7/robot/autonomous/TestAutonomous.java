package ca.warp7.robot.autonomous;

import edu.wpi.first.wpilibj.Timer;

public class TestAutonomous {

    public static double sinAuto(double distance) {

        distance = Math.cos(Timer.getFPGATimestamp() / 5);

        return distance;

    }


}
