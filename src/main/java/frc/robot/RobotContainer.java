// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
// import com.pathplanner.lib.commands.PathPlannerAuto; commented out bc pathplanner errors

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.actions.endeffector.manual.moveFeeder;
import frc.robot.commands.actions.endeffector.manual.moveLeftIntake;
import frc.robot.commands.actions.endeffector.manual.moveLeftRoller;
import frc.robot.commands.actions.endeffector.manual.moveRightIntake;
import frc.robot.commands.actions.endeffector.manual.moveRightRoller;
import frc.robot.commands.autons.apriltag.Angle2AprilTag;
// import frc.robot.commands.autons.BasicCommands; commented out for now bc pathplanner errors
import frc.robot.commands.autons.apriltag.LimelightTest;
import frc.robot.commands.autons.pathplanner.BasicCommands;
import frc.robot.commands.autons.timed.Taxi;
import frc.robot.configs.constants.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;
import com.pathplanner.lib.path.PathPlannerPath;


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

    private final CommandXboxController joystick = new CommandXboxController(0);
    private final CommandXboxController visionController = new CommandXboxController(1);
    private final CommandXboxController endeffectControl = new CommandXboxController(2);

    
    public final static CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    
    // private PathPlannerPath pathPlannerTest = PathPlannerPath.fromPathFile("DriveVertical");
    // private Command autonomousCommand = AutoBuilder.followPath(pathPlannerTest);

    
    //armaan bind linlight command to a button on the operator controller make object then do in configure bindings
    //im so sorry anthony i have failed yoour diing wish sammy if you are reading this right now please do his wish
   
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
                    drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                        .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                        .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
                )
            );
    
            // Idle while the robot is disabled. This ensures the configured
            // neutral mode is applied to the drive motors while disabled.
            final var idle = new SwerveRequest.Idle();
            RobotModeTriggers.disabled().whileTrue(
                drivetrain.applyRequest(() -> idle).ignoringDisable(true)
            );
    
            joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
            joystick.b().whileTrue(drivetrain.applyRequest(() ->
                point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
            ));
            // uehheh ehfeuguuegeufuhehehfuef
            // Run SysId routines when holding back/start and X/Y.
            // Note that each routine should be run exactly once in a single log.
            joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
            joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
            joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
            joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
            
            // Reset the field-centric heading on left bumper press.
            joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
    
            drivetrain.registerTelemetry(logger::telemeterize);
            
            joystick.x().whileTrue(new Angle2AprilTag(0));

            //Subsystem commands for the endeffector, binded to the operator controller
            new Trigger(() -> endeffectControl.getLeftY() > 0.05).whileTrue(new moveLeftIntake(endeffectControl));
            new Trigger(() -> endeffectControl.getLeftY() < -0.05).whileTrue(new moveLeftIntake(endeffectControl));

            new Trigger(() -> endeffectControl.getRightY() > 0.05).whileTrue(new moveRightIntake(endeffectControl));
            new Trigger(() -> endeffectControl.getRightY() < -0.05).whileTrue(new moveRightIntake(endeffectControl));
            
            endeffectControl.leftBumper().whileTrue(new moveRightRoller(0.5));
            endeffectControl.rightBumper().whileTrue(new moveLeftRoller(0.5));
            
            endeffectControl.pov(0).whileTrue(new moveFeeder(0.5));
            endeffectControl.pov(180).whileTrue(new moveFeeder(-0.5));

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
