package org.mort11.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Odometry extends SubsystemBase {

    private final CommandSwerveDrivetrain drivetrain;
    private final Field2d field;

    // change to hub pose i
    private final Translation2d target = new Translation2d(16.5, 5.5);
    public Odometry(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.field = new Field2d();

        SmartDashboard.putData("Field", field);
    }

    @Override
    public void periodic() {
        Vision.updateRobotOrientation(drivetrain);

        // Add vision data from ALL cameras
        addVisionMeasurements();

        // Get fused pose (AFTER vision updates)
        Pose2d robotPose = drivetrain.getState().Pose;

        // Dashboard output
        SmartDashboard.putNumber("Robot X", robotPose.getX());
        SmartDashboard.putNumber("Robot Y", robotPose.getY());
        SmartDashboard.putNumber("Robot Heading",
            robotPose.getRotation().getDegrees());

        field.setRobotPose(robotPose);

        // Distance to target
        SmartDashboard.putNumber(
            "Distance To Target",
            getDistanceToTarget()
        );
    }

    private void addVisionMeasurements() {
        for (String name : Vision.getLimelights()) {

            Vision.VisionMeasurement vm = Vision.getMeasurement(name);

            if (vm == null) continue;

            // Must see at least 1 tag
            if (vm.tagCount == 0) continue;

            // Reject bad measurements
            if (vm.avgTagDist > 6.0) continue;

            Matrix<N3, N1> stdDevs =
                edu.wpi.first.math.VecBuilder.fill(
                    0.5 + vm.avgTagDist * 0.2,  // X
                    0.5 + vm.avgTagDist * 0.2,  // Y
                    999999                     // ignore rotation
                );

            drivetrain.addVisionMeasurement(
                vm.pose,
                vm.timestamp,
                stdDevs
            );

            // Debug per camera
            SmartDashboard.putBoolean(name + " Has Tag", vm.tagCount > 0);
            SmartDashboard.putNumber(name + " X", vm.pose.getX());
            SmartDashboard.putNumber(name + " Y", vm.pose.getY());
            SmartDashboard.putNumber(name + " Dist", vm.avgTagDist);
            SmartDashboard.putNumber(name + " Tags", vm.tagCount);
        }
    }

    // Distance calculation 
    public double getDistanceToTarget() {
        Pose2d pose = drivetrain.getState().Pose;
        return pose.getTranslation().getDistance(target);
    }

    public Pose2d getPose() {
        return drivetrain.getState().Pose;
    }
}
