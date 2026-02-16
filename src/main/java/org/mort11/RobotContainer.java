// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.mort11;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
// import com.pathplanner.lib.commands.PathPlannerAuto; commented out bc pathplanner errors

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import org.mort11.commands.actions.endeffector.manual.moveFeeder;
import org.mort11.commands.actions.endeffector.manual.moveLeftIntake;
import org.mort11.commands.actions.endeffector.manual.moveLeftRoller;
import org.mort11.commands.actions.endeffector.manual.moveRightIntake;
import org.mort11.commands.actions.endeffector.manual.moveRightRoller;
import org.mort11.commands.actions.endeffector.manual.moveHood;
import org.mort11.commands.actions.endeffector.manual.Shoot;
import org.mort11.commands.actions.endeffector.pid.setHood;
import org.mort11.commands.actions.endeffector.pid.setIntakeLeft;
import org.mort11.commands.actions.endeffector.pid.setIntakeRight;
import org.mort11.commands.actions.endeffector.manual.MoveTurret;
import org.mort11.commands.actions.endeffector.manual.Climb;

import org.mort11.commands.autons.apriltag.Angle2AprilTag;
// import org.mort11.commands.autons.BasicCommands; commented out for now bc pathplanner errors
import org.mort11.commands.autons.apriltag.LimelightTest;
import org.mort11.commands.autons.timed.Taxi;
import org.mort11.configs.constants.TunerConstants;
import org.mort11.subsystems.CommandSwerveDrivetrain;
import org.mort11.subsystems.Vision;
import com.pathplanner.lib.path.PathPlannerPath;

import static edu.wpi.first.units.Units.*;





import static org.mort11.configs.constants.PortConstants.Controller.*;
import static org.mort11.configs.constants.PhysicalConstants.*;

public class RobotContainer {
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandPS5Controller driveController = new CommandPS5Controller(DRIVE_CONTROLLER);
    private final CommandXboxController endeffectorController = new CommandXboxController(ENDEFFECTOR_CONTROLLER);
    private final CommandXboxController manualController = new CommandXboxController(MANUAL_CONTROLLER);

    
    public final static CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    
    // private PathPlannerPath pathPlannerTest = PathPlannerPath.fromPathFile("DriveVertical");
    // private Command autonomousCommand = AutoBuilder.followPath(pathPlannerTest);

   
    private final Vision vision = Vision.getInstance();
    
    
    public static SendableChooser<Command> autoChooser;

    public AutoBuilder autoBuilder;
        public RobotContainer() {
            drivetrain.configureAutoBuilder();
            configureBindings();
            configureAuto();
        }
    
