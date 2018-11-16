package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;

public class GameLogicInterface implements MessageReceiverAndResponder<LifeGameMessage> {
	private GameLogic gameLogic;
	
	public GameLogicInterface(LogicGameBoard gameBoard, int numPlayers) {
        gameLogic = new GameLogic(gameBoard, numPlayers, new Spinner());
	}

	@Override
	public LifeGameMessage receiveMessage(LifeGameMessage lifeGameMessage) {
		LifeGameMessage gameLogicResponse = gameLogic.handleInput(lifeGameMessage);		
		return gameLogicResponse;
	}		 
}
