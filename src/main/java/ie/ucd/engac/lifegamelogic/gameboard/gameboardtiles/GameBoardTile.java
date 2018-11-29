package ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	private double xLocation; //assigned in factory
	private double yLocation; //assigned in factory
	
	public GameBoardTileTypes getGameBoardTileType() {
		return gameBoardTileType;
	}

    public double getXLocation() {
        return xLocation;
    }

    public double getYLocation() {
        return yLocation;
    }
}
