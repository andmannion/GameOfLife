package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboard.DefaultBoardConfigHandler;
import ie.ucd.engac.lifegamelogic.gameboard.GameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("FieldCanBeLocal")
class PathChoiceCollegeCareerTest {
	private final int NUM_PLAYERS = 2;
	@Test
	void test() {	
		/* I don't like the way that we can get the players and do whatever we
		*  like to them from outside - getters should be public, setters should be more 
		   restricted. */
		
		// Set up test
		TestHelpers.importGameConfig();
		GameBoard gameBoard = new GameBoard(new DefaultBoardConfigHandler(GameConfig.game_board_config_file_location));
		Spinnable testSpinner = new TestSpinner(1);
		GameLogic gameLogic = new GameLogic(gameBoard, NUM_PLAYERS, testSpinner);
		
		// Assert preconditions
		assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS);
		assertNull(gameLogic.getPlayerByIndex(0).getOccupationCard());
		assertNull(gameLogic.getPlayerByIndex(0).getCurrentLocation());
		assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
		assertEquals(0, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
		assertNull(gameLogic.getPlayerByIndex(0).getCareerPath());
		assertEquals(0, gameLogic.getPlayerByIndex(0).getHouseCards().size());
		assertEquals(MaritalStatus.Single, gameLogic.getPlayerByIndex(0).getMaritalStatus());
		assertEquals(0, gameLogic.getPlayerByIndex(0).getMovesRemaining());
		assertEquals(GameConfig.starting_money, gameLogic.getPlayerByIndex(0).getCurrentMoney());
		
		// Mock messages to logic, performing pathChoiceState functionality
		LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
		LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

		for(int inc=0;inc<NUM_PLAYERS;inc++){
			assertEquals(LifeGameMessageTypes.LargeDecisionRequest,responseMessage.getLifeGameMessageType(),"Expected message not received");
			initialMessage = new DecisionResponseMessage(0,LifeGameMessageTypes.LargeDecisionResponse);
			responseMessage = gameLogic.handleInput(initialMessage);
		}

		assertEquals(LifeGameMessageTypes.UIConfigMessage,responseMessage.getLifeGameMessageType(),"Expected message not received");
		initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
		responseMessage = gameLogic.handleInput(initialMessage);
		
		assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.OptionDecisionRequest);
		
		// Provide mock UI response
		int choiceIndex = PathChoiceState.COLLEGE_CAREER_CHOICE_INDEX;
		initialMessage = new DecisionResponseMessage(choiceIndex,LifeGameMessageTypes.OptionDecisionResponse);
		
		responseMessage = gameLogic.handleInput(initialMessage);		
		assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.SpinRequest);
		
		// Assert that player 0 has been initialised correctly
		assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS - 1);
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation(), "f");
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentMoney(), GameConfig.starting_money - GameConfig.college_upfront_cost);
		assertEquals(gameLogic.getPlayerByIndex(0).getCareerPath(), CareerPathTypes.CollegeCareer);
	}
}
