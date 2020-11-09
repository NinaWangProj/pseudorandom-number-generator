import PRNG.LinearCongruentialGenerator;
import PRNG.PseudoRandomNumberGenerator;
import TestingHarness.GoodnessOfFitTest;
import TestingHarness.KSTest;
import TestingHarness.SubSequence;
import TestingHarness.UniformityTestHarness;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class KSTestUnitTest {
     private SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap = new TreeMap<>();
     private SortedMap<Integer,ArrayList<SubSequence>> subSequenceMap = new TreeMap<>();
     private SortedMap<Integer, Double> p_values = new TreeMap();

     @Test
     public void TestKSAgainstThirdPartyLib() {
          int a = 11;
          int b = 37;
          int m = 100;
          int leftBound = 0;
          int rightBound = 10;
          int seed = 1;
          int maxSubSequenceLength = 2;
          String relativePath = "./frequncyTable.csv";
          GoodnessOfFitTest goodnessOfFitTest = new KSTest();
          int numOfIteration = 100;

          PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m, leftBound,rightBound);

          //create subsequence map
          CreateSubSequenceMap(RNG,numOfIteration,maxSubSequenceLength);

          p_values = UniformityTestHarness.RunGoodnessOfFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                  subSequenceFrequencyMap);

          SortedMap<Integer,Double> expected_p_values = GenerateBaseline(rightBound,leftBound);

          //compare p_values against baseline
          for(SortedMap.Entry<Integer, Double> entry : expected_p_values.entrySet()) {
               Assertions.assertThat(entry.getValue()).isCloseTo(p_values.get(entry.getKey()), Offset.offset(0.000001));
          }
     }

     private void CreateSubSequenceMap(PseudoRandomNumberGenerator RNG,int numOfIteration,int maxSubSequenceLength) {
          List<Integer> lastElementInSequence = new ArrayList<>();
          int RNGRange = RNG.getRightBound() - RNG.getLeftBound();

          for(int i = 0; i < numOfIteration; i++) {
               //generate one random integer
               int randomInt = RNG.GetNext();
               lastElementInSequence.add(randomInt);

               //keep subSequence length within max length
               if(lastElementInSequence.size() > maxSubSequenceLength) {
                    lastElementInSequence.remove(0);
               }

               //store all new subSequence to map
               UniformityTestHarness.GenerateNewSubsequences(lastElementInSequence, randomInt,
                       subSequenceFrequencyMap, subSequenceMap, RNGRange);
          }
     }

     private SortedMap<Integer,Double> GenerateBaseline(int rightBound, int leftBound) {
          SortedMap<Integer,Double> expected_p_values = new TreeMap();
          for (SortedMap.Entry<Integer, ArrayList<SubSequence>> entry : subSequenceMap.entrySet()) {
               double[] samples = new double[entry.getValue().size()];
               int i = 0;
               for (SubSequence subSequence : entry.getValue()) {
                    samples[i] = subSequence.getIntCode();
                    i++;
               }

               double upperBound = Math.pow((rightBound-leftBound), entry.getKey());
               UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                       0, upperBound);

               //Expected Results from third part lib
               KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();
               double p_value = ksTest.kolmogorovSmirnovTest(theoreticalDist, samples);
               expected_p_values.put(entry.getKey(),p_value);
          }
          return  expected_p_values;
     }
}
