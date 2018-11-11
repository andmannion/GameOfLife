package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class BabyState extends GameState {

	@Override
	public void enter(GameLogic gameLogic) {
		// Request a spin from the current player to determine how many babies they had
		String babyEventMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", " + "you landed on the Baby Stop. {1-3} : 0, {4-6} : 1, {7-8} : 2, {9-10} : 3.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),
																 gameLogic.getCurrentPlayerIndex(),
																 babyEventMessage);
		gameLogic.setResponseMessage(responseMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			int spinResult = gameLogic.getSpinner().spinTheWheel();
            return handleSpinForBabyState(gameLogic, spinResult);			
		}

		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}
	
	private GameState handleSpinForBabyState(GameLogic gameLogic, int spinResult) {
		String babyResultMessage = "Player 1, you had ";
		
		switch(spinResult) {
			case 1:
			case 2:
			case 3:
				babyResultMessage += "0 babies.";
				break;
			case 4:
			case 5:
			case 6:
				babyResultMessage += "1 baby.";
				gameLogic.getCurrentPlayer().addDependants(1);
				break;
			case 7:
			case 8:
				babyResultMessage += "2 babies.";
				gameLogic.getCurrentPlayer().addDependants(2);
				break;
			case 9:
			case 10:
				babyResultMessage += "3 babies.";
				gameLogic.getCurrentPlayer().addDependants(3);
				break;
			default:
				// Should never get here...
				break;
		}
		
		return new EndTurnState(babyResultMessage);
	}
}
