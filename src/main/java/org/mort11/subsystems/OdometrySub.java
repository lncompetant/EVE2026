package org.mort11.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.configs.LookUpTable;
import org.mort11.configs.constants.LookUpTableConstants;
import org.mort11.configs.constants.VisionConstants;

public class OdometrySub extends SubsystemBase {

    private static OdometrySub instance;

    private final CommandSwerveDrivetrain drivetrain;
    private final Field2d field;

    // // All 8 AprilTag IDs per hub — pairs: 19&20, 21&24, 25&26, 27&18
    // private static final int[] BLUE_HUB_TAG_IDS = { 18, 19, 20, 21, 24, 25, 26, 27 };

    // // All 8 AprilTag IDs per hub — pairs: 11&2, 3&4, 5&8, 9&10
    // private static final int[] RED_HUB_TAG_IDS  = { 2, 3, 4, 5, 8, 9, 10, 11 };

    private Translation2d blueHubCenter;
    private Translation2d redHubCenter;

    // change to hub pose i
    // private final Translation2d target = new Translation2d(16.5, 5.5);
    public OdometrySub(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.field = new Field2d();
        SmartDashboard.putData("Field", field);

        AprilTagFieldLayout layout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

        blueHubCenter = averageTagPositions(layout, VisionConstants.BLUE_HUB_TAG_IDS);
        redHubCenter = averageTagPositions(layout, VisionConstants.RED_HUB_TAG_IDS);

        // TODO: Verify these values on startup.
        SmartDashboard.putNumber("Blue Hub X", blueHubCenter.getX());
        SmartDashboard.putNumber("Blue Hub Y", blueHubCenter.getY());
        SmartDashboard.putNumber("Red Hub X", redHubCenter.getX());
        SmartDashboard.putNumber("Red Hub Y", redHubCenter.getY());
    }

    // Averages the XY positions of all hub AprilTags to find the hub's center.
    //
    private Translation2d averageTagPositions(AprilTagFieldLayout layout, int[] tagIDs) {
        double sumX = 0, sumY = 0;
        int count = 0;
        for (int id : tagIDs) {
            var pose = layout.getTagPose(id);
            if (pose.isPresent()) {
                sumX += pose.get().getX();
                sumY += pose.get().getY();
                count++;
            }
        }
        if (count == 0) return new Translation2d();
        return new Translation2d(sumX / count, sumY / count);
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
        // SmartDashboard.putNumber(
        //     "Distance To Target",
        //     getDistanceToTarget()
        // );
        double dist = getDistanceToTarget();
        SmartDashboard.putNumber("Distance to Hub (m)", dist);
        SmartDashboard.putNumber("Target Shooter RPM", LookUpTable.getNeededShooterRPM(dist));
        SmartDashboard.putNumber("Target Hood Angle",  LookUpTable.getNeededHoodAngle(dist));
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

    private Translation2d getHubPosition() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
            return redHubCenter;
        }
        return blueHubCenter;
    }

    public boolean hasVisionTarget() {
        for (String name : Vision.getLimelights()) {
            Vision.VisionMeasurement vm = Vision.getMeasurement(name);
            if (vm != null && vm.tagCount > 0) return true;
        }
        return false;
    }

    public double getDistanceToTarget() {
        return drivetrain.getState().Pose.getTranslation().getDistance(getHubPosition());
    }

    public double getTargetShooterRPM() {
        if (!hasVisionTarget()) return LookUpTableConstants.FALLBACK_SHOOTER_RPM;
        return LookUpTable.getNeededShooterRPM(getDistanceToTarget());
    }

    public double getTargetHoodAngle() {
        return LookUpTable.getNeededHoodAngle(getDistanceToTarget());
    }

    public Pose2d getPose() {
        return drivetrain.getState().Pose;
    }

    public static OdometrySub getInstance() {
        if (instance == null) throw new IllegalStateException("Odometry not initialized!");
        return instance;
    }

    public static OdometrySub getInstance(CommandSwerveDrivetrain drivetrain) {
        if (instance == null) {
            instance = new OdometrySub(drivetrain);
        }
        return instance;
    }

    public static void setInstance(OdometrySub o) {
        instance = o;
    }


}
