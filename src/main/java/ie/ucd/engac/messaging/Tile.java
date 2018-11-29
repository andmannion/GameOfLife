package ie.ucd.engac.messaging;

public class Tile {
    private String type;
    private double xLocation;
    private double yLocation;

    public Tile(String type,double xLocation,double yLocation){
        this.type = type;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public String getType() {
        return type;
    }

    public double getXLocation() {
        return xLocation;
    }

    public double getYLocation() {
        return yLocation;
    }
}
