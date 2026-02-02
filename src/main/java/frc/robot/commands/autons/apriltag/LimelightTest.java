package frc.robot.commands.autons.apriltag;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

//This is a test to check if Go To April Tag will work for pathplanner
public class LimelightTest extends SequentialCommandGroup {
    public LimelightTest(CommandSwerveDrivetrain drivetrain, Vision vision, int tagID) {
        addCommands(
            new SequentialCommandGroup(
                new WaitCommand(0.2)));
    }
}