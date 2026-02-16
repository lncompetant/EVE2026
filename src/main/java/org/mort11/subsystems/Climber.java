package org.mort11.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.configs.constants.PortConstants;

public class Climber extends SubsystemBase {
    private static Climber climber;
    private SparkMax climbMotor;
    private SparkMaxConfig climbConfig;
    private double motorSpeed = 0;

    public Climber() {
        climbMotor = new SparkMax(PortConstants.Climber.CLIMBER_MOTOR, MotorType.kBrushless);
        climbConfig = new SparkMaxConfig();

        climbMotor.configure(climbConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setSpeed(double speed){
        climbMotor.set(speed);
    }

    public static Climber getInstance(){
        if (climber == null){
            climber = new Climber();
        }
        return climber;
    }
}
