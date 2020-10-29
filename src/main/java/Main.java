import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        int numOfIterations = 200;
        //inclusive left bound and exclusive right bound
        int leftBound = 0;
        int rightBound = 100;
        int seed = 1;
        RandomNumberGenerator RNG = new RandomNumberGenerator(seed,leftBound,rightBound);
        ArrayList<Integer> randomInts = new ArrayList<>();
        HashMap<Integer,Integer> randomIntFrequencyMap = new HashMap<>();

        for(int i = 0; i < numOfIterations; i++) {
            int randomInt = RNG.GetNext();
            System.out.println(randomInt);
            randomInts.add(randomInt);
            if(!randomIntFrequencyMap.containsKey(randomInt)) {
                randomIntFrequencyMap.put(randomInt, 1);
            } else {
                randomIntFrequencyMap.put(randomInt, randomIntFrequencyMap.get(randomInt) + 1);
            }
        }

        Collection<Integer> frequencies = randomIntFrequencyMap.values();
        double mean = AnalysisUtility.CalculateAverage(frequencies);
        double std = AnalysisUtility.CalculateStandardDeviation(frequencies, mean);

        System.out.println("mean: " + mean);
        System.out.println("std: " + std);
    }
}