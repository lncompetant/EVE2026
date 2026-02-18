package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;
import org.mort11.subsystems.Hood;

public class setHood extends Command {
    private Hood hood;
    private double targetAngle;
    
    public setHood(double targetAngle) {
        hood = Hood.getInstance();
        this.targetAngle = targetAngle;
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.setHoodPosition(targetAngle);
    }

    @Override
    public void end(boolean interrupted) {
        hood.stop();
    }

    @Override
    public boolean isFinished() {
        return false; // Change this to true when the hood is closed
    }
    
}
