package org.mort11.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hood;

public class closeHood extends Command {
    private Hood hood;
    private double targetRotation;
    
    closeHood(double targetRotation) {
        hood = Hood.getInstance();
        this.targetRotation = targetRotation;
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.setHoodPosition(targetRotation);
    }

    @Override
    public void end(boolean interrupted) {
        hood.setHoodPosition(targetRotation);
    }

    @Override
    public boolean isFinished() {
        return false; // Change this to true when the hood is closed
    }
    
}
