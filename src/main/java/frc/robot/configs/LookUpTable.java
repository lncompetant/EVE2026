package frc.robot.configs;

public class LookUpTable {
    public double table[][];

    public LookUpTable(double[][] table) {
        this.table = table;
    }

    public double[] lowerBounds(double independent) {
        int currentSpot = 0;

        while(independent < table[currentSpot][0]) {
            currentSpot++;
        }

        currentSpot--;
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

    public static double map(double data, double minIn, double maxIn, double minOut, double maxOut) {
        double inRange = maxIn - minIn;
        double outRange = maxOut - minOut;
        
        return (data * outRange) / inRange;
    }
}
