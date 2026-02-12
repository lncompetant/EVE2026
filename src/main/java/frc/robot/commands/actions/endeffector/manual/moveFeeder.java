package frc.robot.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Feeder;

public class moveFeeder extends Command {
    private Feeder feeder;
    private double speed;

    public moveFeeder(double speed){
        feeder = Feeder.getInstance();
        this.speed = speed;
        addRequirements(feeder);
    }

    public void initialize(){
    }

    public void execute(){
        feeder.setSpeed(speed);
    }

    public void end(boolean interrupted){
        feeder.setSpeed(0);
    }
    
    public boolean isFinished(){
        return false;
    }   
}


