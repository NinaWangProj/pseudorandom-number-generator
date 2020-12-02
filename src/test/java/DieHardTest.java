import PRNG.LinearCongruentialGenerator;
import PRNG.Mod10KMapDownFactory;
import PRNG.PseudoRandomNumberGenerator;
import TestingHarness.DieHardMinimumDistanceSquareTest;
import TestingHarness.SubSequence;
import TestingHarness.UniformityTestHarness;
import Utility.AnalysisUtility;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.SortedMap;
import java.util.TreeMap;

public class DieHardTest {

    @Test
    public void MinimumDistanceSquareTest() throws FileNotFoundException {
        PseudoRandomNumberGenerator RNG = new LinearCongruentialGenerator(1,10001,3,134217728,
                0,10000,new Mod10KMapDownFactory());

        double p_value = DieHardMinimumDistanceSquareTest.RunTest(RNG);

        //print out frequency map
        SortedMap<Integer, SortedMap<SubSequence,Integer>> frequencyMap = UniformityTestHarness.Init(RNG, 1);

        for(int randomInt : RNG.getMappedDownSequence()) {
            SubSequence subSequence = new SubSequence(new int[]{randomInt}, 10000);
            frequencyMap.get(1).put(subSequence,frequencyMap.get(1).get(subSequence) + 1);
        }
        AnalysisUtility.WriteFrequencyMapToFile("./frequncyTable.csv", frequencyMap);
        System.out.println("p_value: " + p_value);
    }
}
