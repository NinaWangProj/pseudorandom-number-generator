import java.util.ArrayList;
import java.util.HashMap;

public class UniformityTestHarness {
    public static void RunTest(int seed, int leftBound, int rightBound, int runDepth) {
        int numOfRandomNumbers = (int)Math.pow(rightBound - leftBound, runDepth) + runDepth -1;

        LinearCongruentialGenerator RNG = new LinearCongruentialGenerator(seed,leftBound,rightBound);
        int[] randomInts = new int[numOfRandomNumbers];
        //HashMap<runDepth, HashMap<randomInt, frequency>>
        HashMap<Integer, HashMap<int[],Integer>> randomIntFrequencyMap = new HashMap<>();
        HashMap<Integer,ArrayList<int[]>> runNumbersMap = new HashMap<>();
        int sourceCopyIndex = 0;

        for(int i = 0; i < numOfRandomNumbers; i++) {
            int randomInt = RNG.GetNext();
            randomInts[i] = randomInt;
            System.out.println(randomInt);

            int adjustedRunDepth = runDepth;
            if( i < runDepth) {
                adjustedRunDepth = i + 1;
            }

            for(int j = adjustedRunDepth; j >= 1; j--) {
                int[] copiedRandomInts = new int[j];
                System.arraycopy(randomInts,sourceCopyIndex+adjustedRunDepth-j,copiedRandomInts,0, j);

                if(!randomIntFrequencyMap.containsKey(j)) {
                    randomIntFrequencyMap.put(j,new HashMap<>());
                }

                HashMap<int[],Integer> frequencyMapForGivenDepth = randomIntFrequencyMap.get(j);

                if(!frequencyMapForGivenDepth.containsKey(copiedRandomInts)) {
                    frequencyMapForGivenDepth.put(copiedRandomInts, 0);
                }

                frequencyMapForGivenDepth.put(copiedRandomInts, frequencyMapForGivenDepth.get(copiedRandomInts) + 1);

                //for printing purpose
                if(!runNumbersMap.containsKey(j)) {
                    runNumbersMap.put(j,new ArrayList<>());
                }
                if(!runNumbersMap.get(j).contains(copiedRandomInts)) {
                    runNumbersMap.get(j).add(copiedRandomInts);
                }
            }

            if(i >= runDepth -1)
                sourceCopyIndex += 1;
        }
        AnalysisUtility.PrintStats(randomIntFrequencyMap,runNumbersMap, runDepth);

    }
}
