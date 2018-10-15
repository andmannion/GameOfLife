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
		
		/* TODO: Expected message response shouldn't be removed until the 
		 * incoming message matches. 
		 */
		LifeGameMessage expectedIncomingMessage = gameLogic.getExpectedResponse();
		
		if(incomingMessageType == LifeGameMessageTypes.StartupMessage) {
			// Should we transition to another state to send a message?
			// Need to have a "choose one of a set of options" message
			ArrayList<Chooseable> careerPathChoices = new ArrayList<>();
			
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
		else {
		    gameLogic.setResponseMessage(null);
        }/*
		// TODO: Must match; THEN check what it is
		else if(incomingMessageType == LifeGameMessageTypes.OptionDecisionResponse
				&& expectedIncomingMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			// Need to parse the response - what type of decision was made? Then assign the correct value to 
			// the player that the message related to
			// Then after we have updated the player - exiting UpdatePlayer state, we should construct a 
			// shadowPlayer, and send off this message.
			// Then transition into awaitUserInput, after pushing a new AwaitSpin expected message.
			
		}
		else{
			 gameLogic.setResponseMessage(null);
		}
		*/
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
	}

	

}
