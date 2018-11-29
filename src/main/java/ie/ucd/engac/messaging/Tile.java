package ie.ucd.engac.messaging;

import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;

public class Tile {
    private String type;
    private double xLocation;
    private double yLocation;

    public Tile(String type,double xLocation,double yLocation){
        this.type = type;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public Tile(GameBoardTile gameBoardTile){
        this.type = gameBoardTile.getGameBoardTileType().toString();
        this.xLocation = gameBoardTile.getXLocation();
        this.yLocation = gameBoardTile.getYLocation();
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
