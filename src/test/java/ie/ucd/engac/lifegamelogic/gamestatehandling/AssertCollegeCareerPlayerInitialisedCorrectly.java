package ie.ucd.engac.lifegamelogic.gamestatehandling;

import static org.junit.jupiter.api.Assertions.*;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.Test;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

class AssertCollegeCareerPlayerInitialisedCorrectly {
	private final int NUM_PLAYERS = 2;
	@Test
	void test() {	
		/* I don't like the way that we can get the players and do whatever we
		*  like to them from outside - getters should be public, setters should be more 
		   restricted. */
		
		// Set up test
		TestHelpers.importGameConfig();
		LogicGameBoard gameBoard = new LogicGameBoard(GameConfig.game_board_config_file_location);
		Spinnable testSpinner = new TestSpinner(1);
		GameLogic gameLogic = new GameLogic(gameBoard, NUM_PLAYERS, testSpinner);
		
		// Assert preconditions
		assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS);
		assertEquals(gameLogic.getPlayerByIndex(0).getOccupationCard(), null);
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentLocation(), null);
		assertEquals(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice(), null);
		assertEquals(gameLogic.getPlayerByIndex(0).getNumberOfActionCards(), 0);
		assertEquals(gameLogic.getPlayerByIndex(0).getCareerPath(), null);
		assertEquals(gameLogic.getPlayerByIndex(0).getHouseCards().size(), 0);
		assertEquals(gameLogic.getPlayerByIndex(0).getMaritalStatus(), MaritalStatus.Single);
		assertEquals(gameLogic.getPlayerByIndex(0).getMovesRemaining(), 0);
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentMoney(), GameConfig.starting_money);
		
		// Mock messages to logic, performing pathChoiceState functionality
		LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);		
		LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
		
		assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.OptionDecisionRequest);
		
		// Provide mock UI response
		int choiceIndex = PathChoiceState.COLLEGE_CAREER_CHOICE_INDEX;
		initialMessage = new DecisionResponseMessage(choiceIndex);
		
		responseMessage = gameLogic.handleInput(initialMessage);		
		assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.SpinRequest);
		
		// Assert that player 0 has been initialised correctly
		assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS - 1);
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation(), "f");
		assertEquals(gameLogic.getPlayerByIndex(0).getCurrentMoney(), GameConfig.starting_money - GameConfig.college_upfront_cost);
		assertEquals(gameLogic.getPlayerByIndex(0).getCareerPath(), CareerPathTypes.CollegeCareer);
	}
}
