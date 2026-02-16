package org.mort11.commands.actions.endeffector.manual;

import edu.wpi.first.wpilibj2.command.Command;

import org.mort11.subsystems.Climber;

public class MoveClimber extends Command {
    private Climber climber;
    private double speed;

    public MoveClimber(double speed) {
        climber = Climber.getInstance();
        this.speed = speed;
        addRequirements(climber);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        climber.setSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}