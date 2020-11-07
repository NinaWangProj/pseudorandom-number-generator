package TestingHarness;

public class SubSequence {
    private int length;
    private int[] elements;
    private int intRepOfSubSequence;

    public SubSequence(int[] elements, int RNGRangeSize) {
        this.length = elements.length;
        this.elements = elements;

        for(int i = 0; i < elements.length; i ++) {
            this.intRepOfSubSequence += elements[i] * Math.pow(RNGRangeSize, elements.length - i -1);
        }
    }

    public int getLength() {
        return length;
    }

    public int[] getElements() {
        return elements;
    }

    public int getIntRepOfSubSequence() {
        return intRepOfSubSequence;
    }
}
