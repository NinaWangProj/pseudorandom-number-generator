import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UniformityTestHarness {
    public static void RunTest(int seed, int leftBound, int rightBound, int runDepth, String filePah, PseudoRandomNumberGenerator RNG
    ) throws FileNotFoundException {
        int numOfRandomNumbers = (int)Math.pow(rightBound - leftBound, runDepth) + runDepth -1;

        int[] randomInts = new int[numOfRandomNumbers];
        //HashMap<runDepth, HashMap<randomInt, frequency>>
        HashMap<Integer, HashMap<String,Integer>> randomIntFrequencyMap = new HashMap<>();
        HashMap<Integer,ArrayList<String>> runNumbersMap = new HashMap<>();
        int sourceCopyIndex = 0;

        for(int i = 0; i < numOfRandomNumbers; i++) {
            int randomInt = RNG.GetNext();
            randomInts[i] = randomInt;

            int adjustedRunDepth = runDepth;
            if( i < runDepth) {
                adjustedRunDepth = i + 1;
            }

            for(int j = adjustedRunDepth; j >= 1; j--) {
                int[] copiedRandomInts = new int[j];
                System.arraycopy(randomInts,sourceCopyIndex+adjustedRunDepth-j,copiedRandomInts,0, j);
                String runNumber = IntStream.of(copiedRandomInts)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(","));

                if(!randomIntFrequencyMap.containsKey(j)) {
                    randomIntFrequencyMap.put(j,new HashMap<>());
                }

                HashMap<String,Integer> frequencyMapForGivenDepth = randomIntFrequencyMap.get(j);

                if(!frequencyMapForGivenDepth.containsKey(runNumber)) {
                    frequencyMapForGivenDepth.put(runNumber, 0);
                }

                frequencyMapForGivenDepth.put(runNumber, frequencyMapForGivenDepth.get(runNumber) + 1);

                //for printing purpose
                if(!runNumbersMap.containsKey(j)) {
                    runNumbersMap.put(j,new ArrayList<>());
                }
                if(!runNumbersMap.get(j).contains(runNumber)) {
                    runNumbersMap.get(j).add(runNumber);
                }
            }

            if(i >= runDepth -1)
                sourceCopyIndex += 1;
        }
        AnalysisUtility.PrintStats(randomIntFrequencyMap, runDepth);
        AnalysisUtility.WriteFrequencyMapToFile(filePah, randomIntFrequencyMap);
    }
}
