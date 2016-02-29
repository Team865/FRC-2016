package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.DigitalInput;

public abstract class ControllerSettings {

	abstract public void init(Drive drive);

	abstract public void periodic(XboxController driver, XboxController operator, ADXRS453Gyro gyro, Shooter shooter,
			Intake intake, Drive drive, DigitalInput photosensor, Climber climber);

	abstract public void drive(XboxController driver, XboxController operator);

	abstract public void logs(Shooter shooter);

	abstract public double getWantedRPM();

	abstract public boolean isFiring();
}
