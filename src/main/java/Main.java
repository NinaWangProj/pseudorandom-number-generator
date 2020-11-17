import PRNG.LinearCongruentialGenerator;
import PRNG.PseudoRandomNumberGenerator;
import TestingHarness.ChiSquareTest;
import TestingHarness.GoodnessOfFitTest;
import TestingHarness.UniformityTestHarness;
import Utility.AnalysisUtility;

import java.util.HashMap;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) throws Exception {
        //inclusive left bound and exclusive right bound
        int leftBound = 0;
        int rightBound = 10;
        int seed = 1;
        int maxSubSequenceLength = 2;
        String relativePath = "./frequncyTable.csv";
        GoodnessOfFitTest goodnessOfFitTest = new ChiSquareTest();
        int numOfIteration = 100;

        HashMap<int[],SortedMap<Integer,Double>> parametersAndPvalueMap = new HashMap<>();

        for(int a = 55; a<=55; a++) {
            for(int b = 127; b<= 127; b++) {
                for(int m = 101; m<=101; m++) {
                    PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m, leftBound,rightBound);

                    parametersAndPvalueMap.put(new int[]{a,b,m},
                            UniformityTestHarness.RunTest(RNG,numOfIteration,maxSubSequenceLength,relativePath,goodnessOfFitTest));
                }
            }
        }

        AnalysisUtility.WriteOutParametersAndPValues(parametersAndPvalueMap);
/*
        int a = 11;
        int b = 37;
        int m = 100;

*/
    }
}