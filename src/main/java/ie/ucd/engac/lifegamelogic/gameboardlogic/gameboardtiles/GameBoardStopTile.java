package ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles;

public class GameBoardStopTile extends GameBoardTile {
	private GameBoardStopTileTypes gameBoardStopTileType; 
	
	public GameBoardStopTileTypes getGameBoardStopTileType() {
		return gameBoardStopTileType;
	}
	
	public GameBoardStopTile(GameBoardStopTileTypes gameBoardStopTileType) {
		gameBoardTileType = GameBoardTileTypes.Stop;
		this.gameBoardStopTileType = gameBoardStopTileType;
	}
}
