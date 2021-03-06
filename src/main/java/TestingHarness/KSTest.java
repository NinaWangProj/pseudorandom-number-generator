package TestingHarness;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.util.SortedMap;

public class KSTest implements GoodnessOfFitTest{
    public double RunTest(SortedMap<SubSequence,Integer> frequencyMap, UniformRealDistribution theoreticalDist) {
        //construct cumulative distribution (cdf) for both empirical and theoretical distribution
        int totalCount = frequencyMap.values().stream().mapToInt(i ->(Integer)i).sum();
        double cumulativeProbability = 0;
        double maxDistance = 0;

        //calculate test statistic Dn = Max|EDF-TheoreticalCDF|
        for(SortedMap.Entry<SubSequence,Integer> entry : frequencyMap.entrySet()) {
            double subSequence = (double)entry.getKey().getIntCode();
            //theoretical cumulative probability for the given subSequence:
            double theoreticalCumulativeProb = theoreticalDist.cumulativeProbability(subSequence);
            maxDistance = Math.max(maxDistance, Math.abs(theoreticalCumulativeProb - cumulativeProbability));

            cumulativeProbability += (double) entry.getValue() / totalCount;
        }

        //calculate p-value by using kolmogorov-smirnov distribution
        KolmogorovSmirnovTest ks = new KolmogorovSmirnovTest();
        double quantile_ks = ks.cdf(maxDistance,totalCount);
        double p_value = 1-quantile_ks;

        double quantile_ks1 = ks.cdf(0.05,totalCount);
        double p_value1 = 1-quantile_ks1;

        return p_value;
    }
}
