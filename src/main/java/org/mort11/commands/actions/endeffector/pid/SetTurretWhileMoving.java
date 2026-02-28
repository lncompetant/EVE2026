package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;
import org.mort11.subsystems.CommandSwerveDrivetrain;
import org.mort11.subsystems.Turret;
import edu.wpi.first.math.geometry.Pose2d;

public class SetTurretWhileMoving extends Command {
    private final Turret turret;
    private final CommandSwerveDrivetrain drivetrain;
    private final Pose2d targetPose;

    public SetTurretWhileMoving(Pose2d targetPose) {
        this.turret = Turret.getInstance();
        this.drivetrain = CommandSwerveDrivetrain.getInstance(); //used only to get the robot speed
        this.targetPose = targetPose;
        
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.getPIDController().reset(turret.getTurretPosDeg());
    }

    @Override
    public void execute() {
        Pose2d currentRobotPose = drivetrain.getPose();
        double timeForBallToReachTarget = 67; //PLACEHOLDER, NEED TO FIND A WAY TO GET THIS VALUE AND CONSTANTLY UPDATE IT.
        ChassisSpeeds RobotSpeed = drivetrain.getFieldRelativeSpeeds();
      
        double FakeX = targetPose.getX() - (RobotSpeed.vxMetersPerSecond * timeForBallToReachTarget);  //make a fake hub pose for robot to aim at so that the velocity vectors cancel.
        double FakeY = targetPose.getY() - (RobotSpeed.vyMetersPerSecond * timeForBallToReachTarget);

        Pose2D FakeTargetPose = new Pose2d(FakeX,FakeY,targetPose.getRotation())

        double targetDeg = turret.calculateAngleToTarget(currentRobotPose, FakeTargetPose);  //calculate an angle with 2 poses

        turret.setTurretPosDeg(targetDeg);  //use the already made method to aim at hub
    }

    @Override
    public boolean isFinished() {
        return false; // Keep tracking as long as the command is scheduled
    }

    @Override
    public void end(boolean interrupted) {
        // Safety: stop the motor when the button is released
        turret.setTurretMotorPercent(0);
    }
}
