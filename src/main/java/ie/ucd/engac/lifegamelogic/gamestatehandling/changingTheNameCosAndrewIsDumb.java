package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.messaging.LifeGameMessage;

public class changingTheNameCosAndrewIsDumb implements GameState {

    private GameState currentSubstate = null;
    private GameBoardTile currentTile;

    changingTheNameCosAndrewIsDumb(){
        currentSubstate = null;
    }

    @Override
    public void enter(GameLogic gameLogic){
        System.out.println("test:wq");
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {

        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {

    }
}
//TODO delete this class