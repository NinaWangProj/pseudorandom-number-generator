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
        String frequencyTableFilePath = "C:\\Users\\nwang\\IdeaProjects\\Pseudorandom_Number_Generator\\frequencyTable\\frequncyTable.csv";

        LinearCongruentialGenerator RNG = new LinearCongruentialGenerator(seed,leftBound,rightBound);
        UniformityTestHarness.RunTest(seed,leftBound,rightBound,runDepth,frequencyTableFilePath,RNG);
    }
}