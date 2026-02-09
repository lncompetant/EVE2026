package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configs.constants.PortConstants;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeRollerRight extends SubsystemBase {
    private static IntakeRollerRight intake;
    private SparkMax rollerMotor;
    private SparkMaxConfig rollerConfig;
    private double motorSpeed = 0;

    public IntakeRollerRight() {
        rollerMotor = new SparkMax(PortConstants.IntakeRollerRight.sparkRollRight, MotorType.kBrushless);
        rollerConfig = new SparkMaxConfig();

        rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setSpeed(double speed){
        rollerMotor.set(speed);
    }

    public static IntakeRollerRight getInstance(){
        if (intake == null){
            intake = new IntakeRollerRight();
        }
        return intake;
    }
}
