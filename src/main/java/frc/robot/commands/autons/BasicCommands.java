package frc.robot.commands.autons;

// import frc.robot.commands.autons.apriltag.AlignToTag;
// import frc.robot.commands.autons.apriltag.GoToAprilTag;
// import frc.robot.commands.autons.apriltag.HuntTag;
import frc.robot.configs.constants.PhysicalConstants;
import frc.robot.RobotContainer;

import com.pathplanner.lib.auto.NamedCommands;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class BasicCommands {
    public static CommandSwerveDrivetrain drivetrain;
    public static Vision vision;

    public static void setCommands() {
        // NamedCommands.registerCommand("GoLimelight",new GoToAprilTag(drivetrain, vision, vision.getTagId()).withTimeout(5.0));
    }
}
