package org.mort11.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;
import org.mort11.subsystems.Hood;

public class moveHood extends Command {
    private Hood hood;
    private double speed;
    
    public moveHood(double speed) {
        hood = Hood.getInstance();
        this.speed = speed;
        addRequirements(hood);
    }

    @Override
    public void execute() {
        hood.setHoodSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        hood.stop();
    }

    @Override
    public boolean isFinished() {
        return false;  
    }
}
