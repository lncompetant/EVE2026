package org.mort11.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.sound.sampled.Port;

import org.mort11.configs.constants.PortConstants;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.Follower;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
        private static Shooter shooter;
    private final TalonFX shooterLeader;
    private final TalonFX shooterFollowerA;
    private final TalonFX shooterFollowerB;

    private final VelocityVoltage velocityRequest =
        new VelocityVoltage(0).withSlot(0);

    private static final double MAX_RPS = 7530.0 / 60.0; // 125.5


    public Shooter() {
        shooterLeader = new TalonFX(PortConstants.Shooter.talonFXShooterLeader);
        shooterFollowerA = new TalonFX(PortConstants.Shooter.talonFXShooterFollowerA);
        shooterFollowerB = new TalonFX(PortConstants.Shooter.talonFXShooterFollowerB);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.Slot0.kP = 0.15;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.01;
        config.Slot0.kV = 0.12;

        shooterLeader.getConfigurator().apply(config);
        shooterFollowerA.getConfigurator().apply(config);
        shooterFollowerB.getConfigurator().apply(config);

        // shooterFollowerA.setControl(new Follower(PortConstants.Shooter.talonFXShooterLeader, false));
        // shooterFollowerB.setControl(new Follower(PortConstants.Shooter.talonFXShooterLeader, false));
        
        // Probably need to indiviual set control modes for the followers
    }

    // public void setShooterSpeed(double speed) {
    //     velocityRequest.withVelocity(speed);
    //     shooterLeader.setControl(velocityRequest);
    //     shooterFollowerA.setControl(velocityRequest);
    //     shooterFollowerB.setControl(velocityRequest);
    // }

    public void setShooterPercent(double percent) {
        shooterLeader.set(percent);
        shooterFollowerA.set(percent);
        shooterFollowerB.set(percent);
    }

    public void setAdvancedShooterSpeed(double rpm) {    

    }

    public double getDistanceToHub() {
        return 0.0;
    }

    public double getAngleHood() {
        return 0.0;
    }

    public double getShooterSpeed() {
        return shooterLeader.getVelocity().getValueAsDouble();
    }

    public double getShooterRPM() {
        return shooterLeader.getVelocity().getValueAsDouble() * 60.0;
    }


    public static Shooter getInstance(){
        if (shooter == null){
            shooter = new Shooter();
        }
        return shooter;
    }
}

