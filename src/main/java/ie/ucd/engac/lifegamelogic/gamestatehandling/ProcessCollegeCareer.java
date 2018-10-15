package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;

public class ProcessCollegeCareer implements GameState {
	private final int COLLEGE_UPFRONT_COST = 100000;

	@Override
	public void enter(GameLogic gameLogic) {
		// All we have to do in this state is perform sime mutations on the 
		// current player
		gameLogic.getCurrentPlayer().subtractFromBalance(COLLEGE_UPFRONT_COST);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		// Must reject all messages in this state?
		// Want to transition to outside of the superstate now
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}

}
