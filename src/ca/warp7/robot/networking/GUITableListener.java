package ca.warp7.robot.networking;

import ca.warp7.robot.Warp7Robot;
import edu.wpi.first.wpilibj.tables.*;

public class GUITableListener implements ITableListener {

	public GUITableListener() { }
	
	@Override
	public void valueChanged(ITable arg0, String arg1, Object arg2, boolean arg3) {
		if(arg1.equals("auton")) {
			double i = Double.parseDouble(arg2.toString());
			System.out.println("set auton to ID " + i);
			Warp7Robot.autonID = i;
		} else if(arg1.equals("distance")) {
			double i = Double.parseDouble(arg2.toString());
			System.out.println("got new distance of " + i);
			Warp7Robot.autonDistance = i;
		} else if(arg1.equals("azimuth")) {
			double i = Double.parseDouble(arg2.toString());
			System.out.println("got new angle of " + i);
			Warp7Robot.autonAngle = i;
		}
	}

}
