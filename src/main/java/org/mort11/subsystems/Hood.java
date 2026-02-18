package org.mort11.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.configs.constants.PortConstants;
import static org.mort11.configs.constants.PhysicalConstants.Hood.*;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.ResetMode;
import com.revrobotics.servohub.*;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.servohub.config.ServoChannelConfig;
import com.revrobotics.servohub.config.ServoHubConfig;
import com.revrobotics.encoder.*;
import edu.wpi.first.math.controller.PIDController;

import org.mort11.configs.LookUpTable;


public class Hood extends SubsystemBase {
    public static LookUpTable table;
    public static Hood hood;
    public ServoHub hoodServo;
    public ServoHub hoodServo2;
    public ServoHubConfig hoodConfig;
    public ServoHubConfig hoodConfig2;
    public double servopulse;
    public double currenthoodPosDegrees;
    public PIDController pidController = new PIDController(0.1, 0, 0);
    public double pidEffort;


    //Estimate, temporarily using this as a placeholder
    public double rot = 1.9;
    public CANcoder hoodEncoder;
    public CANcoderConfiguration encoderconfig;

    //hood calculations
    private double lastAbsPos = 0.0;
    private int rotationCount = 0;
    private double continuousPosition = 0.0;

    //

    Hood() {        
        hoodEncoder = new CANcoder(PortConstants.Hood.canCoderHood);
        encoderconfig = new CANcoderConfiguration();
        // encoderconfig.MagnetSensor.SensorDirection = 
        
        hoodEncoder.getConfigurator().apply(encoderconfig);

        hoodServo = new ServoHub(PortConstants.Hood.servoHood);
        hoodServo2 = new ServoHub(PortConstants.Hood.servoHood2);

        hoodConfig = new ServoHubConfig();
        hoodConfig
        .channel0.pulseRange(MIN_PULSE_WIDTH_SERVO, MIDDLE_PULSE_WIDTH_SERVO, MAX_PULSE_WIDTH_SERVO)
        .disableBehavior(ServoChannelConfig.BehaviorWhenDisabled.kSupplyPower);

        hoodConfig2 = new ServoHubConfig();
        hoodConfig2
        .channel1.pulseRange(MIN_PULSE_WIDTH_SERVO, MIDDLE_PULSE_WIDTH_SERVO, MAX_PULSE_WIDTH_SERVO)
        .disableBehavior(ServoChannelConfig.BehaviorWhenDisabled.kSupplyPower);

        hoodServo.configure(hoodConfig, ResetMode.kResetSafeParameters);
        hoodServo2.configure(hoodConfig2, ResetMode.kResetSafeParameters);

        pidController.setTolerance(0.5); 
    }

    public void setHoodPosition(double targetDeg) {
    ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
    ServoChannel channel1 = hoodServo2.getServoChannel(ChannelId.kChannelId1);

    channel0.setPowered(true);
    channel0.setEnabled(true);
    channel1.setPowered(true);
    channel1.setEnabled(true);

    double currentDeg = getHoodPosDeg();

    double pidOutput = pidController.calculate(currentDeg, targetDeg);

    // Clamp PID to -1 to 1 (speed command)
    pidOutput = Math.max(-1.0, Math.min(1.0, pidOutput));

    // Convert speed -> pulse
    double pulse = 1500 + (pidOutput * 1000);

    pulse = Math.max(1000, Math.min(2000, pulse));

    channel0.setPulseWidth((int) pulse);

    // Invert second servo
    channel1.setPulseWidth((int) (3000 - pulse));

    if (pidController.atSetpoint()) {
    channel0.setPulseWidth(1500);
    channel1.setPulseWidth(1500);
    }

    }

    public double setHoodSpeed(double speed) {
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        ServoChannel channel1 = hoodServo2.getServoChannel(ChannelId.kChannelId1);

        channel0.setPowered(true);
        channel0.setEnabled(true);
        channel1.setPowered(true);
        channel1.setEnabled(true);


        double pulse = 1500 + (speed * 1000); // -1 -> 500, 1 -> 2500
        pulse = Math.max(500, Math.min(2500, pulse));

        channel0.setPulseWidth((int) pulse);
        channel1.setPulseWidth((int) (3000 - pulse));

        return pulse;
    }

    public double getHoodPosDeg() {
        return getCancoderPosition() * DEG_PER_ENCODER_ROTATION;
    }


    public double getCancoderPosition() {
    double absPos = hoodEncoder.getAbsolutePosition().getValueAsDouble(); 
    // 0.0 → 1.0 rotations

    double delta = absPos - lastAbsPos;

    // Detect wrap forward (0.99 → 0.01)
    if (delta < -0.5) {
        rotationCount++;
    }

    // Detect wrap backward (0.01 → 0.99)
    if (delta > 0.5) {
        rotationCount--;
    }

    lastAbsPos = absPos;

    continuousPosition = rotationCount + absPos;

    return continuousPosition;
}


    public void stop() {
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        ServoChannel channel1 = hoodServo2.getServoChannel(ChannelId.kChannelId1);

        channel0.setPulseWidth(1500);
        channel1.setPulseWidth(1500);
    }

    // public double getCancoderDegrees(){
    //     double absrot = hoodEncoder.getPosition().getValueAsDouble();
    //     return absrot * 360;
    // }

    public void periodic() {
        getCancoderPosition(); // Update tracking of continuous position
        SmartDashboard.putNumber("Hood Deg", getHoodPosDeg());
        SmartDashboard.putNumber("Hood Cancoder Pos", getCancoderPosition());
    }
    
    public static Hood getInstance() {
        if (hood == null) {
            hood = new Hood();
        }
        return hood;
    }
}
