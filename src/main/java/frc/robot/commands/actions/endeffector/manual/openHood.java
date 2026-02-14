package frc.robot.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hood;

public class openHood extends Command {
    private Hood hood;
    private double targetRotation;

    openHood(double targetRotation) {
        hood = Hood.getInstance();
        this.targetRotation = targetRotation;
        addRequirements(hood);
    }


    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        hood.setHoodPosition(targetRotation);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setHoodPosition(0);

    }

    @Override
    public boolean isFinished() {
        // Code to determine when the hood is fully open
        return false; // Change this to true when the hood is open
    }
    
}
