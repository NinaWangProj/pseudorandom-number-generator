import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        //inclusive left bound and exclusive right bound
        int leftBound = 0;
        int rightBound = 3;
        int seed = 1;
        int runDepth = 2;

        UniformityTestHarness.RunTest(seed,leftBound,rightBound,runDepth);
    }
}