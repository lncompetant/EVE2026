package org.mort11.commands.actions.endeffector.pid;

import org.mort11.subsystems.IntakeArmRight;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.configs.constants.PIDconstants;

public class setIntakeRight extends Command {
    private IntakeArmRight intakeArmRight;
    private double targetPosition;

    public setIntakeRight(double targetPosition){
        intakeArmRight = IntakeArmRight.getInstance();
        this.targetPosition = targetPosition;
        addRequirements(intakeArmRight);
    }
    public void initialize(){

    }
    public void execute(){
        intakeArmRight.setArmPosition(targetPosition);
    }
    
    public boolean isFinished(){
        return false;
    }
    public void end(boolean interrupted){
        //coralCorral.setMotorPercent(gravitySpeed);
    }

    public static Command intake(){
        return new setIntakeRight(PIDconstants.IntakeArmRight.down);
    }

    public static Command up(){
        return new setIntakeRight(PIDconstants.IntakeArmRight.up);
    }
}
