import java.util.Collection;

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
}
