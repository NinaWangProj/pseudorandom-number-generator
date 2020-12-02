package TestingHarness;

import PRNG.PseudoRandomNumberGenerator;
import Utility.Coordinates;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.util.ArrayList;
import java.util.List;

public class DieHardMinimumDistanceSquareTest {

    public static double RunTest(PseudoRandomNumberGenerator RNG) {
        double[] testStatistics = new double[100];

        //generate 8000 points and find the minimum distance square
        for(int i = 0; i <100; i ++) {
            double minDistanceSquare = Double.MAX_VALUE;
            List<Coordinates> previousPoints = new ArrayList<>();

            for(int j = 0; j < 8000; j++) {
                Coordinates coordinates = new Coordinates(RNG.GetNext(), RNG.GetNext());

                if (!previousPoints.contains(coordinates)) {
                    for (Coordinates previousPoint : previousPoints) {
                        double distance = Math.pow(coordinates.getxCoordinate() - previousPoint.getxCoordinate(), 2)
                                + Math.pow(coordinates.getyCoordinate() - previousPoint.getyCoordinate(), 2);
                        minDistanceSquare = Math.min(minDistanceSquare, distance);
                    }
                    previousPoints.add(coordinates);
                }
            }
            double testStatistic = 1 - Math.exp(-minDistanceSquare/0.995);
            testStatistics[i] = testStatistic;
        }

        //ks test
        UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                0, 1);
        KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();
        double p_value = ksTest.kolmogorovSmirnovTest(theoreticalDist,testStatistics);

        return p_value;
    }
}
