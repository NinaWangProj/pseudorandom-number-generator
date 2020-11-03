import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnalysisUtility {

    public static double CalculateAverage(Collection<Integer> frequencies) {
        double averageFrequency = frequencies.stream().mapToInt(val -> val).average().orElse(-1);

        return averageFrequency;
    }

    public static double CalculateStandardDeviation(Collection<Integer> frequencies, double average) {
        long count = frequencies.stream().mapToInt(val -> val).count();
        double deviationSqr = frequencies.stream().mapToDouble(val -> (val-average) * (val-average)).sum();
        double std = Math.sqrt(deviationSqr / count);

        return std;
    }

    public static void PrintStats(HashMap<Integer, HashMap<int[],Integer>> randomIntFrequencyMap,
                                  HashMap<Integer, ArrayList<int[]>> runNumbersMap, int runDepth) {
        for(int k = 1; k <= runDepth; k++) {

            for (int[] runNumber : runNumbersMap.get(k)) {
                String runNumberString = "";
                for(int i = 0; i < runNumber.length; i++) {
                    runNumberString = runNumberString + runNumber[i] + ", ";
                }
                System.out.println(runNumberString);
            }
            Collection<Integer> frequencies = randomIntFrequencyMap.get(k).values();
            double mean = AnalysisUtility.CalculateAverage(frequencies);
            double std = AnalysisUtility.CalculateStandardDeviation(frequencies, mean);
            System.out.println("frequency mean for run depth " + k + " is: " + mean);
            System.out.println("frequency std for run depth " + k + " is: " + std);
        }
    }
}
