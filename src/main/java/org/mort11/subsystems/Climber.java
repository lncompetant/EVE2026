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
    private SparkMax climberMotor;
    private SparkMaxConfig climberConfig;

    public Climber() {
        climberMotor = new SparkMax(PortConstants.Climber.CLIMBER_MOTOR, MotorType.kBrushless);
        climberConfig = new SparkMaxConfig();

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);
    }

    public void setSpeed(double speed){
        climberMotor.set(speed);
    }

    public static Climber getInstance() {
        if (climber == null) {
            climber = new Climber();
        }
        return climber;
    }    
}
