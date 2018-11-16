package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

import java.util.ArrayList;

public abstract class GameState {

    private ArrayList<Chooseable> pendingCardChoices;

	public abstract void enter(GameLogic gameLogic);

	public abstract GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);

	public abstract void exit(GameLogic gameLogic);

    protected void storePendingChoiceCards(ArrayList<Chooseable> pendingCardChoices) {
        this.pendingCardChoices = pendingCardChoices;
    }

    protected ArrayList<Chooseable> getPendingCardChoices(){
        return pendingCardChoices;
    }

	protected LifeGameMessage setupChoiceAndMessage(int relatedPlayerIndex, Chooseable firstOption,
                                                    Chooseable secondOption, String eventMessage) {
        // Need to store both choices so that we can assign the chosen one to the
        // correct player, and push the unchosen one to the bottom of the correct deck.

		ArrayList<Chooseable> validOptions = new ArrayList<>();

        validOptions.add(firstOption);
        validOptions.add(secondOption);

        storePendingChoiceCards(validOptions);

		LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
		return new DecisionRequestMessage(validOptions, relatedPlayerIndex, eventMessage, requestType);
	}

	protected void actOnOccupationCardChoice(GameLogic gameLogic, int choiceIndex){
		// Need to assign the chosen card to the relevant player
		//ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices(); //TODO remove
		OccupationCard chosenCareerCard = (OccupationCard) getPendingCardChoices().get(choiceIndex);
		gameLogic.getCurrentPlayer().setOccupationCard(chosenCareerCard);

		// Only two cards at the moment, return unchosen
		OccupationCard unchosenCareerCard = (OccupationCard) getPendingCardChoices().get((choiceIndex + 1) % 2);
		gameLogic.returnOccupationCard(unchosenCareerCard);
	}
}
