package PRNG;

public class LeadingDigitMapDownFactory implements MapDownFactory {
    public int MapDown(int originalInt) {
        int MappedDownInt = 0;
        if(originalInt%10 == 0) {
            MappedDownInt = 0;
        } else {
            while (originalInt >= 10)
                originalInt /=10;
                MappedDownInt = originalInt;
        }
        return MappedDownInt;
    }
}
