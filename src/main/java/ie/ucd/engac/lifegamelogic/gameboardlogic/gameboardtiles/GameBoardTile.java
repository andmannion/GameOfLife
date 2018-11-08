package ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles;

import ie.ucd.engac.lifegamelogic.gamestatehandling.GameState;

import java.util.ArrayList;

public abstract class GameBoardTile {
	protected GameBoardTileTypes gameBoardTileType;
	public ArrayList<GameState> tileActionSequence; // TODO make this an enum
	
	public GameBoardTileTypes getGameBoardTileType() {
		return gameBoardTileType;
	}

	public ArrayList<GameState> getTileActionSequence(){
	    return tileActionSequence;
    }

}
