package TestingHarness;

import java.util.HashMap;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public interface GoodnessOfFitTest {

    public double RunTest(HashMap<String,Integer> subSequenceFrequencyMap, UniformRealDistribution theoreticalDist);
}
