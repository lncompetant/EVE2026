package org.mort11.configs;

import static org.mort11.configs.constants.LookUpTableConstants.*;

public class LookUpTable {
    public double table[][];

    private static final LookUpTable shooterSupersystemTable = new LookUpTable(SHOOTER_SUPERSYSTEM);

    public LookUpTable(double[][] table) {
        this.table = table;
    }

    public double[] lowerBounds(double independent) {
        int currentSpot = 0;

        System.out.println(independent);

        // for(int i = 0; independent < table[i][0]; i++) {
        //     System.out.println(independent);
        //     currentSpot = i;
        // }

        while(independent < table[currentSpot][0]) {
            currentSpot++;
        }

        if (currentSpot > 0) {
            currentSpot--;
        }

        return table[currentSpot];
    }

    public double[] upperBounds(double independent) {
        int currentSpot = 0;

        while(independent < table[currentSpot][0]) {
            currentSpot++;
        }

        return table[currentSpot];
    }

    public double[] linearInterpolation(double independentValue) {
        double[] output = new double[table[0].length];

        double[] lowerBounds = lowerBounds(independentValue);
        double[] upperBounds = upperBounds(independentValue);

        for(int i = 0; i < table[0].length; i++) {
            output[i] = map(independentValue, lowerBounds[0], upperBounds[0], lowerBounds[i], upperBounds[i]);
        }

        return output;
    }

    public static double map(double input, double minIn, double maxIn, double minOut, double maxOut) {
        double inRange = maxIn - minIn;
        double outRange = maxOut - minOut;
        
        return (((input - minIn) / inRange) * outRange) + minOut;
    }

    public static double limitedMap(double input, double minIn, double maxIn, double minOut, double maxOut) {
        return clamp(minOut, map(input, minIn, maxIn, minOut, maxOut), maxOut);
    }

    public static double clamp(double min, double data, double max) {
        return Math.max(Math.min(data, max), min);
    }

    public static double getNeededShooterRPM(double meters) {
        return shooterSupersystemTable.linearInterpolation(meters)[1];
    }

    public static double getNeededHoodAngle(double meters) {
        return shooterSupersystemTable.linearInterpolation(meters)[2];
    }
}
