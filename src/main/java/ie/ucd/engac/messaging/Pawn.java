package ie.ucd.engac.messaging;

import java.awt.*;

public class Pawn {

    protected int playerNumber;
    protected int numDependants;
    protected double xLocation;
    protected double yLocation;
    protected Color colour;

    public Pawn(double xLocation, double yLocation, Color colour, int playerNumber, int numDependants){
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.colour = colour;
        this.playerNumber = playerNumber;
        this.numDependants = numDependants;
    }

    public void setXLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public double getXLocation() {
        return xLocation;
    }

    public void setYLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    public double getYLocation() {
        return yLocation;
    }

    public void setNumDependants(int numDependants) {
        this.numDependants = numDependants;
    }

    public int getNumDependants() {
        return numDependants;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Color getColour() {
        return colour;
    }
}
