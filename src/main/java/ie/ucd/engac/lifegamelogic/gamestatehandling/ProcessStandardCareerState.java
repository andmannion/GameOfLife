package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class ProcessStandardCareerState extends InitialisePlayerState {

	@Override
	public void enter(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		// Set the expected response to something?
		
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			DecisionResponseMessage careerCardChoiceMessage = (DecisionResponseMessage) lifeGameMessage;
			
			int choiceIndex = careerCardChoiceMessage.getChoiceIndex();
			
			// Need to assign the chosen card to the relevant player
			ArrayList<Card> pendingCardChoices = gameLogic.getPendingCardChoices();			
			OccupationCard chosenCareerCard = (OccupationCard) pendingCardChoices.get(choiceIndex);			
			gameLogic.getCurrentPlayer().setOccupationCard(chosenCareerCard);
			
			// Only two cards at the moment, return unchosen
			OccupationCard unchosenCareerCard = (OccupationCard) pendingCardChoices.get((choiceIndex + 1) % 2);			
			gameLogic.returnCareerCard(unchosenCareerCard);
			
			// Need to set the reply message to SpinRequest
			LifeGameMessage replyMessage = new LifeGameMessage(LifeGameMessageTypes.SpinRequest);
			
			gameLogic.setResponseMessage(replyMessage);
			
			// TODO: Need to transition to the waitForSpinState - still need to figure out correct layout of 
			// hierarchical states and who owns them, transitions from lower to higher, etc.
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}
	
}
