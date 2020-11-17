package TestingHarness;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.Map;
import java.util.SortedMap;

public class ChiSquareTest implements GoodnessOfFitTest{
    public double RunTest(SortedMap<SubSequence,Integer> subSequenceFrequencyMap, UniformRealDistribution theoreticalDist) {
        double chiSquare = 0;
        int NumOfSamples = subSequenceFrequencyMap.values().stream().mapToInt(val -> val).sum();
        int degreeOfFreedom = subSequenceFrequencyMap.keySet().size() - 1;

        for(Map.Entry<SubSequence,Integer> observed : subSequenceFrequencyMap.entrySet()) {
            int categoryIndex = observed.getKey().getIntCode();
            int observedFrequency = observed.getValue();
            double expectedFrequency = theoreticalDist.probability(categoryIndex,categoryIndex+1) * NumOfSamples;
            chiSquare += Math.pow(observedFrequency-expectedFrequency,2) / expectedFrequency;
        }

        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(degreeOfFreedom);
        double p_value = 1 - chiSquaredDistribution.cumulativeProbability(chiSquare);

        return p_value;
    }
}
