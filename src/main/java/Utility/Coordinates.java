package Utility;

public class Coordinates {
    private int xCoordinate;
    private int yCoordinate;

    public Coordinates(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        Coordinates coordinates = (Coordinates) o;

        if(this.xCoordinate == coordinates.xCoordinate & this.yCoordinate == coordinates.yCoordinate) {
            return true;
        } else {
            return false;
        }
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
