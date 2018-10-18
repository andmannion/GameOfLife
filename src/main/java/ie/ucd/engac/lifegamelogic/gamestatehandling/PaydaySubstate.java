package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.*;

public class PaydaySubstate implements GameState{
    public PaydaySubstate(){}

    @Override
    public void enter(GameLogic gameLogic) {
        gameLogic.getCurrentPlayer().addToBalance(1); //TODO get this value from cfg
        exit(gameLogic);
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // Must clear the sent message?
    }
}
