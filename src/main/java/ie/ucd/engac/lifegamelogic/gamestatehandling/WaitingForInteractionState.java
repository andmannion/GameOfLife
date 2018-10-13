package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.gameboardlogic.CareerPath;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class WaitingForInteractionState implements GameState {

	@Override
	public void enter(GameLogic gameLogic) {
		// Nothing to be done here

	}
	
	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		LifeGameMessageTypes incomingMessageType = lifeGameMessage.getLifeGameMessageType();
		
		if(incomingMessageType == LifeGameMessageTypes.StartupMessage) {
			// Should we transition to another state to send a message?
			// Need to have a "choose one of a set of options" message
			ArrayList<Chooseable> careerPathChoices = new ArrayList<Chooseable>();
			
			for(OccupationCardTypes occupationType : OccupationCardTypes.values()) {
				careerPathChoices.add(new CareerPath(occupationType));
			}
			
			int relatedPlayerIndex = gameLogic.getCurrentPlayer().getPlayerNumber();
			
			LifeGameMessage optionDecisionRequestMessage = new DecisionRequestMessage(careerPathChoices, relatedPlayerIndex);
			
			gameLogic.setResponseMessage(optionDecisionRequestMessage);			
			gameLogic.addExpectedResponse(new LifeGameMessage(LifeGameMessageTypes.OptionDecisionResponse));
			
			// No further transition is necessary at this time
			return null;
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
	}

	

}
