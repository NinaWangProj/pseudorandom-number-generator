package TestingHarness;

import PRNG.PseudoRandomNumberGenerator;
import Utility.Coordinates;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DieHardMinimumDistanceSquareTest {

    public static double RunTest(PseudoRandomNumberGenerator RNG, boolean writeDebugOutput, String debugOutputPath) {
        int numTestIterations = 100;
        int numPoints = 8000;
        double[] testStatistics = new double[numTestIterations];

        StringBuilder[] debugOutput = null;
        if (writeDebugOutput) {
            debugOutput = new StringBuilder[numPoints + 1];
            for(int i = 0; i < numPoints + 1; i++){
                debugOutput[i] = new StringBuilder();
            }
        }

        //generate 8000 points and find the minimum distance square
        for(int i = 0; i <numTestIterations; i ++) {
            double minDistanceSquare = Double.MAX_VALUE;
             List<Coordinates> previousPoints = new ArrayList<>();

            for(int j = 0; j < numPoints; j++) {
                Coordinates coordinates = new Coordinates(RNG.GetNext(), RNG.GetNext());

                if (!previousPoints.contains(coordinates)) {
                    for (Coordinates previousPoint : previousPoints) {
                        double distance = Math.pow(coordinates.getxCoordinate() - previousPoint.getxCoordinate(), 2)
                                + Math.pow(coordinates.getyCoordinate() - previousPoint.getyCoordinate(), 2);
                        minDistanceSquare = Math.min(minDistanceSquare, distance);
                    }
                    previousPoints.add(coordinates);
                }

                if(writeDebugOutput && debugOutput != null) {
                    if (debugOutput[j + 1] == null)
                        debugOutput[j + 1] = new StringBuilder();

                    debugOutput[j + 1].append(String.format(",%d,%d", coordinates.getxCoordinate(), coordinates.getyCoordinate()));

                    if (j % 1000 == 1)
                        System.out.println("For TestStat " + i + ", Finished dist for " + j + " points");
                }
            }
            double testStatistic = 1 - Math.exp(-minDistanceSquare/0.995);
            testStatistics[i] = testStatistic;

            if(debugOutput != null)
                debugOutput[0].append(String.format(",x_%d,y_%d", i, i));
        }

        if(debugOutput != null)
        {
            try{
                FileWriter csvWriter = new FileWriter(debugOutputPath);

                for (int i = 0; i < debugOutput.length; i++) {
                    csvWriter.write(debugOutput[i].toString());
                    csvWriter.write("\n");
                }

                csvWriter.flush();
                csvWriter.close();
            }
            catch (IOException ex) { System.out.println(ex.getMessage()); }
        }

        //ks test
        UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                0, 1);
        KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();
        double p_value = ksTest.kolmogorovSmirnovTest(theoreticalDist,testStatistics);

        return p_value;
    }
}
