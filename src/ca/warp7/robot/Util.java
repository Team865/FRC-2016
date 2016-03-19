package ca.warp7.robot;

public class Util {
	public static double limit(double val) {
		return limit(val, 1);
	}

	public static double limit(double val, double lim) {
		return Math.max(-lim, Math.min(val, lim));
	}
}
