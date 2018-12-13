/*
	Class used to represent the different parameters associated
	with drawing a tile on the GUI panel.
*/

public class TileInfo {
    private double xLocation;
    private double yLocation;
    private double xDimension;
    private double yDimension;

    public TileInfo(double xLocation, double yLocation, double xDimension, double yDimension){
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.xDimension = xDimension;
        this.yDimension = yDimension;
    }

    public double getxLocation(){
        return  xLocation;
    }

    public double getyLocation() {
        return yLocation;
    }

    public double getxDimension() {
        return xDimension;
    }

    public double getyDimension() {
        return yDimension;
    }
}
