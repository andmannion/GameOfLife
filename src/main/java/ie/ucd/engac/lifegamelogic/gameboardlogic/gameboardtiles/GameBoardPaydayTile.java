package ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles;

import ie.ucd.engac.lifegamelogic.gamestatehandling.GameState;
import ie.ucd.engac.lifegamelogic.gamestatehandling.PaydaySubstate;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameStates;
import java.util.ArrayList;

public class GameBoardPaydayTile extends GameBoardTile{

    public GameBoardPaydayTile(GameBoardTileTypes gameBoardTileType) {
        this.gameBoardTileType = gameBoardTileType;
        this.tileActionSequence = new ArrayList<>();
        this.tileActionSequence.add(GameStates.PaydaySubstate);

    }
}

