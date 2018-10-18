package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessage;

public class InitialisePlayerState implements GameState {
	// According to hierarchical state machines: the substate should
	// attempt to handle the event first.If it will not handle the event, it is passed further
	// up the hierarchy.
	private GameState currentSubstate;
	
	protected InitialisePlayerState() {
		currentSubstate = new PathChoiceState();
	}	
	
	@Override
	public void enter(GameLogic gameLogic) {
		currentSubstate.enter(gameLogic);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		GameState nextSubstate = currentSubstate.handleInput(gameLogic, lifeGameMessage);
		
		// Three options from a substate handleInput required:
		// - No change in state
		// - Change to the substate
		// - Change required in the superstate
		
		if(nextSubstate != null) {
			currentSubstate = nextSubstate;
			currentSubstate.enter(gameLogic);
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// Should decrement the number of players to be initialised upon exiting this state
		gameLogic.decrementNumberOfUnconfiguredPlayers();
	}

}
