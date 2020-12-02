import PRNG.*;
import TestingHarness.ChiSquareTest;
import TestingHarness.GoodnessOfFitTest;
import TestingHarness.UniformityTestHarness;
import Utility.AnalysisUtility;

import java.util.HashMap;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) throws Exception {
        //inclusive left bound and exclusive right bound
        int lowerBound = 0;
        int upperBound = 10000;
        int seed = 1;
        int maxSubSequenceLength = 1;
        String relativePath = "./frequncyTable.csv";
        GoodnessOfFitTest goodnessOfFitTest = new ChiSquareTest();
        int numOfIteration = 1600000;

        HashMap<int[],SortedMap<Integer,Double>> parametersAndPvalueMap = new HashMap<>();
        MapDownFactory mapDownFactory = new Mod10KMapDownFactory();

        for(int a = 10005; a<=10005; a++) {
            for(int b = 3; b<= 3; b++) {
                for(int m=134217728; m <= 134217728; m++) {
                    PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(seed, a, b,
                            m, lowerBound,upperBound,mapDownFactory);

                    parametersAndPvalueMap.put(new int[]{a,b,m},
                            UniformityTestHarness.RunTest(RNG,numOfIteration,maxSubSequenceLength,relativePath,goodnessOfFitTest));
                    //AnalysisUtility.PrintOriginalSequence(RNG.getOriginalSequence());
                }
            }
        }


        AnalysisUtility.WriteOutParametersAndPValues(parametersAndPvalueMap,maxSubSequenceLength);
    }
}