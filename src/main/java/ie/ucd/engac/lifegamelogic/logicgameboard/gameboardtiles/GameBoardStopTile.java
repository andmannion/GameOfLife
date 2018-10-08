package ie.ucd.engac.lifegamelogic.logicgameboard.gameboardtiles;

public class GameBoardStopTile extends GameBoardTile {
	private GameBoardStopTileType gameBoardStopTileType; 
	
	public GameBoardStopTileType getGameBoardStopTileType() {
		return gameBoardStopTileType;
	}
	
	public GameBoardStopTile(GameBoardStopTileType gameBoardStopTileType) {
		gameBoardTileType = GameBoardTileType.Stop;
		this.gameBoardStopTileType = gameBoardStopTileType;
	}
}
