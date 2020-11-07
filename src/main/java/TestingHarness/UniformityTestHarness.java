package TestingHarness;

import PRNG.PseudoRandomNumberGenerator;
import Utility.AnalysisUtility;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UniformityTestHarness {
    public static void RunTest(PseudoRandomNumberGenerator RNG,int numOfIteration, int maxSubSequenceLength, String filePah,
                               GoodnessOfFitTest goodnessOfFitTest) throws FileNotFoundException {
        //generate rng and populate sub-sequence map
        //HashMap<SubSequenceLength, HashMap<subSequence, frequency>>
        HashMap<Integer, HashMap<String,Integer>> subSequenceFrequencyMap = new HashMap<>();
        HashMap<Integer,ArrayList<String>> subSequenceMap = new HashMap<>();
        List<Integer> previousMaxLengthSubSequence = new ArrayList<>();

        for(int i = 0; i < numOfIteration; i++) {
            //generate one random integer
            int randomInt = RNG.GetNext();

            //store all new subSequence to map
            GenerateNewSubsequences(previousMaxLengthSubSequence, randomInt, maxSubSequenceLength,
                    subSequenceFrequencyMap, subSequenceMap);
        }

        //perform goodnessOfFitTest for each sub-sequence length
        // to see how close our random numbers follows the theoretical uniform distribution
        HashMap<Integer,Double> p_valueMap = RunGoodnessToFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                subSequenceFrequencyMap);

        //output stats:
        AnalysisUtility.PrintRunNumbers(subSequenceFrequencyMap,subSequenceMap,maxSubSequenceLength);
        AnalysisUtility.PrintStats(subSequenceFrequencyMap, maxSubSequenceLength);
        AnalysisUtility.WriteFrequencyMapToFile(filePah, subSequenceFrequencyMap);
    }

    private static void GenerateNewSubsequences(List<Integer> previousMaxLengthSubSequence, int randomInt,
                                        int maxSubSequenceLength,HashMap<Integer, HashMap<SubSequence,Integer>> subSequenceFrequencyMap,
                                               HashMap<Integer,ArrayList<SubSequence>> subSequenceMap) {
        previousMaxLengthSubSequence.add(randomInt);
        List<Integer> currentMaxLengthSubSequence = previousMaxLengthSubSequence;

        //keep subSequence length within max length
        if(currentMaxLengthSubSequence.size() > maxSubSequenceLength) {
            currentMaxLengthSubSequence.remove(0);
        }

        for(int subSequenceLength = 1; subSequenceLength <= maxSubSequenceLength; subSequenceLength ++) {
            String subSequence = currentMaxLengthSubSequence.subList(0,subSequenceLength-1).stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            if (!subSequenceFrequencyMap.containsKey(subSequenceLength)) {
                subSequenceFrequencyMap.put(subSequenceLength, new HashMap<>());
            }

            HashMap<String, Integer> frequencyMapForGivenDepth = subSequenceFrequencyMap.get(subSequenceLength);

            if (!frequencyMapForGivenDepth.containsKey(subSequence)) {
                frequencyMapForGivenDepth.put(subSequence, 0);
            }
            frequencyMapForGivenDepth.put(subSequence, frequencyMapForGivenDepth.get(subSequence) + 1);

            //for debugging purpose; print out all sub sequence
            if (!subSequenceMap.containsKey(subSequenceLength)) {
                subSequenceMap.put(subSequenceLength, new ArrayList<>());
            }
            subSequenceMap.get(subSequenceLength).add(subSequence);
        }
    }


    private static HashMap<Integer,Double> RunGoodnessToFitTests(GoodnessOfFitTest goodnessOfFitTest, PseudoRandomNumberGenerator RNG,
                                                                 int maxSubSequenceLength,
                                                                 HashMap<Integer, HashMap<String,Integer>> subSequenceFrequencyMap) {
        HashMap<Integer,Double> p_valueMap = new HashMap<>();
        for(int subSequenceLength = 1; subSequenceLength < maxSubSequenceLength; subSequenceLength++) {
            HashMap<String,Integer> sampleFrequencyMap = subSequenceFrequencyMap.get(subSequenceLength);
            double upperBound = Math.pow((RNG.getRightBound() - RNG.getLeftBound()),subSequenceLength);
            UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                    0,upperBound);

            Double p_value = goodnessOfFitTest.RunTest(sampleFrequencyMap,theoreticalDist);

            p_valueMap.put(subSequenceLength,p_value);
        }

        return p_valueMap;
    }
}
