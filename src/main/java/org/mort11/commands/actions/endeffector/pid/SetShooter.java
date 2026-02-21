package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;

import org.mort11.subsystems.Shooter;

public class SetShooter extends Command {
    private final Shooter shooter;
    private DoubleSupplier RPM;

    public SetShooter(DoubleSupplier RPM) {
        shooter = Shooter.getInstance();
        this.RPM = RPM;

        addRequirements(shooter);
    }
    
    public SetShooter(double RPM) {
        shooter = Shooter.getInstance();
        this.RPM = () -> RPM;
            
        addRequirements(shooter);
    }
    
    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        shooter.setShooterRPM(RPM.getAsDouble());
        
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
