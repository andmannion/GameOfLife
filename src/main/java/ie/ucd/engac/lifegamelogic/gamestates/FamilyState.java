package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class FamilyState extends GameState {
	static final int LIFE_PATH_MESSAGE_INDEX = 0;
	static final int FAMILY_PATH_MESSAGE_INDEX = 1;

	@Override
	public void enter(GameLogic gameLogic) {
		ArrayList<String> familyPathChoices = new ArrayList<>();
		String lifePathMessage = "Take the life path";
		familyPathChoices.add(lifePathMessage);
		String familyPathMessage = "Take the family path";
		familyPathChoices.add(familyPathMessage);
		
		// Generate response for the player to choose between the choices provided
		LifeGameMessageTypes requestType = LifeGameMessageTypes.OptionDecisionRequest;
		ShadowPlayer sp = gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex());
		LifeGameMessage responseMessage = new DecisionRequestMessage(ChooseableString.convertToChooseableArray(familyPathChoices),
					gameLogic.getCurrentPlayerIndex(), "Choose either the Life or Family path.", requestType, sp);
		
		gameLogic.setResponseMessage(responseMessage);
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			// Must set the path for the next turn in the board
			return parseFamilyStopResponse(gameLogic, (DecisionResponseMessage) lifeGameMessage);
		}
		
		return null;
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
			System.err.println("Error: board has been configured incorrectly at the Family Stop Tile for player " + gameLogic.getCurrentPlayer().getPlayerNumber());
			System.exit(-1);
		}

		if(LIFE_PATH_MESSAGE_INDEX == choiceIndex) {
			BoardLocation lifePathTile = familyPathOptions.get(LIFE_PATH_MESSAGE_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(lifePathTile);

			String endTurnMessageLifePath = "you have chosen the Life path.";
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", " + endTurnMessageLifePath;

			return new EndTurnState(endTurnEventMessage);
		}

		if(FAMILY_PATH_MESSAGE_INDEX == choiceIndex) {
			BoardLocation familyPathTile = familyPathOptions.get(FAMILY_PATH_MESSAGE_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(familyPathTile);

			String endTurnMessageFamilyPath = "you have chosen the Family path.";
			String endTurnEventMessage = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", " + endTurnMessageFamilyPath;
			
			return new EndTurnState(endTurnEventMessage);
		}
			
		return null;
	}
}
