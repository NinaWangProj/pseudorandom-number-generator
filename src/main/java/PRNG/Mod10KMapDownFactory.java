package PRNG;

public class Mod10KMapDownFactory implements MapDownFactory{
    public int MapDown(int originalInt) {
        int mappedDownInt = originalInt %10000;
        return mappedDownInt;
    }
}
