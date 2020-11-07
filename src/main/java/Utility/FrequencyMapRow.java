package Utility;

import com.opencsv.bean.CsvBindByName;

public class FrequencyMapRow {
    @CsvBindByName
    private int RunDepth;
    @CsvBindByName
    private String RunNumber;
    @CsvBindByName
    private int Frequency;

    public FrequencyMapRow(int runDepth, String runNumber, int frequency) {
        RunDepth = runDepth;
        RunNumber = runNumber;
        Frequency = frequency;
    }

    public int getRunDepth() {
        return RunDepth;
    }

    public String getRunNumber() {
        return RunNumber;
    }

    public int getFrequency() {
        return Frequency;
    }
}
