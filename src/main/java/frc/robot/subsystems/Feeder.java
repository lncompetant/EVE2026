package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.constants.PortConstants;

public class Feeder extends SubsystemBase {
    private static Feeder feeder;
    private SparkMax feedMotor;
    private SparkMaxConfig feedConfig;
    private double motorSpeed = 0;

    public Feeder() {
        feedMotor = new SparkMax(PortConstants.Feeder.sparkFeeder, MotorType.kBrushless);
        feedConfig = new SparkMaxConfig();

        feedMotor.configure(feedConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setSpeed(double speed){
        feedMotor.set(speed);
    }

    public static Feeder getInstance(){
        if (feeder == null){
            feeder = new Feeder();
        }
        return feeder;
    }
}

