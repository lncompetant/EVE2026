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
		public final static double ROT_KP = 0.05;
		public final static double ROT_KI = 0;
		public final static double ROT_KD = 0;

		public final static Constraints ROT_CONSTRAINTS = new Constraints(1, 1000);

        public final static double ROT_TOLERANCE = 0.5;
    }

    public static final class Turret {
        //all units deg - deg/sec ...
        //FeedForward
		public final static double ROT_KS = 0;
		public final static double ROT_KV = 0;
		public final static double ROT_KA = 0;

		//PID Rotational
		public final static double ROT_KP = 0.2; //0.2 from desginer Evan (i feel this could be right)
		public final static double ROT_KI = 0;
		public final static double ROT_KD = 0;
        //start slow, (180, 180), raise first number until it makes no change, then slowly increase second number, repeat
		public final static Constraints ROT_CONSTRAINTS = new Constraints(180, 180); //start slow, (180, 180), raise first number until it makes no change, then slowly increase second number, repeat
    }

    public static final class Shooter {

    }
}
