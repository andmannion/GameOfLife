package ie.ucd.engac.lifegamelogic;

import ie.ucd.engac.lifegamelogic.gameboard.GameBoard;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;

public class GameLogicInterface implements MessageReceiverAndResponder<LifeGameMessage> {
	private GameLogic gameLogic;
	
	public GameLogicInterface(GameBoard gameBoard, int numPlayers) {
        gameLogic = new GameLogic(gameBoard, numPlayers, new Spinner());
	}

	@Override
	public LifeGameMessage receiveMessage(LifeGameMessage lifeGameMessage) {
		return gameLogic.handleInput(lifeGameMessage);
	}		 
}
