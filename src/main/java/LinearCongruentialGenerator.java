


public class LinearCongruentialGenerator implements PseudoRandomNumberGenerator {
    private int a;
    private int b;
    private int m;
    private int seed;
    private int leftBound;
    private int rightBound;
    private int currentRandomNumber;

    public LinearCongruentialGenerator(int seed, int a, int b, int m, int leftBound, int rightBound) {
        this.a = a;
        this.b = b;
        this.m = m;
        this.seed = seed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.currentRandomNumber = seed;
    }

    public int GetNext() {
        int nextRandomNumber = (a * currentRandomNumber + b) % m;
        currentRandomNumber = nextRandomNumber;
        int test =  ((nextRandomNumber - nextRandomNumber % (rightBound - leftBound)) /
                (rightBound - leftBound)) % (rightBound - leftBound) + leftBound;
        return test;
    }
}
