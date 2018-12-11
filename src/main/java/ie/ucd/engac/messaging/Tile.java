package ie.ucd.engac.messaging;

import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;

public class Tile {
    private String type;
    private double xLocation;
    private double yLocation;
    private double xDimension;
    private double yDimension;

    public Tile(String type,double xLocation,double yLocation,double xDimension, double yDimension){
        this.type = type;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.xDimension = xDimension;
        this.yDimension = yDimension;
    }

    public Tile(GameBoardTile gameBoardTile){
        this.type = gameBoardTile.getGameBoardTileType().toString();
        this.xLocation = gameBoardTile.getXLocation();
        this.yLocation = gameBoardTile.getYLocation();
        System.err.println(gameBoardTile.getXDimension());
        this.xDimension = gameBoardTile.getXDimension();
        this.yDimension = gameBoardTile.getYDimension();
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

    public double getXDimension() {
        return xDimension;
    }

    public double getYDimension() {
        return yDimension;
    }
}
