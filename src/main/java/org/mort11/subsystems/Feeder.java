package org.mort11.subsystems;

import static org.mort11.configs.constants.PortConstants.Feeder.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static Feeder feeder;
    private SparkMax feedMotor;
    private SparkMaxConfig feedConfig;
    private double motorSpeed = 0;

    public Feeder() {
        feedMotor = new SparkMax(sparkFeeder, MotorType.kBrushless);
        feedConfig = new SparkMaxConfig();

        feedMotor.configure(feedConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Feeder Pos Motor Rotations", getMotorRotationPosition());
        SmartDashboard.putNumber("Feeder Motor Speed RPM", getMotorRotationRPM());
    }

    public void setSpeed(double speed){
        feedMotor.set(speed);
    }

    public double getMotorRotationPosition() {
        return feedMotor.getEncoder().getPosition();
    }

    public double getMotorRotationRPM() {
        return feedMotor.getEncoder().getVelocity();
    }

    public static Feeder getInstance(){
        if (feeder == null){
            feeder = new Feeder();
        }
        return feeder;
    }
}

