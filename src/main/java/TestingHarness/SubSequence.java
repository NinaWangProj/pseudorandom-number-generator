package TestingHarness;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SubSequence implements Comparable {
    private int length;
    private int[] elements;
    private int intCode;

    public SubSequence(int[] elements, int RNGRangeSize) {
        this.length = elements.length;
        this.elements = elements;

        for (int i = 0; i < elements.length; i++) {
            this.intCode += elements[i] * Math.pow(RNGRangeSize, elements.length - i - 1);
        }
    }

    public int getLength() {
        return length;
    }

    public int[] getElements() {
        return elements;
    }

    public int getIntCode() {
        return intCode;
    }

    public String getString() {
        String subSequenceString = Arrays.stream(elements)
                .mapToObj(i -> ((Integer) i).toString())
                .collect(Collectors.joining(", "));
        return subSequenceString;
    }

    @Override
    public boolean equals(Object o) {
        SubSequence otherSubSequence = (SubSequence) o;

        if(this.intCode == otherSubSequence.intCode) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Object o) {
        SubSequence otherSubSequence = (SubSequence)o;
        if(this.intCode == otherSubSequence.intCode) {
            return 0;
        } else if(this.intCode > otherSubSequence.intCode) {
            return 1;
        } else {
            return -1;
        }
    }
}
