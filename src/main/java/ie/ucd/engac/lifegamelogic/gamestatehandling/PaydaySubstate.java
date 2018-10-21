package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.messaging.*;

public class PaydaySubstate implements GameState {
    private final static int PAYDAY_LANDED_ON_BONUS = 100000;
    public PaydaySubstate(){}

    @Override
    public void enter(GameLogic gameLogic) {
        OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
        if(currentOccupationCard != null) {
            int currentSalary = currentOccupationCard.getSalary();
            gameLogic.extractMoneyFromBank(currentSalary + PAYDAY_LANDED_ON_BONUS);
            gameLogic.getCurrentPlayer().addToBalance(currentSalary + PAYDAY_LANDED_ON_BONUS);
        }
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
