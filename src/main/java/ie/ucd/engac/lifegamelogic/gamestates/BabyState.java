package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.LifeGameRequestMessage;
import ie.ucd.engac.messaging.SpinResultMessage;

public class BabyState extends GameState {
	private int spinResult = 0;

	@Override
	public void enter(GameLogic gameLogic) {
		// Request a spin from the current player to determine how many babies they had
		String babyEventMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", " + "you landed on the Baby Stop. {1-3} : 0, {4-6} : 1, {7-8} : 2, {9-10} : 3.";
		
		LifeGameMessage responseMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,babyEventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex())
		);
		gameLogic.setResponseMessage(responseMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			spinResult = gameLogic.getSpinner().spinTheWheel();
			LifeGameMessage replyMessage = new SpinResultMessage(spinResult);
			gameLogic.setResponseMessage(replyMessage);
			return null;
		}
		else if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse) {
            return handleSpinForBabyState(gameLogic, spinResult);			
		}
		return null;
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
