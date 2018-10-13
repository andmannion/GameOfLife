package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;

public interface GameState {
	void enter(GameLogic gameLogic);
	
	GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);
	
	void exit(GameLogic gameLogic);
}
