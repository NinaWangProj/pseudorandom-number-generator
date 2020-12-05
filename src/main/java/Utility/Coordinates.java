package Utility;

public class Coordinates {
    private double xCoordinate;
    private double yCoordinate;

    public Coordinates(double xCoordinate, double yCoordinate) {
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

    public double getxCoordinate() {
        return xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }
}
