package org.mort11.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Shooter;

public class shoot extends Command {
    private static Shooter shooter;
    private double speed;

        public shoot(double speed) {
            shooter = Shooter.getInstance();
            this.speed = speed;
            addRequirements(shooter);
        }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        shooter.setShooterSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setShooterSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false; // Change this to true when the shooting action is complete
    }
    
}
