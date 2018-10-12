package ie.ucd.engac.lifegamelogic.gamestatehandling;

public interface GameState {
	void enter(GameLogic gameLogic);
	
	GameState handleInput(GameLogic gameLogic, UserInput userInput);
	
	void exit(GameLogic gameLogic);
}
