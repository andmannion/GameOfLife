package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboard.DefaultBoardConfigHandler;
import ie.ucd.engac.lifegamelogic.gameboard.GameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.Test;

import static TestOnly.TestHelpers.importGameConfig;
import static org.junit.jupiter.api.Assertions.*;

class ProcessStandardCareerStateTest {
    @Test
    void testStandardCareerInitialisation() {
        int numberOfPlayers = 2;

        importGameConfig();
        GameLogic gameLogic = new GameLogic(new GameBoard(new DefaultBoardConfigHandler(GameConfig.game_board_config_file_location)), numberOfPlayers, new TestSpinner(0));

        LifeGameMessage initialMessage;
        LifeGameMessage responseMessage;
        LifeGameMessage choiceMessage;

        initialMessage = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        responseMessage = gameLogic.handleInput(initialMessage);


        for (int inc = 0; inc < numberOfPlayers; inc++) {
            assertEquals(LifeGameMessageTypes.LargeDecisionRequest, responseMessage.getLifeGameMessageType(), "Expected message not received");
            initialMessage = new DecisionResponseMessage(0, LifeGameMessageTypes.LargeDecisionResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
        }

        assertEquals(LifeGameMessageTypes.UIConfigMessage, responseMessage.getLifeGameMessageType(), "Expected message not received");
        initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        responseMessage = gameLogic.handleInput(initialMessage);

        // Assert preconditions
        assertEquals(numberOfPlayers, gameLogic.getNumberOfUninitialisedPlayers());
        assertNull(gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertNull(gameLogic.getPlayerByIndex(0).getCurrentLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertNull(gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getHouseCards().size());
        assertEquals(MaritalStatus.Single, gameLogic.getPlayerByIndex(0).getMaritalStatus());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getMovesRemaining());
        assertEquals(GameConfig.starting_money, gameLogic.getPlayerByIndex(0).getCurrentMoney());

        assertEquals(LifeGameMessageTypes.OptionDecisionRequest, responseMessage.getLifeGameMessageType(), "Expected message not received");

        //choose a path for this player
        int choiceIndex = PathChoiceState.STANDARD_CAREER_CHOICE_INDEX;
        choiceMessage = new DecisionResponseMessage(choiceIndex, LifeGameMessageTypes.OptionDecisionResponse);

        responseMessage = gameLogic.handleInput(choiceMessage);

        // Assert the logic is prompting to choose between two career cards
        assertEquals(LifeGameMessageTypes.OptionDecisionRequest, responseMessage.getLifeGameMessageType());

        // Choose the first card
        choiceIndex = 0;
        choiceMessage = new DecisionResponseMessage(choiceIndex, LifeGameMessageTypes.OptionDecisionResponse);
        responseMessage = gameLogic.handleInput(choiceMessage);

        // Assert that the player has been given a careercard
        assertNotNull(gameLogic.getPlayerByIndex(0).getOccupationCard());

        // Assert player has been moved to the first career tile
        assertEquals("b", gameLogic.getCurrentPlayer().getCurrentLocation().getLocation());

        // Ensure that this player has been registered as initialised
        assertEquals(numberOfPlayers - 1, gameLogic.getNumberOfUninitialisedPlayers());

        // Assert that other fields relating to this player have not been altered
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertNotNull(gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getHouseCards().size());
        assertEquals(MaritalStatus.Single, gameLogic.getPlayerByIndex(0).getMaritalStatus());
        assertEquals(0, gameLogic.getPlayerByIndex(0).getMovesRemaining());
        assertEquals(GameConfig.starting_money, gameLogic.getPlayerByIndex(0).getCurrentMoney());

        // Must roll now
        // Assert that we're being asked to roll
        assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());
    }
}