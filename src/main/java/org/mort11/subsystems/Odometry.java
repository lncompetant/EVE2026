package org.mort11.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.subsystems.LimelightHelpers; // make sure this exists

public class Odometry extends SubsystemBase {

    private final CommandSwerveDrivetrain drivetrain;
    private final Field2d field;

    // Example target (speaker, hub, etc.)
    private final Translation2d target = new Translation2d(16.5, 5.5); // change for your field

    public Odometry(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.field = new Field2d();

        SmartDashboard.putData("Field", field);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = drivetrain.getState().Pose;

        addVisionMeasurement();

        SmartDashboard.putNumber("Robot X", robotPose.getX());
        SmartDashboard.putNumber("Robot Y", robotPose.getY());

        field.setRobotPose(robotPose);

        double distance = getDistanceToTarget();
        SmartDashboard.putNumber("Distance To Target", distance);
    }

    private void addVisionMeasurement() {
        // Get MegaTag2 estimate (BEST option)
        LimelightHelpers.PoseEstimate estimate =
            LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("");

        if (estimate == null) return;

        // Must have at least 1 tag
        if (estimate.tagCount == 0) return;

        // Reject bad ambiguity (optional but recommended)
        if (estimate.avgTagDist > 6.0) return; // too far away = unreliable

        // Get pose and timestamp directly
        Pose2d visionPose = estimate.pose;
        double timestamp = estimate.timestampSeconds;

        // Add measurement to drivetrain
        drivetrain.addVisionMeasurement(visionPose, timestamp);

        // Debug
        SmartDashboard.putNumber("Vision X", visionPose.getX());
        SmartDashboard.putNumber("Vision Y", visionPose.getY());
        SmartDashboard.putNumber("Vision Tag Count", estimate.tagCount);
    }

    public double getDistanceToTarget() {
        Pose2d pose = drivetrain.getState().Pose;
        return pose.getTranslation().getDistance(target);
    }

    public Pose2d getPose() {
        return drivetrain.getState().Pose;
    }
}