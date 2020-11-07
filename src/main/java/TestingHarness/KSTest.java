package TestingHarness;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.HashMap;

public class KSTest implements GoodnessOfFitTest{
    public double RunTest(HashMap<String,Integer> frequencyMap, UniformRealDistribution theoreticalDist) {

        //construct cumulative distribution (cdf) for both empirical and theoretical distribution



    }

    private double CalculateTestStatistics() {
        double D = 0;
        //D=Max|EDF(x) - Theoretical(x)|





        return D;

    }
}
