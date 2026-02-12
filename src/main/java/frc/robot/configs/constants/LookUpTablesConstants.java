package frc.robot.configs.constants;

public class LookUpTablesConstants {

    //distance in meters, shooter rpm, hood angle degrees (straight up = 90 deg)
    public double[][] shooterSupersystem = {
        {0, 0, 0},
        {10, 10, 10}
    };

    //distance in meters, time in air seconds
    public double[][] timeInAir = {
        {0, 0},
        {10, 10}
    };
}
