


public class LinearCongruentialGenerator implements PseudoRandomNumberGenerator {
    private int a;
    private int b;
    private int m;
    private int seed;
    private int leftBound;
    private int rightBound;
    private int currentRandomNumber;

    public LinearCongruentialGenerator(int seed, int leftBound, int rightBound) {
        this.a = 11;
        this.b = 37;
        this.m = rightBound-leftBound;
        this.seed = seed;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.currentRandomNumber = seed;
    }

    public int GetNext() {
        int nextRandomNumber = (a * currentRandomNumber + b) % m + leftBound;
        currentRandomNumber = nextRandomNumber;
        return nextRandomNumber;
    }
}
