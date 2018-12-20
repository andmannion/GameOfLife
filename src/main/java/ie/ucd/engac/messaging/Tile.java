package ie.ucd.engac.messaging;

import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTileTypes;

public class Tile {
    private String type;
    private String stopType;
    private double xLocation;
    private double yLocation;
    private double xDimension;
    private double yDimension;

    public Tile(String type,String stopType,double xLocation,double yLocation,double xDimension, double yDimension){
        this.type = type;
        this.stopType = stopType;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.xDimension = xDimension;
        this.yDimension = yDimension;
    }

    public Tile(GameBoardTile gameBoardTile){
        this.type = gameBoardTile.getGameBoardTileType().toString();
        if (gameBoardTile.getGameBoardTileType() == GameBoardTileTypes.Stop) {
            this.stopType = ((GameBoardStopTile)gameBoardTile).getGameBoardStopTileType().toString();
        }
        else{
            stopType = "";
        }
        this.xLocation = gameBoardTile.getXLocation();
        this.yLocation = gameBoardTile.getYLocation();
        this.xDimension = gameBoardTile.getXDimension();
        this.yDimension = gameBoardTile.getYDimension();
    }

    public String getType() {
        return type;
    }

    public String getTypeString(){
        if (type.equals("Stop")){
            return stopType;
        }
        else{
            return type;
        }
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
