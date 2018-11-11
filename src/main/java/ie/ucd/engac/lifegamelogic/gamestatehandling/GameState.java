package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;

public abstract class GameState {
	abstract void enter(GameLogic gameLogic);

	abstract GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage);

	abstract void exit(GameLogic gameLogic);
}
