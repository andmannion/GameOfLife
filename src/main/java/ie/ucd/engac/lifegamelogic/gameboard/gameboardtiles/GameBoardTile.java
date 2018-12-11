package ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	private double xLocation; //assigned in factory
	private double yLocation; //assigned in factory
    private double xDimension; //assigned in factory //TODO ****
    private double yDimension; //assigned in factory //TODO ****
	
	public GameBoardTileTypes getGameBoardTileType() {
		return gameBoardTileType;
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
