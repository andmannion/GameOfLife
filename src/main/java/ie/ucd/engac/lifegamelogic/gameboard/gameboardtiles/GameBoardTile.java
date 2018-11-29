package ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	private double xLocation;
	private double yLocation;
	
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
