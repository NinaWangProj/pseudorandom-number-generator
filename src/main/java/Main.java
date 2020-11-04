import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        //inclusive left bound and exclusive right bound
        int a = 11;
        int b = 37;
        int m = 100;
        int leftBound = 0;
        int rightBound = 10;
        int seed = 1;
        int runDepth = 2;
        String relativePath = "./frequncyTable.csv";

        LinearCongruentialGenerator RNG = new LinearCongruentialGenerator(seed, a, b, m, leftBound,rightBound);

        UniformityTestHarness.RunTest(seed,m,leftBound,rightBound,runDepth,relativePath,RNG);
    }
}