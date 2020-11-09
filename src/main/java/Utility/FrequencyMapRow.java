package Utility;

import com.opencsv.bean.CsvBindByName;

public class FrequencyMapRow {
    @CsvBindByName
    private int subSequenceLength;
    @CsvBindByName
    private String subSequence;
    @CsvBindByName
    private int Frequency;

    public FrequencyMapRow(int subSequenceLength, String subSequence, int frequency) {
        this.subSequenceLength = subSequenceLength;
        this.subSequence = subSequence;
        Frequency = frequency;
    }

    public int getSubSequenceLength() {
        return subSequenceLength;
    }

    public String getSubSequence() {
        return subSequence;
    }

    public int getFrequency() {
        return Frequency;
    }
}
