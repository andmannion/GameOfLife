package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;

import java.util.ArrayList;

public abstract class GameState {

    private ArrayList<Chooseable> pendingCardChoices;

	abstract void enter(GameLogic gameLogic);

	abstract GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);

	abstract void exit(GameLogic gameLogic);

    protected void storePendingChoiceCards(ArrayList<Chooseable> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    public ArrayList<Chooseable> getPendingCardChoices(){
        return pendingCardChoices;
    }

	protected static LifeGameMessage constructChoiceMessage(int relatedPlayerIndex, Chooseable firstOption,
                                                            Chooseable secondOption, String eventMessage) {

		ArrayList<Chooseable> validOptions = new ArrayList<>();

        validOptions.add(firstOption);
        validOptions.add(secondOption);

		return new DecisionRequestMessage(validOptions, relatedPlayerIndex, eventMessage);
	}

	protected static void actOnOccupationCardChoice(GameLogic gameLogic, int choiceIndex){
		// Need to assign the chosen card to the relevant player
		ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();
		OccupationCard chosenCareerCard = (OccupationCard) pendingCardChoices.get(choiceIndex);
		gameLogic.getCurrentPlayer().setOccupationCard(chosenCareerCard);

		// Only two cards at the moment, return unchosen
		OccupationCard unchosenCareerCard = (OccupationCard) pendingCardChoices.get((choiceIndex + 1) % 2);
		gameLogic.returnOccupationCard(unchosenCareerCard);
	}

    protected static HouseCard actOnHouseCardChoice(GameLogic gameLogic, int choiceIndex){
        // Need to assign the chosen card to the relevant player
        ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();
        HouseCard chosenHouseCard = (HouseCard) pendingCardChoices.get(choiceIndex);
        gameLogic.getCurrentPlayer().addHouseCard(chosenHouseCard);

        // Only two cards at the moment, return unchosen
        HouseCard unchosenHouseCard = (HouseCard) pendingCardChoices.get((choiceIndex + 1) % 2);
        gameLogic.returnHouseCard(unchosenHouseCard);

        return chosenHouseCard;
    }
}
