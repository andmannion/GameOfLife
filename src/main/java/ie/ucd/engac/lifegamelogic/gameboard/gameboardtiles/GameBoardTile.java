package ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	private int xLocation;
	private int yLocation;
	
	public GameBoardTileTypes getGameBoardTileType() {
		return gameBoardTileType;
	}

    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
    }
}
