package TestingHarness;

import PRNG.PseudoRandomNumberGenerator;
import Utility.AnalysisUtility;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.io.FileNotFoundException;
import java.util.*;

public class UniformityTestHarness {
    public static void RunTest(PseudoRandomNumberGenerator RNG,int numOfIteration, int maxSubSequenceLength, String filePah,
                               GoodnessOfFitTest goodnessOfFitTest) throws FileNotFoundException {
        //generate rng and populate sub-sequence map
        //HashMap<SubSequenceLength, HashMap<subSequence, frequency>>
        SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap = new TreeMap<>();
        SortedMap<Integer,ArrayList<SubSequence>> subSequenceMap = new TreeMap<>();
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
            GenerateNewSubsequences(lastElementInSequence, randomInt,
                    subSequenceFrequencyMap, subSequenceMap, RNGRange);
        }

        //perform goodnessOfFitTest for each sub-sequence length
        // to see how close our random numbers follows the theoretical uniform distribution
        SortedMap<Integer,Double> p_valueMap = RunGoodnessOfFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                subSequenceFrequencyMap);

        //output stats:
        AnalysisUtility.PrintSubSequences(subSequenceMap);
        AnalysisUtility.PrintStats(subSequenceFrequencyMap, maxSubSequenceLength);
        AnalysisUtility.WriteFrequencyMapToFile(filePah, subSequenceFrequencyMap);
        AnalysisUtility.PrintPValues(p_valueMap);

    }

    public static void GenerateNewSubsequences(List<Integer> lastElementInSequence, int randomInt,
                                        SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap,
                                                SortedMap<Integer,ArrayList<SubSequence>> subSequenceMap,
                                                int RNGRangeSize) {
        int sequenceLength = lastElementInSequence.size();
        int[] baseSequence = lastElementInSequence.stream().mapToInt(i->i).toArray();

        for(int subSequenceLength = 1; subSequenceLength <= sequenceLength; subSequenceLength ++) {

            //create subSequence object
            int[] subSequenceArray = new int[subSequenceLength];
            System.arraycopy(baseSequence,sequenceLength-subSequenceLength,
                    subSequenceArray,0,subSequenceLength);
            SubSequence subSequence = new SubSequence(subSequenceArray,RNGRangeSize);

            //store subsequence to Map
            if (!subSequenceFrequencyMap.containsKey(subSequenceLength)) {
                subSequenceFrequencyMap.put(subSequenceLength, new TreeMap<>());
            }
            SortedMap<SubSequence, Integer> frequencyMap = subSequenceFrequencyMap.get(subSequenceLength);
            if (!frequencyMap.containsKey(subSequence)) {
                frequencyMap.put(subSequence, 0);
            }
            frequencyMap.put(subSequence, frequencyMap.get(subSequence) + 1);

            //for debugging purpose; print out all sub sequence
            if (!subSequenceMap.containsKey(subSequenceLength)) {
                subSequenceMap.put(subSequenceLength, new ArrayList<>());
            }
            subSequenceMap.get(subSequenceLength).add(subSequence);
        }
    }


    public static SortedMap<Integer,Double> RunGoodnessOfFitTests(GoodnessOfFitTest goodnessOfFitTest, PseudoRandomNumberGenerator RNG,
                                                                 int maxSubSequenceLength,
                                                                 SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap) {
        SortedMap<Integer,Double> p_valueMap = new TreeMap<>();
        for(int subSequenceLength = 1; subSequenceLength <= maxSubSequenceLength; subSequenceLength++) {
            SortedMap<SubSequence,Integer> sampleFrequencyMap = subSequenceFrequencyMap.get(subSequenceLength);
            double upperBound = Math.pow((RNG.getRightBound() - RNG.getLeftBound()),subSequenceLength);
            UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                    0,upperBound);

            Double p_value = goodnessOfFitTest.RunTest(sampleFrequencyMap,theoreticalDist);

            p_valueMap.put(subSequenceLength,p_value);
        }

        return p_valueMap;
    }
}
