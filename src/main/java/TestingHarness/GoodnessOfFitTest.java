package TestingHarness;

import java.util.HashMap;
import java.util.SortedMap;

import org.apache.commons.math3.distribution.UniformRealDistribution;

public interface GoodnessOfFitTest {

    public double RunTest(SortedMap<SubSequence,Integer> subSequenceFrequencyMap, UniformRealDistribution theoreticalDist);
}
