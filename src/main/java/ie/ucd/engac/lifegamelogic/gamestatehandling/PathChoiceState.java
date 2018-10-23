package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.CareerPath;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class PathChoiceState implements GameState {
	private final int COLLEGE_UPFRONT_COST = 100000;

	@Override
	public void enter(GameLogic gameLogic) {

	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		/*
		 * Must reply with an OptionDecisionRequest for a path type to the
		 * InitialMessage, and continue to respond to any further messages with that
		 * same message until we get the desired response type.
		 */
		if (lifeGameMessage.getLifeGameMessageType() != LifeGameMessageTypes.OptionDecisionResponse) {
			LifeGameMessage replyMessage = constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber());
			gameLogic.setResponseMessage(replyMessage);
		} 
		else {
			// Have a path choice to resolve
			OccupationCardTypes pathChoiceResponse = parsePathChoiceResponse((DecisionResponseMessage) lifeGameMessage);

			// Must set the path choice for the current player based on what was returned
			if (pathChoiceResponse == OccupationCardTypes.CollegeCareer) {
				gameLogic.getCurrentPlayer().setCareerPath(CareerPathTypes.CollegeCareer);
				gameLogic.getCurrentPlayer().subtractFromBalance(COLLEGE_UPFRONT_COST);
						
				// Must move the player to the CollegeCareer path
				gameLogic.movePlayerToInitialCollegeCareerPath(gameLogic.getCurrentPlayer().getPlayerNumber());
				
				// Set the response message to "SpinRequest"
				LifeGameMessage replyMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
																	  gameLogic.getCurrentPlayer().getPlayerNumber(), "");
				gameLogic.setResponseMessage(replyMessage);
				
				// TODO: Need to exit from this inner state - what's next is the spinaccept
				// state
				gameLogic.decrementNumberOfUnconfiguredPlayers();
				
				return new HandlePlayerMoveState();
				//return null;
			} 
			else {
				// Must send a message to transition to processStandardCareer
				gameLogic.getCurrentPlayer().setCareerPath(CareerPathTypes.StandardCareer);

				// Set the response message to "CardChoice"
				// Get the two top CareerCards
				CareerCard firstCareerCardChoice = (CareerCard) gameLogic.getTopStandardCareerCard();
				CareerCard secondCareerCardChoice = (CareerCard) gameLogic.getTopStandardCareerCard();

				ArrayList<Card> pendingCardChoices = new ArrayList<>();
				pendingCardChoices.add(firstCareerCardChoice);
				pendingCardChoices.add(secondCareerCardChoice);

				LifeGameMessage replyMessage = constructStandardCareerCardChoiceMessage(
												gameLogic.getCurrentPlayer().getPlayerNumber(), 
												(Chooseable) firstCareerCardChoice,
												(Chooseable) secondCareerCardChoice);

				// Need to store both choices so that we can assign the chosen one to the
				// correct player,
				// and push the unchosen one to the bottom of the correct deck.
				gameLogic.storePendingChoiceCards(pendingCardChoices);
				gameLogic.setResponseMessage(replyMessage);

				return new ProcessStandardCareerState();
			}
		}
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// Must clear the sent message?
	}

	protected static LifeGameMessage constructPathChoiceMessage(int relatedPlayerIndex) {
		ArrayList<Chooseable> validPathChoices = new ArrayList<>();
		validPathChoices.add(new CareerPath(OccupationCardTypes.Career));
		validPathChoices.add(new CareerPath(OccupationCardTypes.CollegeCareer));

		LifeGameMessage replyMessage = new DecisionRequestMessage(validPathChoices, relatedPlayerIndex);

		return replyMessage;
	}

	private OccupationCardTypes parsePathChoiceResponse(DecisionResponseMessage pathChoiceMessage) {
		int choiceIndex = pathChoiceMessage.getChoiceIndex();

		if (0 == choiceIndex) {
			return OccupationCardTypes.Career;
		}
		if (1 == choiceIndex) {
			return OccupationCardTypes.CollegeCareer;
		}

		System.out.println("Invalid pathResponse received in PathChoiceState.handleInput().parsePathChoiceResponse()");
		return null;
	}

	private LifeGameMessage constructStandardCareerCardChoiceMessage(int relatedPlayerIndex, Chooseable firstOptionCard,
			Chooseable secondOptionCard) {

		ArrayList<Chooseable> validStandardCareerCardOptions = new ArrayList<>();

		validStandardCareerCardOptions.add(firstOptionCard);
		validStandardCareerCardOptions.add(secondOptionCard);

		LifeGameMessage replyMessage = new DecisionRequestMessage(validStandardCareerCardOptions, relatedPlayerIndex);

		return replyMessage;
	}
}