package org.mort11.configs.constants;

import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public final class PIDConstants {
    public static final class CommandSwerveDrivetrain {

    }
    
    public static final class Feeder {

    }

    public static final class IntakeArmLeft {
        public final static double down = 0;
        public final static double up = 0;

    }

    public static final class IntakeArmRight {
        public final static double down = 0;
        public final static double up = 0;

    }

    public static final class IntakeRollerLeft {

    }

    public static final class IntakeRollerRight {

    }

    public static final class Hood {

		//PID Rotational
		public final static double ROT_KP = 0.25;
		public final static double ROT_KI = 0;
		public final static double ROT_KD = 0.01; // try 0.1, 0.15, 0.2

		public final static Constraints ROT_CONSTRAINTS = new Constraints(1000, 100);

        public final static double ROT_TOLERANCE = 0.1;
        public final static double ROT_SPEED_TOLERANCE = 2;
    }

    public static final class Turret {
        //all units deg - deg/sec ...
        //FeedForward
		public final static double ROT_KS = 0;
		public final static double ROT_KV = 0;
		public final static double ROT_KA = 0;

		//PID Rotational
		public final static double ROT_KP = 0.001; //0.2 from desginer Evan (i feel this could be right)
		public final static double ROT_KI = 0;
		public final static double ROT_KD = 0;
        //start slow, (180, 180), raise first number until it makes no change, then slowly increase second number, repeat
		public final static Constraints ROT_CONSTRAINTS = new Constraints(10, 180); //start slow, (180, 180), raise first number until it makes no change, then slowly increase second number, repeat
    }

    public static final class Shooter {
        public final static double RPM_KS = 0;
		public final static double RPM_KV = 0.00029;
		public final static double RPM_KA = 0;

        public final static double RPM_CHANGE_PER_SEC = 1000;

        public static final double SLEW_RATE_LIMIT = RPM_CHANGE_PER_SEC / PhysicalConstants.Shooter.MAX_SHOOTER_RPM;
    }
}
