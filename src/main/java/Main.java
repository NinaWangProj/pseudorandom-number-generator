import PRNG.LinearCongruentialGenerator;
import TestingHarness.GoodnessOfFitTest;
import TestingHarness.KSTest;
import TestingHarness.UniformityTestHarness;

public class Main {
    public static void main(String[] args) throws Exception {
        //inclusive left bound and exclusive right bound
        int a = 11;
        int b = 37;
        int m = 100;
        int leftBound = 0;
        int rightBound = 10;
        int seed = 1;
        int maxSubSequenceLength = 2;
        String relativePath = "./frequncyTable.csv";
        GoodnessOfFitTest goodnessOfFitTest = new KSTest();
        int numOfIteration = m;

        LinearCongruentialGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m, leftBound,rightBound);

        UniformityTestHarness.RunTest(RNG,m,maxSubSequenceLength,relativePath,goodnessOfFitTest);
    }
}