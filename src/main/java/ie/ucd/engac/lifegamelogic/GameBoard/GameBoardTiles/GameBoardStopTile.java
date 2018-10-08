package ie.ucd.engac.lifegamelogic.GameBoard.GameBoardTiles;

public class GameBoardStopTile extends GameBoardTile {
	private GameBoardStopTileType gameBoardStopTileType; 
	
	public GameBoardStopTileType getGameBoardStopTileType() {
		return gameBoardStopTileType;
	}
	
	public GameBoardStopTile(GameBoardStopTileType gameBoardStopTileType) {
		gameBoardTileType = GameBoardTileTypes.Stop;
		this.gameBoardStopTileType = gameBoardStopTileType;
	}
}
