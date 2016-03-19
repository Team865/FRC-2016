package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Admin extends ControllerSettings{

	private static double degrees;
    private static boolean increased;
    private static boolean changed;
    private static boolean changed2;
    private static boolean changed3;
    private static boolean changed4;
    private static boolean changed5;
    private static int wantedRPM;
    private static DataPool shooter_;
    private static boolean firing;
    
    @Override
    public void init(Drive drive){
    	degrees = 0.0;
    	firing = false;
    	increased = false;
    	changed = false;
    	changed2 = false;
    	changed3 = false;
    	changed4 = false;
    	changed5 = false;
    	wantedRPM = 0;
    	shooter_ = new DataPool("shooter");
    	drive.setDirection(Constants.INTAKE);
    }
    
    @Override
	public void periodic(XboxController driver, XboxController operator, Shooter shooter, Intake intake, Drive drive, DigitalInput photosensor, Climber climber, Compressor compressor){
        //if(degrees ==0)shooter.setHood(0.15);
        
        
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
                System.out.println(wantedRPM);
                increased = true;
            }
        }

        if(driver.getDPad() == 180){
            if(!increased){
                wantedRPM -= 200;

                if(wantedRPM >= -1000 && wantedRPM <= 0)wantedRPM = -1000;
                System.out.println(wantedRPM);
                increased = true;
            }
        }

        if(driver.getDPad() == 90){
            if(!increased){
                degrees += 0.05;
                
                if(degrees <= 0.15)degrees = 0.15;
                System.out.println(degrees);
                increased = true;
            }
        }

        if(driver.getDPad() == 270){
            if(!increased){
                degrees -= 0.05;
                
                if(degrees >= -0.15)degrees = -0.15;
                System.out.println(degrees);
                increased = true;
            }
        }

        if(driver.getDPad() == -1){
            increased = false;
        }

        if (driver.getLeftTrigger() >= 0.5) {
            if (driver.getRightBumperbutton()) {
                intake.intake(false);
                firing = true;
            } else {
            	firing = false;
                intake.intake(photosensor.get());
            }
        } else {
            intake.stopIntake();
        }

        if (driver.getBbutton()) {
            intake.outake();
        }

        wantedRPM = Math.max(-6000, Math.min(6000, wantedRPM));
        degrees = Math.max(-1, Math.min(1, degrees));

        if(driver.getRightTrigger() <= 0.5) {
        	wantedRPM = 0;
        	if(!driver.getRightBumperbutton()){
        		firing = false;
        	}
        }
        if(driver.getStartButton()) degrees = 0.0;
      
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
		drive.cheesyDrive(driver.getLeftY(), driver.getRightX(), driver.getLeftBumperbutton());

	}


	@Override
	public void logs(Shooter shooter) {
		shooter_.logDouble("Wanted RPM", wantedRPM);
        shooter_.logDouble("Current RPM", (shooter.getSpeed() * -1));
        shooter_.logDouble("Hood Degree", degrees);
	}

	@Override
	public double getWantedRPM() {
		return wantedRPM;
	}

	@Override
	public boolean isFiring() {
		return firing;
	}
}
