package org.mort11.commands.actions.endeffector.pid;

import edu.wpi.first.wpilibj2.command.Command;

import org.mort11.subsystems.EvanHood;

public class SetEvanHood extends Command {
    private EvanHood hood;
    private double targetPositionDeg;

    public SetEvanHood(double targetPositionDeg) {
        this.hood = EvanHood.getInstance();
        this.targetPositionDeg = targetPositionDeg;

        addRequirements(hood);
    }

    @Override
    public void initialize() {
        hood.getPIDController().reset(hood.getHoodPositionDeg());
    }

    @Override
    public void execute() {
        hood.setHoodSpeed(
            hood.getPIDController().calculate(
                hood.getHoodPositionDeg(), 
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
        hood.setHoodSpeed(0);
    }
}
