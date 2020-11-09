package Utility;

import TestingHarness.SubSequence;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

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

    public static void PrintSubSequences(SortedMap<Integer, ArrayList<SubSequence>> subSequenceMap) {
        for(SortedMap.Entry<Integer, ArrayList<SubSequence>> entry : subSequenceMap.entrySet()) {
            double[] samples = new double[100];
            int i = 0;
            for(SubSequence subSequence : entry.getValue()) {
                System.out.println(subSequence.getString());
                    samples[i] = subSequence.getIntCode();
                    i++;
            }
            //delete later:
            //testing using library:
            double upperBound = Math.pow((10),entry.getKey());
            UniformRealDistribution theoreticalDist = new UniformRealDistribution(
                    0,upperBound);
            KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();

            double p_value = ksTest.kolmogorovSmirnovTest(theoreticalDist,samples,true);
            double test = 0;
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
}
