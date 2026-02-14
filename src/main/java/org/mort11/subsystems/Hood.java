package org.mort11.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.configs.constants.PortConstants;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.ResetMode;
import com.revrobotics.servohub.*;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.servohub.config.ServoChannelConfig;
import com.revrobotics.servohub.config.ServoHubConfig;
import com.revrobotics.encoder.*;


public class Hood extends SubsystemBase {
    public static Hood hood;
    public ServoHub hoodServo;
    public ServoHubConfig hoodConfig;
    //Estimate, temporarily using this as a placeholder
    public double rot = 1.9;
    public CANcoder hoodEncoder;
    
    Hood() {        
        hoodEncoder = new CANcoder(PortConstants.Hood.canCoderHood);
        hoodServo = new ServoHub(PortConstants.Hood.servoHood);
        hoodConfig = new ServoHubConfig();
        hoodConfig
        .channel0.pulseRange(500, 1500, 2500)
        .disableBehavior(ServoChannelConfig.BehaviorWhenDisabled.kSupplyPower);

        hoodServo.configure(hoodConfig, ResetMode.kResetSafeParameters);
    }

    public void setHoodSpeed(double targetRotation) {
        //Might be channel 0 
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        channel0.setPowered(true);
        channel0.setEnabled(true);
        // I think convert targetRotation to pulse width here, but I'm not sure how to do that
        //i'll do research later just a placeholder for now
        double pulse = 1500.0 + (targetRotation * rot);
        //uhh set limits on pulse width to be between 500 and 2500 
        pulse = Math.max(500.0, Math.min(2500.0, pulse));
        //cast to int and set pulse width
        channel0.setPulseWidth((int) Math.round(pulse));
        //not accounting for cancoder here
    }

    public void setHoodPosition(double targetPosition) {
        targetPosition = Math.max(0.0, Math.min(1.0, targetPosition)); 
        int minPulse = 500;
        int maxPulse = 2500;
        // Map 0 (up) / 1 (down) across the servo pulse range.
        // Change this to use cancoder value instead of target position, but for now just use target position as a placeholder
        // double rotations = targetPosition * rot;
        int pulse = (int) Math.round(minPulse + targetPosition * (maxPulse - minPulse));

        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        channel0.setPulseWidth(pulse);
    }

    public double getHoodPosition() {
        // Implement method to return current hood position
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        if (channel0 == null) {
            return 0;
        }
        int pulseWidth = channel0.getPulseWidth();
        return (pulseWidth - 1500.0) / rot;
    }
    
    public double getCancoderPosition(){
        double position = hoodEncoder.getPosition().getValueAsDouble();
        return position;
    }

    public void periodic() {
        SmartDashboard.putNumber("Hood Pos", getHoodPosition());
        SmartDashboard.putNumber("Hood Cancoder Pos", getCancoderPosition());
    }
    
    public static Hood getInstance() {
        if (hood == null) {
            hood = new Hood();
        }
        return hood;
    }
}
