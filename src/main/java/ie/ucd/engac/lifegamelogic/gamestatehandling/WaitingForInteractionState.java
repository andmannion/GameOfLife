package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class WaitingForInteractionState implements GameState {

	@Override
	public void enter(GameLogic gameLogic, GameStateStack gameStateStack) {
		// Nothing to be done here
	}
	
	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		LifeGameMessageTypes incomingMessageType = lifeGameMessage.getLifeGameMessageType();
		
		if(incomingMessageType == LifeGameMessageTypes.StartupMessage) {
			// 
			
			// Must reply with a "PlayerMustChooseInitialCareerPath" message			
			
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
	}

	

}
