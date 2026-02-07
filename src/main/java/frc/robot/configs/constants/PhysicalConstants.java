package frc.robot.configs.constants;
import edu.wpi.first.math.util.Units;

public class PhysicalConstants {
    public final static class CommandSwerveDrivetrain {
        // The left-to-right distance between the drivetrain wheels measured from center
		// to center.
		public static final double DRIVETRAIN_TRACKWIDTH_METERS = Units.inchesToMeters(20.75);
		// The front-to-back distance between the drivetrain wheels measured from center
		// to center.
		public static final double DRIVETRAIN_WHEELBASE_METERS = Units.inchesToMeters(22.75);

		public static final double DRIVEBASE_RADIUS_METERS = Math.hypot(
			DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0
		);

		// public static final double FRONT_LEFT_OFFSET = 293.9;
		// public static final double FRONT_RIGHT_OFFSET = 273.1;
		// public static final double BACK_LEFT_OFFSET = 223.3;
		// public static final double BACK_RIGHT_OFFSET = 255.5;

		public static final double FRONT_LEFT_OFFSET = 21.3 + 90 + 180;
		public static final double FRONT_RIGHT_OFFSET = 3.08 + 90 + 180;
		public static final double BACK_LEFT_OFFSET = 311.75 + 90 + 180;
		public static final double BACK_RIGHT_OFFSET = 346.73 + 90 + 180;

		public static final int IMU_TO_ROBOT_FRONT_ANGLE = 270;

		public static final double WHEEL_COEFFICIENT_OF_FRICTION = 1;
		public static final double ROBOT_MASS = 45;
		// public static final double ROBOT_MOMENT_OF_INERTIA = ROBOT_MASS * 0.254 * 0.254 / 2;
		public static final double ROBOT_MOMENT_OF_INERTIA = 1.25;

		public static final double DRIVE_MOTOR_CURRENT_LIMIT = 60;
		public static final double DRIVE_MOTOR_MAX_RPM = 6000;

		public static final double DRIVE_REDUCTION = (16.0 / 50.0) * (28.0 / 16.0) * (15.0 / 45.0);
		public static final double WHEEL_DIAMETER = Units.inchesToMeters(4);  //0.1014
		public static final double ROTATIONS_TO_METERS = WHEEL_DIAMETER * Math.PI;
		public static final double MAX_SPEED = DRIVE_REDUCTION * ROTATIONS_TO_METERS * (DRIVE_MOTOR_MAX_RPM / 60);

		public static final double ODOMETRY_MULTIPLIER = 5.67;
		// public static final double ODOMETRY_MULTIPLIER = 5.575;
    }
}
