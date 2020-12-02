package PRNG;


import java.util.ArrayList;
import java.util.List;

public class LinearCongruentialGenerator implements PseudoRandomNumberGenerator {
    private int a;
    private int b;
    private int m;
    private int seed;
    private int leftBound;
    private int rightBound;
    private int currentRandomNumber;
    private List<Integer> originalSequence;
    private List<Integer> mappedDownSequence;
    private MapDownFactory mapDownFactory;

    public LinearCongruentialGenerator(int seed, int a, int b, int m, int leftBound, int rightBound,
                                       MapDownFactory mapDownFactory) {
        this.a = a;
        this.b = b;
        this.m = m;
        this.seed = seed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.currentRandomNumber = seed;
        this.originalSequence = new ArrayList<>();
        this.mappedDownSequence = new ArrayList<>();
        this.mapDownFactory = mapDownFactory;
    }

    public int GetNext() {
        long temp = (long)a * (long)currentRandomNumber + b;
        int nextRandomNumber =  (int) (temp % m);
        originalSequence.add(nextRandomNumber);
        currentRandomNumber = nextRandomNumber;

        int normalizedRandomNumber = mapDownFactory.MapDown(nextRandomNumber);
        mappedDownSequence.add(normalizedRandomNumber);

        return normalizedRandomNumber;
    }

    public int getSeed() {
        return seed;
    }

    public int getLeftBound() {
        return leftBound;
    }

    public int getRightBound() {
        return rightBound;
    }

    public List<Integer> getOriginalSequence() {
        return originalSequence;
    }

    public List<Integer> getMappedDownSequence() {
        return mappedDownSequence;
    }
}
