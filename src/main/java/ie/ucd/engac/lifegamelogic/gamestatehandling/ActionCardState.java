package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.messaging.LifeGameMessage;

public class ActionCardState implements GameState {
    public void enter(GameLogic gameLogic){
        ActionCard thisAction = gameLogic.getTopActionCard();
    };

    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage){
        return null;
    }

    public void exit(GameLogic gameLogic){

    }
}
