package ie.ucd.engac.lifegamelogic.gamestatehandling;

import static org.junit.jupiter.api.Assertions.*;

import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.Test;

import TestOnly.TestHelpers;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinResponseMessage;

class GetMarriedStateTest {
	private final String PRIOR_TILE_LOCATION = "aa";
	private static final int NUM_PLAYERS = 2;
	
	@Test
	void testTwoPlayers() {
		// Testing with a player electing to take the College Career path		
		GameLogic gameLogic = configureGetMarriedStateTestGameLogic();
		
		Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();
		assertEquals(MaritalStatus.Single, currentPlayerUnderTest.getMaritalStatus());		
		
		currentPlayerUnderTest.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));
		
		int playerUnderTestInitialBalance = currentPlayerUnderTest.getCurrentMoney();
		int marriageGuestInitialBalance = gameLogic.getPlayerByIndex(1).getCurrentMoney();
		
		// Mock messages to logic, performing pathChoiceState functionality
        LifeGameMessage messageToLogic = new SpinResponseMessage();
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);
        
        // Now the current player is on the GetMarriedTile - other players have to be queried to spin
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());
        
        // Provide mock UI response
        messageToLogic = new SpinResponseMessage();
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        
        // Assert the current player is still the same, and they are being asked to spin again
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());
        assertEquals(gameLogic.getCurrentPlayer().getPlayerNumber(), currentPlayerUnderTest.getPlayerNumber());
        
        // Assert the player's marital status has been correctly updated
        assertEquals(MaritalStatus.Married, currentPlayerUnderTest.getMaritalStatus());
        
        /* Assert that the current player's money has increased by either the odd amount or even amount, 
        *  and that the other player(s) were deducted the same amount
        */
        int currentBalance = gameLogic.getCurrentPlayer().getCurrentMoney();
        
        if(currentBalance == playerUnderTestInitialBalance + GameConfig.get_married_even_payment) {
        	// Ensure the other player's balance was decremented by the same amount
        	int marriageGuestCurrentBalance = gameLogic.getPlayerByIndex(1).getCurrentMoney();        	
        	int marriageGuestBalanceDelta = marriageGuestInitialBalance - marriageGuestCurrentBalance;
        	assertEquals(GameConfig.get_married_even_payment, marriageGuestBalanceDelta);
        }
        else if(currentBalance == playerUnderTestInitialBalance + GameConfig.get_married_odd_payment) {
        	// Ensure the other player's balance was decremented by the same amount
        	int marriageGuestCurrentBalance = gameLogic.getPlayerByIndex(1).getCurrentMoney();        	
        	int marriageGuestBalanceDelta = marriageGuestInitialBalance - marriageGuestCurrentBalance;
        	assertEquals(GameConfig.get_married_odd_payment, marriageGuestBalanceDelta);
        }
        else {
        	int invalidBalanceDelta = currentBalance - playerUnderTestInitialBalance;        	
        	fail("Player's balance was changed by an invalid amount (" + invalidBalanceDelta + ") when they got married.");
        }
	}
	
	// TODO: Test this functionality with more than one other player.
	
	private static GameLogic configureGetMarriedStateTestGameLogic() {
		Spinnable spinner = new TestSpinner(1);
		GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
		
		return gameLogic;
	}
}
