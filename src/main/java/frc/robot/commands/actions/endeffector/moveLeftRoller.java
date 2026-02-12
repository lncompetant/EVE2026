package frc.robot.commands.actions.endeffector;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeRollerLeft;

public class moveLeftRoller extends Command {
    private IntakeRollerLeft intakeRollerLeft;
    private double speed;

    public moveLeftRoller(double speed){
        intakeRollerLeft = IntakeRollerLeft.getInstance();
        this.speed = speed;
        addRequirements(intakeRollerLeft);
    }

    public void initialize(){
    }

    public void execute(){
        intakeRollerLeft.setSpeed(speed);
    }

    public void end(boolean interrupted){
        intakeRollerLeft.setSpeed(0);
    }
    
    public boolean isFinished(){
        return false;
    }
}
