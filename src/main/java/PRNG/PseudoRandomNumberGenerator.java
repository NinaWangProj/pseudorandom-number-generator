package PRNG;


import java.util.List;

public interface PseudoRandomNumberGenerator {

    public int GetNext();

    public int getRightBound();

    public int getLeftBound();

    public int getSeed();

    public List<Integer> getOriginalSequence();

    public List<Integer> getMappedDownSequence();
}
