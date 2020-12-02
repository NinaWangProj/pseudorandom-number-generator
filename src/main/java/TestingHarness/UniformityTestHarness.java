package TestingHarness;

import PRNG.PseudoRandomNumberGenerator;
import Utility.AnalysisUtility;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.io.FileNotFoundException;
import java.util.*;

public class UniformityTestHarness {
    public static SortedMap<Integer,Double> RunTest(PseudoRandomNumberGenerator RNG,int numOfIteration, int maxSubSequenceLength, String filePah,
                               GoodnessOfFitTest goodnessOfFitTest) throws FileNotFoundException {
        //generate rng and populate sub-sequence map
        //HashMap<SubSequenceLength, HashMap<subSequence, frequency>>
        SortedMap<Integer,ArrayList<SubSequence>> subSequenceMap = new TreeMap<>();
        List<Integer> lastElementInSequence = new ArrayList<>();
        int RNGRange = RNG.getRightBound() - RNG.getLeftBound();
        SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap = Init(RNG,maxSubSequenceLength);

        for(int i = 0; i < numOfIteration; i++) {
            //generate one random integer
            int randomInt = RNG.GetNext();
            lastElementInSequence.add(randomInt);

            //keep subSequence length within max length
            if(lastElementInSequence.size() > maxSubSequenceLength) {
                lastElementInSequence.remove(0);
            }

            //store all new subSequence to map
            GenerateNewSubsequences(lastElementInSequence,
                    subSequenceFrequencyMap, subSequenceMap, RNGRange);
        }

        //perform goodnessOfFitTest for each sub-sequence length
        // to see how close our random numbers follows the theoretical uniform distribution
        SortedMap<Integer,Double> p_valueMap = RunGoodnessOfFitTests(goodnessOfFitTest,RNG,maxSubSequenceLength,
                subSequenceFrequencyMap);

        //output stats:
        //AnalysisUtility.PrintSubSequences(subSequenceMap);
        //AnalysisUtility.PrintStats(subSequenceFrequencyMap, maxSubSequenceLength);
        AnalysisUtility.WriteFrequencyMapToFile(filePah, subSequenceFrequencyMap);
        AnalysisUtility.PrintPValues(p_valueMap);

        return p_valueMap;

    }

    public static void GenerateNewSubsequences(List<Integer> lastElementInSequence,
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
            SortedMap<SubSequence, Integer> frequencyMap = subSequenceFrequencyMap.get(subSequenceLength);
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
            double lowerBound = RNG.getLeftBound();
            double upperBound = Math.pow((RNG.getRightBound() - RNG.getLeftBound()),subSequenceLength) + lowerBound;
            UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                    lowerBound,upperBound);

            Double p_value = goodnessOfFitTest.RunTest(sampleFrequencyMap,theoreticalDist);

            p_valueMap.put(subSequenceLength,p_value);
        }

        return p_valueMap;
    }

    public static SortedMap<Integer, SortedMap<SubSequence,Integer>>  Init(
                            PseudoRandomNumberGenerator RNG,int maxSubSequenceLength) {
        SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap = new TreeMap<>();
        int RNGRangeSize = RNG.getRightBound() - RNG.getLeftBound();

        for(int i = 1; i <= maxSubSequenceLength; i++) {
            subSequenceFrequencyMap.put(i, new TreeMap<>());
            SubSequence subSequence;
            if(i==1) {
                for(int j = RNG.getLeftBound(); j < RNG.getRightBound(); j++) {
                    subSequence = new SubSequence(new int[]{j}, RNGRangeSize);
                    subSequenceFrequencyMap.get(i).put(subSequence, 0);
                }
            } else {
                SortedMap<SubSequence,Integer> subSequencesInPreviousLevel = subSequenceFrequencyMap.get(i-1);
                for(Map.Entry<SubSequence,Integer> entry :subSequencesInPreviousLevel.entrySet()) {
                    for(int k = RNG.getLeftBound(); k < RNG.getRightBound(); k++) {
                        int[] subSequenceArray = new int[i];
                        System.arraycopy(entry.getKey().getElements(),0,subSequenceArray,0,i-1);
                        subSequenceArray[i-1] = k;
                        subSequence = new SubSequence(subSequenceArray, RNGRangeSize);
                        subSequenceFrequencyMap.get(i).put(subSequence, 0);
                    }
                }
            }
        }
        return subSequenceFrequencyMap;
    }
}
