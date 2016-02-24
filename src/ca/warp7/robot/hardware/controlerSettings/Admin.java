package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.DigitalInput;

public class Admin {

	private static double degrees;
    private static boolean increased;
    private static boolean changed;
    private static boolean changed2;
    private static boolean changed3;
    private static boolean changed4;
    private static boolean changed5;
    private static int wantedRPM;
    
    public static void init(){
    	degrees = 0.0;
    	increased = false;
    	changed = false;
    	changed2 = false;
    	changed3 = false;
    	changed4 = false;
    	changed5 = false;
    	wantedRPM = 0;
    }
    
	public static void periodic(XboxController driver, XboxController operator, ADXRS453Gyro gyro, Shooter shooter, Intake intake, Drive drive, DataPool shooter_, DataPool gyro_, DigitalInput photosensor){
		shooter.periodic(wantedRPM);
        shooter.setHood(-0.25);

        
        
        if (driver.getAbutton()) {
            if (!changed2) {
                intake.toggleAdjustingArm();
                changed2 = true;
            }
        }else{
            changed2 = false;
        }

        if(driver.getDPad() == 0){
            if(!increased){
                wantedRPM += 200;
                if(wantedRPM <= 1000 && wantedRPM >= 0)wantedRPM = 1000;
                increased = true;
            }
        }

        if(driver.getDPad() == 180){
            if(!increased){
                wantedRPM -= 200;
                if(wantedRPM >= -1000 && wantedRPM <= 0)wantedRPM = -1000;
                increased = true;
            }
        }

        if(driver.getDPad() == 90){
            if(!increased){
                degrees += 2;
                //if(degrees <= 0.15)degrees = 0.15;
                increased = true;
            }
        }

        if(driver.getDPad() == 270){
            if(!increased){
                degrees -= 2;
                //if(degrees >= -0.15)degrees = -0.15;
                increased = true;
            }
        }

        if(driver.getDPad() == -1){
            increased = false;
        }

        if (driver.getLeftTrigger() >= 0.5) {
            if (driver.getRightBumperbutton()) {
                intake.intake(false);
            } else {
                intake.intake(photosensor.get());
            }
        } else {
            intake.stopIntake();
        }

        if (driver.getBbutton()) {
            intake.outake();
        }

        wantedRPM = Math.max(-6000, Math.min(6000, wantedRPM));
        degrees = Math.max(0, Math.min(90, degrees));

        if(driver.getRightTrigger() <= 0.5) wantedRPM = 0;
        if(driver.getStartButton()) degrees = 0.0;
        
        shooter_.logDouble("Wanted RPM", wantedRPM);
        shooter_.logDouble("Current RPM", (shooter.getSpeed() * -1));
        shooter_.logDouble("Hood Degree", degrees);
      
        shooter.setHood(degrees);

        if (driver.getRightStickButton()) {
            if (!changed) {
                drive.changeDirection();
                changed = true;
            }
        }else{
            changed = false;
        }

        if(driver.getLeftStickButton()){
            if(!changed3){
                drive.changeGear();
                changed3 = true;
            }
        }else{
            changed3 = false;
        }
        if(driver.getXbutton()){
            if(!changed4){
                drive.changePTO();
                changed4 = true;
            }
        }else{
            changed4 = false;
        }

        if(driver.getYbutton()){
            if(!changed5){
                intake.toggleInitialArm();
                changed5 = true;
            }
        }else{
            changed5 = false;
        }





        gyro_.logDouble("Angle ", gyro.getAngle());
        shooter_.logInt("Status", gyro.getStatus());
        shooter_.logBoolean("Calibrating?", gyro.isCalibrating());
        
        if(operator.getStartButton()){
            gyro.calibrate();
        }
        if(operator.getBackButton()){
            gyro.stopCalibrating();
        }

	}
}
