package frc.robot.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.constants.PortConstants;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.ResetMode;
import com.revrobotics.servohub.*;
import com.revrobotics.servohub.ServoChannel.ChannelId;
import com.revrobotics.servohub.config.ServoChannelConfig;
import com.revrobotics.servohub.config.ServoHubConfig;
import com.revrobotics.encoder.*;
import frc.robot.configs.LookUpTable;


public class Hood extends SubsystemBase {
    public static LookUpTable table;
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

    public void setHoodPosition(double targetRotation) {
        //Might be channel 0 
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        ServoChannel channel1 = hoodServo.getServoChannel(ChannelId.kChannelId1);
        channel0.setPowered(true);
        channel0.setEnabled(true);
        
        channel1
        // I think convert targetRotation to pulse width here, but I'm not sure how to do that
        //i'll do research later just a placeholder for now


        double pulse = 1500.0 + (targetRotation * getCancoderPosition());
        //HARD PART NEEDS TO MAYBE POSSIBLY BE CONVERTED LATER


        //uhh set limits on pulse width to be between 500 and 2500 
        pulse = table.map(pulse,0.0,1.0,500.0,2500.0);
        //cast to int and set pulse width
        channel0.setPulseWidth((int) (pulse));
        //not accounting for cancoder here
    }

    public double getHoodPosition() {
        // Implement method to return current hood position
        ServoChannel channel0 = hoodServo.getServoChannel(ChannelId.kChannelId0);
        if (channel0 == null) {
            return 0;
        }
        int pulseWidth = channel0.getPulseWidth();
        return (pulseWidth - 1500.0) / getCancoderPosition(); //Divided by rot
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
