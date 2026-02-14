package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;

import org.mort11.subsystems.Turret;

public class SetTurret extends Command {
    private Turret turret;
    private double targetPositionDeg;

    public SetTurret(double targetPositionDeg) {
        this.turret = Turret.getInstance();
        this.targetPositionDeg = targetPositionDeg;

        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.getPIDController().reset(turret.getTurretPosDeg());
    }

    @Override
    public void execute() {
        turret.setTurretMotorPercent(
            turret.getPIDController().calculate(
                turret.getTurretPosDeg(), 
                targetPositionDeg
            )
        );
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        turret.setTurretMotorPercent(0);
    }
}
