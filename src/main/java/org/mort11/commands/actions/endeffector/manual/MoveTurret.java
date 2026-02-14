package org.mort11.commands.actions.endeffector.manual;

import org.mort11.subsystems.Turret;

import edu.wpi.first.wpilibj2.command.Command;

public class MoveTurret extends Command  {
    
    private Turret turret;

    private double speed;

    public MoveTurret(double speed) {
        this.speed = speed;

        turret = Turret.getInstance();

        addRequirements(turret);
    }

    @Override
    public void execute() {
      turret.setTurretMotorPercent(speed);
    }

  @Override
  public boolean isFinished(){
    return false;
  }

  @Override
  public void end(boolean interrupted){
    turret.setTurretMotorPercent(0);
  }
}