        private void configureBindings() {
            // Note that X is defined as forward according to WPILib convention,
            // and Y is defined as to the left according to WPILib convention.
            drivetrain.setDefaultCommand(
                // Drivetrain will execute this command periodically
                drivetrain.applyRequest(() ->
                    drive.withVelocityX(-driveController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                        .withVelocityY(-driveController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                        .withRotationalRate(-driveController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
                )
            );
    
            // Idle while the robot is disabled. This ensures the configured
            // neutral mode is applied to the drive motors while disabled.
            final var idle = new SwerveRequest.Idle();
            RobotModeTriggers.disabled().whileTrue(
                drivetrain.applyRequest(() -> idle).ignoringDisable(true)
            );
    
            driveController.cross().whileTrue(drivetrain.applyRequest(() -> brake));
            driveController.circle().whileTrue(drivetrain.applyRequest(() ->
                point.withModuleDirection(new Rotation2d(-driveController.getLeftY(), -driveController.getLeftX()))
            ));

            // uehheh ehfeuguuegeufuhehehfuef
            // Run SysId routines when holding back/start and X/Y.
            // Note that each routine should be run exactly once in a single log.
            // driveController.back().and(driveController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
            // driveController.back().and(driveController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
            // driveController.start().and(driveController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
            // driveController.start().and(driveController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
            
            // Reset the field-centric heading on left bumper press.
            driveController.L1().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
    
            drivetrain.registerTelemetry(logger::telemeterize);
            
            driveController.square().whileTrue(new Angle2AprilTag(0));

            //Subsystem commands for the endeffector, binded to the operator controller
            //Intake Arms
            new Trigger(() -> manualController.getLeftY() > DEAD_BAND).whileTrue(new moveLeftIntake(manualController));
            new Trigger(() -> manualController.getLeftY() < -DEAD_BAND).whileTrue(new moveLeftIntake(manualController));

            new Trigger(() -> manualController.getRightY() > DEAD_BAND).whileTrue(new moveRightIntake(manualController));
            new Trigger(() -> manualController.getRightY() < -DEAD_BAND).whileTrue(new moveRightIntake(manualController));
            
            //Set Intake
            manualController.a().onTrue(setIntakeLeft.intake());
            manualController.b().onTrue(setIntakeLeft.up());

            manualController.x().onTrue(setIntakeRight.intake());
            manualController.y().onTrue(setIntakeRight.up());

            //Intake Roller
            manualController.leftBumper().onTrue(new moveLeftRoller(0.5));
            manualController.rightBumper().whileTrue(new moveRightRoller(0.5));

            //Feeder
            manualController.pov(0).whileTrue(new moveFeeder(0.5));
            manualController.pov(180).whileTrue(new moveFeeder(-0.5));

            //Turret
            manualController.pov(90).whileTrue(new MoveTurret(Turret.MANUAL_SPEED));
            manualController.pov(270).whileTrue(new MoveTurret(-Turret.MANUAL_SPEED));

            //Manual shooting
            new Trigger(() -> manualController.getLeftTriggerAxis() > 0.05).whileTrue(new Shoot(-1));
            new Trigger(() -> manualController.getRightTriggerAxis() > 0.05).whileTrue(new Shoot(1));
            
            //set Hood
            manualController.leftStick().onTrue(new setHood(45)); //up
            manualController.rightStick().onTrue(new setHood(80)); //down

            //manual hood control dont change is supposed to be weird
            new Trigger(() -> manualController.getLeftX() > DEAD_BAND).onTrue(new moveHood(-1)); //positive
            new Trigger(() -> manualController.getLeftX() < -DEAD_BAND).onTrue(new moveHood(1)); //negative

            new Trigger(() -> manualController.getRightX() > DEAD_BAND).onTrue(new Climb(1)); 
            new Trigger(() -> manualController.getRightX() < -DEAD_BAND).onTrue(new Climb(-1));
        }
        
        public Command getPathPlannerCommand(){
            try{
        // Load the path you want to follow using its name in the GUI
                PathPlannerPath path = PathPlannerPath.fromPathFile("DriveVertical");
        // Create a path following command using AutoBuilder. This will also trigger event markers.
                return AutoBuilder.followPath(path);
            } 
            catch (Exception e) {
                DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
                return Commands.none();
            }
  
        }
        public Command getAutonomousCommand() {
            // Simple drive forward auton
            return autoChooser.getSelected();
            // final var idle = new SwerveRequest.Idle();
            // return Commands.sequence(
            //     // Reset our field centric heading to match the robot
            //     // facing away from our alliance station wall (0 deg).
            //     drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            //     // Then slowly drive forward (away from us) for 5 seconds.
            //     drivetrain.applyRequest(() ->
            //         drive.withVelocityX(0.5)
            //             .withVelocityY(0)
            //             .withRotationalRate(0)
            //     )
            //     .withTimeout(5.0),
            //     // Finally idle for the rest of auton
            //     drivetrain.applyRequest(() -> idle)
            // );
        }
    
        public void configureAuto() {
        final var idle = new SwerveRequest.Idle();
        autoChooser = new SendableChooser<Command>();
        autoChooser.setDefaultOption("nothing", null);
        autoChooser.addOption("Pathplanner Rotate", new PathPlannerAuto("RotationAuto"));
        autoChooser.addOption("Pathplanner Vertical", new PathPlannerAuto("DriveAuto"));
        autoChooser.addOption("Pathplanner ZigZag", new PathPlannerAuto("ZigZagAuto"));
        autoChooser.addOption("Timed Taxi", new Taxi());
        autoChooser.addOption("Limelight Test", new LimelightTest(drivetrain, vision, 0));
        autoChooser.addOption("Drive forward nopathplan",Commands.sequence(
                // Reset our field centric heading to match the robot
                // facing away from our alliance station wall (0 deg).
                drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
                // Then slowly drive forward (away from us) for 5 seconds.
                drivetrain.applyRequest(() ->
                    drive.withVelocityX(0.5)
                        .withVelocityY(0)
                        .withRotationalRate(0)
                )
                .withTimeout(5.0),
                // Finally idle for the rest of auton
                drivetrain.applyRequest(() -> idle)));
        // Pathplanner autos WIP
        // autoChooser.addOption("LimelightTest", new PathPlannerAuto("Please Work")); 
    
                // drivetrain.applyRequest(() -> idle)
            // );
        }
    
    //     public void configureAuto() {
    //     autoChooser = new SendableChooser<Command>();
    //     autoChooser.setDefaultOption("nothing", null);
    //     autoChooser.addOption("Timed Taxi", new Taxi());
    //     autoChooser.addOption("LimelightTest", new PathPlannerAuto("Please Work"));
    //     SmartDashboard.putData("Auton Chooser", autoChooser);
    // }

    //     public static Command getPlanned(String plan) {
    //         BasicCommands.setCommands();
    //         return new PathPlannerAuto(plan);
    // } incorrect thing
        
        public static CommandSwerveDrivetrain getSwerveDrivetrain() {
            return drivetrain;
    }
}
