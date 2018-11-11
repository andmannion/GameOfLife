package ie.ucd.engac.lifegamelogic.gamestatehandling;

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
}
