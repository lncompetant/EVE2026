package org.mort11.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private static Climber climber;

    public Climber() {
    }

    public static Climber getInstance() {
        if (climber == null) {
            climber = new Climber();
        }
        return climber;
    }    
}
