package org.mort11.configs.constants;

public final class PortConstants {

    public static final class Controller {
        public static final int DRIVE_CONTROLLER = 0;
        public static final int ENDEFFECTORCONTROLLER = 1;
        public static final int MANUAL_CONTROLLER = 2;

        public static final double DEAD_BAND = 0.05;
  }

    public static final class CommandSwerveDrivetrain {
	
	}

    public static final class Feeder {
        public static final int sparkFeeder = -1;
    }

    public static final class IntakeArmLeft {
        public static final int sparkIntakeLeft = -1;

    }

    public static final class IntakeArmRight {        
        public static final int sparkIntakeRight = 1;
    }

    public static final class IntakeRollerLeft {
        public static final int sparkRollLeft = 13;
    }

    public static final class IntakeRollerRight {
        public static final int sparkRollRight = 14;
    }

    public static final class Shooter {
        public static final int talonFXShooterLeader = -1;
        public static final int talonFXShooterFollowerA = -1;
        public static final int talonFXShooterFollowerB = -1;
    }

    public static final class Hood {
        public static final int servoHood = -1;
        public static int canCoderHood = -1;
    }

    public static final class Turret {
        public static final int TURRET_MOTOR = -1;
    }
}
