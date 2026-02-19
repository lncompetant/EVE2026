package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.mort11.subsystems.Shooter;

public class SetShoot extends Command {
    private final Shooter shooter;
    private double RPM;

        public SetShoot(double RPM) {
            shooter = Shooter.getInstance();
            this.RPM = RPM;
            addRequirements(shooter);
        }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        shooter.setShooterRPM(RPM);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setShooterRPM(0);
    }

    @Override
    public boolean isFinished() {
        return false; // Change this to true when the shooting action is complete
    }
    
}
