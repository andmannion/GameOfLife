package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTileTypes;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.LifeGameRequestMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("FieldCanBeLocal")
class FamilyStateTest {
    private static final int NUM_PLAYERS = 2;
    private final String PRIOR_TILE_LOCATION = "aap";
    private final String FAMILY_PATH_TILE = "aaq";
    private final String INITIAL_FAMILY_PATH_TILE = "aaz";
    private final String INITIAL_LIFE_PATH_TILE = "aar";

    @Test
    void testFamilyPathWithTwoPlayers() {
        int playerUnderTestNumber = 1;
        GameLogic gameLogic = configureFamilyStateTestGameLogic();

        LifeGameMessage messageToLogic;
        LifeGameMessage messageFromLogic = null;

        while (playerUnderTestNumber <= NUM_PLAYERS) {

            Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

            assertEquals(playerUnderTestNumber, currentPlayerUnderTest.getPlayerNumber());
            currentPlayerUnderTest.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");
            LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            messageFromLogic = gameLogic.handleInput(spinMessage);

            // Should have a choice to make now
            assertEquals(LifeGameMessageTypes.OptionDecisionRequest, messageFromLogic.getLifeGameMessageType());

            // Make sure there is no initial pendingBoardForkChoice
            assertNull(gameLogic.getCurrentPlayer().getPendingBoardForkChoice());

            // Select the FamilyPath
            messageToLogic = new DecisionResponseMessage(FamilyState.FAMILY_PATH_MESSAGE_INDEX,LifeGameMessageTypes.OptionDecisionResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            // Should have ended the turn
            assertEquals(LifeGameMessageTypes.AckRequest, messageFromLogic.getLifeGameMessageType());

            // Assert getting the right message
            assertEquals("Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you have chosen the Family path.",
                    ((LifeGameRequestMessage)messageFromLogic).getEventMsg());

            // The player should be on the FamilyPathTile
            BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
            GameBoardTile currentTile = gameLogic.getGameBoard().getGameBoardTileFromID(currentBoardLocation);

            assertEquals(GameBoardTileTypes.Stop, currentTile.getGameBoardTileType());
            assertEquals(GameBoardStopTileTypes.Family, ((GameBoardStopTile) currentTile).getGameBoardStopTileType());

            // Make sure there is a pending path choice for next turn
            assertNotNull(gameLogic.getCurrentPlayer().getPendingBoardForkChoice());

            // Send back an ACK response to end the turn
            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            playerUnderTestNumber++;
        }

        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Assert players have been moved off the tile
        assertEquals(INITIAL_FAMILY_PATH_TILE, gameLogic.getCurrentPlayer().getCurrentLocation().getLocation());
    }

    @SuppressWarnings("Duplicates")
    @Test
    void testLifePathWithTwoPlayers() {
        int playerUnderTestNumber = 1;
        GameLogic gameLogic = configureFamilyStateTestGameLogic();

        LifeGameMessage messageToLogic;
        LifeGameMessage messageFromLogic = null;

        while (playerUnderTestNumber <= NUM_PLAYERS) {
            Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

            assertEquals(playerUnderTestNumber, currentPlayerUnderTest.getPlayerNumber());
            currentPlayerUnderTest.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");
            LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            messageFromLogic = gameLogic.handleInput(spinMessage);

            // Should have a choice to make now
            assertEquals(LifeGameMessageTypes.OptionDecisionRequest, messageFromLogic.getLifeGameMessageType());

            // Make sure there is no initial pendingBoardForkChoice
            assertNull(gameLogic.getCurrentPlayer().getPendingBoardForkChoice());

            // Select the FamilyPath
            messageToLogic = new DecisionResponseMessage(FamilyState.LIFE_PATH_MESSAGE_INDEX, LifeGameMessageTypes.OptionDecisionResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            // Should have ended the turn
            assertEquals(LifeGameMessageTypes.AckRequest, messageFromLogic.getLifeGameMessageType());

            // Assert getting the right message
            assertEquals("Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you have chosen the Life path.",
                    ((LifeGameRequestMessage)messageFromLogic).getEventMsg());

            // The player should be on the FamilyPathTile
            BoardLocation currentBoardLocation = gameLogic.getCurrentPlayer().getCurrentLocation();
            GameBoardTile currentTile = gameLogic.getGameBoard().getGameBoardTileFromID(currentBoardLocation);

            assertEquals(GameBoardTileTypes.Stop, currentTile.getGameBoardTileType());
            assertEquals(GameBoardStopTileTypes.Family, ((GameBoardStopTile) currentTile).getGameBoardStopTileType());

            // Make sure there is a pending path choice for next turn
            assertNotNull(gameLogic.getCurrentPlayer().getPendingBoardForkChoice());

            // Send back an ACK response to end the turn
            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            playerUnderTestNumber++;
        }

        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);
        // Assert players have been moved off the tile
        assertEquals(INITIAL_LIFE_PATH_TILE, gameLogic.getCurrentPlayer().getCurrentLocation().getLocation());
    }


    private static GameLogic configureFamilyStateTestGameLogic() {
        Spinnable spinner = new TestSpinner(1);
        return TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
    }
}