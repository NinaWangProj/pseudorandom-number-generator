import PRNG.LeadingDigitMapDownFactory;
import PRNG.LinearCongruentialGenerator;
import PRNG.MapDownFactory;
import PRNG.PseudoRandomNumberGenerator;
import TestingHarness.*;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GoodnessOfFitTests {
     private SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap;
     private SortedMap<Integer,ArrayList<SubSequence>> subSequenceMap = new TreeMap<>();
     private SortedMap<Integer, Double> p_values = new TreeMap();

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
          MapDownFactory mapDownFactory = new LeadingDigitMapDownFactory();

          PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m,
                  leftBound,rightBound,mapDownFactory);

          subSequenceFrequencyMap = UniformityTestHarness.Init(RNG,maxSubSequenceLength);
          //create subsequence map
          CreateSubSequenceMap(RNG,numOfIteration,maxSubSequenceLength);

          p_values = UniformityTestHarness.RunGoodnessOfFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                  subSequenceFrequencyMap);

          SortedMap<Integer,Double> expected_p_values = GenerateKSBaseline(rightBound,leftBound);

          //compare p_values against baseline
          for(SortedMap.Entry<Integer, Double> entry : expected_p_values.entrySet()) {
               Assertions.assertThat(entry.getValue()).isCloseTo(p_values.get(entry.getKey()), Offset.offset(0.000001));
          }
     }

     @Test
     public void TestChiSquareAgainstThirdPartyLib() {
          int a = 55;
          int b = 127;
          int m = 101;
          int leftBound = 0;
          int rightBound = 10;
          int seed = 1;
          int maxSubSequenceLength = 3;
          String relativePath = "./frequncyTable.csv";
          GoodnessOfFitTest goodnessOfFitTest = new TestingHarness.ChiSquareTest();
          int numOfIterations = 1000;

          MapDownFactory mapDownFactory = new LeadingDigitMapDownFactory();

          PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m,
                  leftBound,rightBound,mapDownFactory);
          subSequenceFrequencyMap = UniformityTestHarness.Init(RNG,maxSubSequenceLength);
          //create subsequence map
          CreateSubSequenceMap(RNG,numOfIterations,maxSubSequenceLength);

          p_values = UniformityTestHarness.RunGoodnessOfFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                  subSequenceFrequencyMap);

          SortedMap<Integer,Double> expected_p_values = GenerateChiSquareBaseline(rightBound,leftBound);

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
               UniformityTestHarness.GenerateNewSubsequences(lastElementInSequence,
                       subSequenceFrequencyMap, subSequenceMap, RNGRange);
          }
     }

     private SortedMap<Integer,Double> GenerateKSBaseline(int rightBound, int leftBound) {
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

     private SortedMap<Integer,Double> GenerateChiSquareBaseline(int rightBound, int leftBound) {
          SortedMap<Integer,Double> expected_p_values = new TreeMap();

          for (SortedMap.Entry<Integer, SortedMap<SubSequence,Integer>> entry : subSequenceFrequencyMap.entrySet()) {
               int numOfSamples = entry.getValue().values().stream().mapToInt(val -> val).sum();
               //Observed frequencies:
               long[] observedFrequencies = entry.getValue().values().stream()
                       .mapToLong(val ->val).toArray();

               double upperBound = Math.pow((rightBound-leftBound), entry.getKey());
               double[] expectedFrequencies = new double[(int)upperBound];
               Arrays.fill(expectedFrequencies,1/upperBound * numOfSamples);

               //Expected Results from third part lib
               ChiSquareTest chiSquareTest = new ChiSquareTest();
               double p_value = chiSquareTest.chiSquareTest(expectedFrequencies,observedFrequencies);
               expected_p_values.put(entry.getKey(),p_value);
          }
          return  expected_p_values;
     }
}


