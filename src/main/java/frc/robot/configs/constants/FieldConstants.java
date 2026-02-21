package frc.robot.configs.constants;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class FieldConstants {
    
    public final static class MillField{
        public static final double FIELD_LENGTH = Units.feetToMeters(14.75);
        public static final double FIELD_WIDTH = Units.feetToMeters(27.7916667);
        public static final Translation2d FAKE_HUB = new Translation2d();

    }
}
