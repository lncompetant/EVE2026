package org.mort11.commands.actions.endeffector.pid;

import org.mort11.subsystems.IntakeArmLeft;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.configs.constants.PIDconstants;

public class setIntakeLeft extends Command {
    private IntakeArmLeft intakeArmLeft;
    private double targetPosition;

    public setIntakeLeft(double targetPosition){
        intakeArmLeft = IntakeArmLeft.getInstance();
        this.targetPosition = targetPosition;
        addRequirements(intakeArmLeft);
    }
    public void initialize(){

    }
    public void execute(){
        intakeArmLeft.setArmPosition(targetPosition);
    }
    
    public boolean isFinished(){
        return false;
    }
    public void end(boolean interrupted){
        //coralCorral.setMotorPercent(gravitySpeed);
    }

    public static Command intake(){
        return new setIntakeLeft(PIDconstants.IntakeArmLeft.down);
    }

    public static Command up(){
        return new setIntakeLeft(PIDconstants.IntakeArmLeft.up);
    }
}
