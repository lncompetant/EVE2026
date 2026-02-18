package org.mort11.commands.actions.endeffector.manual;

import org.mort11.subsystems.Hood;

import edu.wpi.first.wpilibj2.command.Command;

public class MoveEvanHood extends Command  {
    
    private Hood hood;

    private double speed;

    public MoveEvanHood(double speed) {
        this.speed = speed;

        hood = Hood.getInstance();

        addRequirements(hood);
    }

    @Override
    public void execute() {
      hood.setHoodSpeed(speed);
    }

  @Override
  public boolean isFinished(){
    return false;
  }

  @Override
  public void end(boolean interrupted){
    hood.setHoodSpeed(0);
  }
}
