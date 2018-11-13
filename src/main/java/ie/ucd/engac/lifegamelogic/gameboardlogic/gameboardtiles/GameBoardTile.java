package ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles;

import ie.ucd.engac.lifegamelogic.gamestatehandling.GameState;

import java.util.ArrayList;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	
	public GameBoardTileTypes getGameBoardTileType() {
		return gameBoardTileType;
	}
}
