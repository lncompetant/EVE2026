package org.mort11.subsystems;

import org.mort11.configs.constants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeArmLeft extends SubsystemBase{
    public static IntakeArmLeft intake;
    private SparkMax intakeMotor;
    private SparkMaxConfig intakeConfig;
    private ProfiledPIDController positionController;
    private SparkClosedLoopController pidControl;
    private double setpoint;
    private double motorSpeed = 0;
    private final double KG = 0.1;
    
    IntakeArmLeft() {
        intakeMotor = new SparkMax(PortConstants.IntakeArmLeft.sparkIntakeLeft, MotorType.kBrushless);
        intakeConfig = new SparkMaxConfig();
        
        intakeConfig.closedLoop
                .p(2)
                .i(0)
                .d(0);

        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        pidControl = intakeMotor.getClosedLoopController();
        // positionController = new ProfiledPIDController(2, 0, 0, null);
        // positionController.setTolerance(0.01);
    }

    public void setSpeed(double speed){
        intakeMotor.set(speed);
    }
    
    public void periodic() {
        SmartDashboard.putNumber("IntakeArmLeftPos", getPosition());
    }
    
    public void setSetpoint(double setpoint){
        this.setpoint = setpoint;
    }

    public double getPosition(){
        return intakeMotor.getEncoder().getPosition();
    }
    
    public ProfiledPIDController getPIDController(){
        return positionController;
    }
    
    public void setArmPosition(double targetPosition){
        pidControl.setSetpoint(targetPosition, SparkMax.ControlType.kPosition, ClosedLoopSlot.kSlot0, 0);
    }

    public void setMotorPercent(double motorSpeed){
        this.motorSpeed=motorSpeed+KG;
    }

    public static IntakeArmLeft getInstance(){
        if (intake == null){
            intake = new IntakeArmLeft();
        }
        return intake;
    }
}
