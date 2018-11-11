package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;

import java.util.ArrayList;

public abstract class GameState {
	abstract void enter(GameLogic gameLogic);

	abstract GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);

	abstract void exit(GameLogic gameLogic);

	protected static LifeGameMessage constructCardChoiceMessage(int relatedPlayerIndex, Chooseable firstOptionCard,
																		Chooseable secondOptionCard, String eventMessage) {

		ArrayList<Chooseable> validStandardCareerCardOptions = new ArrayList<>();

		validStandardCareerCardOptions.add(firstOptionCard);
		validStandardCareerCardOptions.add(secondOptionCard);

		return new DecisionRequestMessage(validStandardCareerCardOptions, relatedPlayerIndex, eventMessage);
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
}
