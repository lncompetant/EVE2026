package org.mort11.commands.autons.pathplanner;

import org.mort11.RobotContainer;
import org.mort11.commands.autons.timed.Taxi;
import org.mort11.configs.constants.PhysicalConstants;
import org.mort11.subsystems.CommandSwerveDrivetrain;
import org.mort11.subsystems.Vision;

import com.pathplanner.lib.auto.NamedCommands;

public class BasicCommands {
    public static CommandSwerveDrivetrain drivetrain;
    public static Vision vision;

    public static void setCommands() {
        // NamedCommands.registerCommand("GoLimelight",new GoToAprilTag(drivetrain, vision, vision.getTagId()).withTimeout(5.0));
        // NamedCommands.registerCommand("AlignToTag", new AlignToTag(drivetrain, vision, 0));
        NamedCommands.registerCommand("Taxi", new Taxi());
    }
}
