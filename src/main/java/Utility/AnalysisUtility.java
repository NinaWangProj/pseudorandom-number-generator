package Utility;

import TestingHarness.SubSequence;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AnalysisUtility {

    public static double CalculateAverage(Collection<Integer> frequencies) {
        double averageFrequency = frequencies.stream().mapToInt(val -> val).filter(val -> val>0).average().orElse(-1);

        return averageFrequency;
    }

    public static double CalculateStandardDeviation(Collection<Integer> frequencies, double average) {
        long count = frequencies.stream().mapToInt(val -> val).count();
        double deviationSqr = frequencies.stream().mapToDouble(val -> (val-average) * (val-average)).sum();
        double std = Math.sqrt(deviationSqr / count);

        return std;
    }

    public static void PrintSubSequences(SortedMap<Integer, ArrayList<SubSequence>> subSequenceMap) {
        for(SortedMap.Entry<Integer, ArrayList<SubSequence>> entry : subSequenceMap.entrySet()) {
            double[] samples = new double[100];
            int i = 0;
            for(SubSequence subSequence : entry.getValue()) {
                System.out.println(subSequence.getString());
                    samples[i] = subSequence.getIntCode();
                    i++;
            }
        }
    }

    public static void PrintStats(SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap, int runDepth) {
        for(int k = 1; k <= runDepth; k++) {
            Collection<Integer> frequencies = subSequenceFrequencyMap.get(k).values();
            double mean = AnalysisUtility.CalculateAverage(frequencies);
            double std = AnalysisUtility.CalculateStandardDeviation(frequencies, mean);
            System.out.println("frequency mean for run depth " + k + " is: " + mean);
            System.out.println("frequency std for run depth " + k + " is: " + std);
        }
    }

    public static void WriteFrequencyMapToFile(String filePath,SortedMap<Integer, SortedMap<SubSequence,Integer>> subSequenceFrequencyMap) throws FileNotFoundException {
        FileOutputStream stream = new FileOutputStream(filePath);
        OutputStreamWriter writer = new OutputStreamWriter(stream);

        List<FrequencyMapRow> frequencyTable = new ArrayList<>();

        for(SortedMap.Entry<Integer, SortedMap<SubSequence,Integer>> entry : subSequenceFrequencyMap.entrySet()) {
            int subSequenceLength = entry.getKey();

            for(Map.Entry<SubSequence,Integer> frequencyEntry : entry.getValue().entrySet()) {
                FrequencyMapRow frequencyMapRow = new FrequencyMapRow(subSequenceLength,
                        frequencyEntry.getKey().getString(),frequencyEntry.getValue());
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

    public static void PrintPValues(SortedMap<Integer,Double> pValues) {
        for(Map.Entry<Integer, Double> entry : pValues.entrySet()) {
            System.out.println("for sub-sequence length " + entry.getKey() + ", the p-value is: " + entry.getValue());
        }
    }

    public static void WriteOutParametersAndPValues(HashMap<int[],SortedMap<Integer,Double>> parametersAndPvalueMap)
            throws IOException {
        FileWriter writer = new FileWriter("./abmPvalues.csv");
        String header = String.join(",","a","b","m","p_level1","p_level2");
        writer.write(header);
        writer.append("\n");

        for(Map.Entry<int[],SortedMap<Integer,Double>> entry : parametersAndPvalueMap.entrySet()) {
            String parameters = Arrays.stream(entry.getKey()).mapToObj(val -> String.valueOf(val)).
                    collect(Collectors.joining(", "));
            String outputRow = parameters;
            for(Map.Entry<Integer,Double> pValueEntry : entry.getValue().entrySet()) {
                double p_value = pValueEntry.getValue();
                outputRow = String.join(",",outputRow,String.valueOf(p_value));
            }
            writer.write(outputRow);
            writer.append("\n");
        }
        writer.close();
    }
}
