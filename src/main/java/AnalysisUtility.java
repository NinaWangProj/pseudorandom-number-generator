import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static void PrintRunNumbers(HashMap<Integer, HashMap<int[],Integer>> randomIntFrequencyMap,
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

    public static void PrintStats(HashMap<Integer, HashMap<String,Integer>> randomIntFrequencyMap, int runDepth) {
        for(int k = 1; k <= runDepth; k++) {
            Collection<Integer> frequencies = randomIntFrequencyMap.get(k).values();
            double mean = AnalysisUtility.CalculateAverage(frequencies);
            double std = AnalysisUtility.CalculateStandardDeviation(frequencies, mean);
            System.out.println("frequency mean for run depth " + k + " is: " + mean);
            System.out.println("frequency std for run depth " + k + " is: " + std);
        }
    }

    public static void WriteFrequencyMapToFile(String filePath,HashMap<Integer, HashMap<String,Integer>> randomIntFrequencyMap) throws FileNotFoundException {
        FileOutputStream stream = new FileOutputStream(filePath);
        OutputStreamWriter writer = new OutputStreamWriter(stream);

        List<FrequencyMapRow> frequencyTable = new ArrayList<>();

        for(Map.Entry<Integer, HashMap<String,Integer>> entry : randomIntFrequencyMap.entrySet()) {
            int runDepth = entry.getKey();

            for(Map.Entry<String,Integer> runNumEntry : entry.getValue().entrySet()) {
                FrequencyMapRow frequencyMapRow = new FrequencyMapRow(runDepth,runNumEntry.getKey(),runNumEntry.getValue());
                frequencyTable.add(frequencyMapRow);
            }
        }

        StatefulBeanToCsv frequencyTableToCSV = new StatefulBeanToCsvBuilder(writer).build();

        try {
            frequencyTableToCSV.write(frequencyTable);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
