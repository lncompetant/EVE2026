package org.mort11.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.mort11.configs.constants.PortConstants;

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
    //

    Hood() {        
        hoodEncoder = new CANcoder(PortConstants.Hood.canCoderHood);
        encoderconfig = new CANcoderConfiguration();
        
        hoodEncoder.getConfigurator().apply(encoderconfig);

        hoodServo = new ServoHub(PortConstants.Hood.servoHood);
        hoodServo2 = new ServoHub(PortConstants.Hood.canCoderHood);

        hoodConfig = new ServoHubConfig();
        hoodConfig
        .channel1.pulseRange(500, 1500, 2500)
        .disableBehavior(ServoChannelConfig.BehaviorWhenDisabled.kSupplyPower);

        hoodConfig2 = new ServoHubConfig();
        hoodConfig2
        .channel1.pulseRange(500, 1500, 2500)
        .disableBehavior(ServoChannelConfig.BehaviorWhenDisabled.kSupplyPower);

        hoodServo.configure(hoodConfig, ResetMode.kResetSafeParameters);
        hoodServo2.configure(hoodConfig2, ResetMode.kResetSafeParameters);
    }

    public void setHoodPosition(double hoodAngleDegrees) {
        //Might be channel 0              
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        ServoChannel channel1 = hoodServo2.getServoChannel(ChannelId.kChannelId1);
        channel0.setPowered(true);
        channel0.setEnabled(true);
                                          
        channel1.setPowered(true);
        channel1.setEnabled(true);
        // I think convert targetRotation to pulse width here, but I'm not sure how to do that
        // I'll do research later just a placeholder for now
                                          
        // double pulse = 1500.0 + (hoodAngleDegrees * getCancoderPosition());
        
        //HARD PART NEEDS TO MAYBE POSSIBLY BE CONVERTED LATER

        // hoodPosDegrees = map(CurrentencoderPos, minEncoderPos, MaxencoderPos, minDegrees, maxDegrees);

        currenthoodPosDegrees = table.map(getCancoderPosition(), 0, 0, 0, 30);
        //uhh set limits on pulse width to be between 500 and 2500 
        
        double servopulse = table.map(currenthoodPosDegrees,80,45.0,500.0,2500.0);
        
        // PID control to adjust pulse width based on error between current position and target position
        pidEffort = pidController.calculate(currenthoodPosDegrees, hoodAngleDegrees);
        servopulse += pidEffort;

        // Ensure pulse width is within bounds
        servopulse = Math.max(500.0, Math.min(2500.0, servopulse));
        servopulse = table.map(pidEffort,-100.0,100.0,500.0,2500.0);

        //cast to int and set pulse width
        channel0.setPulseWidth((int) (servopulse));

        //map for reverse direction on second servo since inverted
        servopulse = table.map(servopulse,500,2500,2500.0,500.0);
        channel1.setPulseWidth((int) (servopulse));
        //not accounting for cancoder here
    }

    public double getCancoderPosition(){
        double position = hoodEncoder.getPosition().getValueAsDouble();
        return position;
    }

    // public double getCancoderDegrees(){
    //     double absrot = hoodEncoder.getPosition().getValueAsDouble();
    //     return absrot * 360;
    // }

    public double getHoodPosition() {
        // Implement method to return current hood position
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        if (channel0 == null) {
            return 0;
        }
        int pulseWidth = channel0.getPulseWidth();
        return (pulseWidth - 1500.0) / getCancoderPosition(); //Divided by rot
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
