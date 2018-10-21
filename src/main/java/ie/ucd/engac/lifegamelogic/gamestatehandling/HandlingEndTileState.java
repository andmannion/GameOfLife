package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.messaging.LifeGameMessage;

public class HandlingEndTileState implements GameState {

    private GameState currentSubstate = null;
    private GameBoardTile currentTile;

    HandlingEndTileState(){
        currentSubstate = null;
    }

    @Override
    public void enter(GameLogic gameLogic){
        if (currentSubstate != null) { //
            currentSubstate.enter(gameLogic);
        }
        else{
            //currentTile = gameLogic.getCurrentTile(); //ask Andrew how to get this
            //currentSubstate = currentTile.getInitialSubstate(); //get this somehow
        }
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextSubstate = currentSubstate.handleInput(gameLogic,lifeGameMessage);
        // Three options from a substate handleInput required:
        // - No change in state
        // - Change to the substate
        // - Change required in the superstate

        if(nextSubstate != null) {
            currentSubstate = nextSubstate;
            currentSubstate.enter(gameLogic);
        }
        // TODO what is my next state?
        // TODO vvvvvvvvvvvvvv
        // What we should be doing here: returning the next state to the highest level,
        // then recursively looking for where this state is located (i.e. invoking
        // the enter() method of each subsequent substate until we enter the correct
        // destination state).
        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {

    }
}
//TODO delete this class