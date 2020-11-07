package PRNG;


public interface PseudoRandomNumberGenerator {

    public int GetNext();

    public int getRightBound();

    public int getLeftBound();

    public int getSeed();

}
