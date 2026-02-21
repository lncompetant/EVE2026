package org.mort11.commands.actions.endeffector.pid;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SetSuperShooter extends SequentialCommandGroup {
    
    public SetSuperShooter(DoubleSupplier shooterRPM, DoubleSupplier turretDeg, DoubleSupplier hoodDeg) {
        addCommands(
            new SetShooter(shooterRPM),
            new SetTurret(turretDeg),
            new SetEvanHood(hoodDeg)
        );
    }
    
    public SetSuperShooter(double shooterRPM, double turretDeg, double hoodDeg) {
        addCommands(
            new SetShooter(shooterRPM),
            new SetTurret(turretDeg),
            new SetEvanHood(hoodDeg)
        );
    }
}
