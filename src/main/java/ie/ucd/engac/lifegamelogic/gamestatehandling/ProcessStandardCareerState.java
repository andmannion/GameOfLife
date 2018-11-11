package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.messaging.*;

public class ProcessStandardCareerState extends GameState {

	@Override
	public void enter(GameLogic gameLogic) {
		// Must send a message to transition to processStandardCareer
		gameLogic.getCurrentPlayer().setCareerPath(CareerPathTypes.StandardCareer);

		// Set the response message to "CardChoice"
		// Get the two top CareerCards
		CareerCard firstCareerCardChoice = (CareerCard) gameLogic.getTopStandardCareerCard();
		CareerCard secondCareerCardChoice = (CareerCard) gameLogic.getTopStandardCareerCard();

		ArrayList<Card> pendingCardChoices = new ArrayList<>();
		pendingCardChoices.add(firstCareerCardChoice);
		pendingCardChoices.add(secondCareerCardChoice);

		LifeGameMessage replyMessage = constructChoiceMessage(
				gameLogic.getCurrentPlayer().getPlayerNumber(),
				(Chooseable) firstCareerCardChoice,
				(Chooseable) secondCareerCardChoice,
				"Choose career card");

		// Need to store both choices so that we can assign the chosen one to the
		// correct player,
		// and push the unchosen one to the bottom of the correct deck.
		gameLogic.storePendingChoiceCards(pendingCardChoices);
		gameLogic.setResponseMessage(replyMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;
			
			int choiceIndex = careerCardChoiceMessage.getChoiceIndex();

			//call static method in superclass to set/return card
            actOnOccupationCardChoice(gameLogic, choiceIndex);
			
			gameLogic.movePlayerToInitialCareerPath(gameLogic.getCurrentPlayerIndex());
			
			// Need to set the reply message to SpinRequest
			int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
			String eventMessage = "Player " + playNum + "'s turn.";
			SpinRequestMessage spinRequestMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),playNum, eventMessage);
			gameLogic.setResponseMessage(spinRequestMessage);

			gameLogic.decrementNumberOfUninitialisedPlayers();
			
			// TODO: Need to transition to the waitForSpinState - still need to figure out correct layout of 
			// hierarchical states and who owns them, transitions from lower to higher, etc.
			return new HandlePlayerMoveState();
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}

}
