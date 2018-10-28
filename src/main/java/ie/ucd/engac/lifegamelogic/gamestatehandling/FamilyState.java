package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.ChooseableString;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class FamilyState implements GameState {
	private ArrayList<String> familyPathChoices;
	private final String FAMILY_PATH_MESSAGE = "Take the family path";
	private final int FAMILY_PATH_MESSAGE_INDEX = 0;
	
	private final String LIFE_PATH_MESSAGE = "Take the life path";
	private final int LIFE_PATH_MESSAGE_INDEX = 1;
	
	private final String END_TURN_MESSAGE_FAMILY_PATH = "you have chosen the Family path.";
	private final String END_TURN_MESSAGE_LIFE_PATH = "you have chosen the Life path.";
	
	@Override
	public void enter(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		familyPathChoices = new ArrayList<>();
		familyPathChoices.add(FAMILY_PATH_MESSAGE);
		familyPathChoices.add(LIFE_PATH_MESSAGE);
		
		// Generate response for the player to choose between the choices provided
		LifeGameMessage responseMessage = new DecisionRequestMessage(convertToChooseableArray(familyPathChoices),
																	 gameLogic.getCurrentPlayerIndex());
		
		gameLogic.setResponseMessage(responseMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		// TODO Auto-generated method stub
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			// Must set the path for the next turn in the board
			return parseFamilyStopResponse(gameLogic, (DecisionResponseMessage) lifeGameMessage);
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}
	
	private GameState parseFamilyStopResponse(GameLogic gameLogic, DecisionResponseMessage decisionResponseMessage) {
		int choiceIndex = decisionResponseMessage.getChoiceIndex(); 
		
		/* Must ensure that the current tile has two adjacent forward locations; otherwise,
		* the board has been configured incorrectly. 
		*/		
		BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
		ArrayList<BoardLocation> familyPathOptions = gameLogic.getAdjacentForwardLocations(currentBoardLocation);
		
		int numberOfFamilyPathChoices = familyPathOptions.size();
		
		if(numberOfFamilyPathChoices != 2) {
			// Error
			System.err.println("Error: board has been configured incorrectly at the Family Stop Tile.");
			return null;
		}
		
		if(FAMILY_PATH_MESSAGE_INDEX == choiceIndex) {
			BoardLocation familyPathTile = familyPathOptions.get(FAMILY_PATH_MESSAGE_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(familyPathTile);
			
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", " + END_TURN_MESSAGE_FAMILY_PATH;
			
			return new EndTurnState(endTurnEventMessage);
		}
		if(LIFE_PATH_MESSAGE_INDEX == choiceIndex) {
			BoardLocation lifePathTile = familyPathOptions.get(LIFE_PATH_MESSAGE_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(lifePathTile);
						
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", " + END_TURN_MESSAGE_LIFE_PATH;
						
			return new EndTurnState(endTurnEventMessage);
		}
			
		return null;
	}
	
	// TODO: move this to a more appropriate place
	private static ArrayList<Chooseable> convertToChooseableArray(ArrayList<String> stringArray){
		ArrayList<Chooseable> chooseableArray = new ArrayList<>();
		
		for(String str : stringArray) {
			chooseableArray.add(new ChooseableString(str));
		}
		
		return chooseableArray;
	}
}
