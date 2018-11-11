package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.messaging.ChooseableString;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class FamilyState extends GameState {
	private static final int FAMILY_PATH_MESSAGE_INDEX = 0;
	private static final int LIFE_PATH_MESSAGE_INDEX = 1;

	@Override
	public void enter(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		ArrayList<String> familyPathChoices = new ArrayList<>();
		String familyPathMessage = "Take the family path";
		familyPathChoices.add(familyPathMessage);
		String lifePathMessage = "Take the life path";
		familyPathChoices.add(lifePathMessage);
		
		// Generate response for the player to choose between the choices provided
		LifeGameMessage responseMessage = new DecisionRequestMessage(ChooseableString.convertToChooseableArray(familyPathChoices),
					gameLogic.getCurrentPlayerIndex(), "Choose either the Life or Family path.");
		
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

			String endTurnMessageFamilyPath = "you have chosen the Family path.";
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", " + endTurnMessageFamilyPath;
			
			return new EndTurnState(endTurnEventMessage);
		}

		if(LIFE_PATH_MESSAGE_INDEX == choiceIndex) {
			BoardLocation lifePathTile = familyPathOptions.get(LIFE_PATH_MESSAGE_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(lifePathTile);

			String endTurnMessageLifePath = "you have chosen the Life path.";
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayerIndex() + ", " + endTurnMessageLifePath;
						
			return new EndTurnState(endTurnEventMessage);
		}
			
		return null;
	}
	

}
