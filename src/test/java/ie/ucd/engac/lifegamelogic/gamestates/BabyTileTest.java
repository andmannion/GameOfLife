package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BabyTileTest {
    private static int NUM_PLAYERS = 2;

    @Test
    void testStandardBabyTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        String priorTileLocation = "aaab";
        String endTileLocation = "aaac";

        testNStandardTile(gameLogic, priorTileLocation, endTileLocation, 1);
    }

    @Test
    void testStandardTwinsTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        String priorTileLocation = "aah";
        String endTileLocation = "aai";
        testNStandardTile(gameLogic, priorTileLocation, endTileLocation, 2);
    }

    @Test
    void testBabyStopTile(){
        String priorTileLocation = "aax";
        String endTileLocation = "aay";
        int expectedDependants;
        double divVal;
        for (int inc = 1;inc<=10;inc++) {
            if(inc <= 6) {
                divVal = 3.1; //maps 1-3 -> 0, 4-6 -> 1
            }
            else {
                divVal = 3; //maps 7-8-> 2, 9-10 -> 3
            }
            GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, inc);
            expectedDependants = (int)Math.floor(((double)inc)/divVal);
            testBabyStopTile(gameLogic, priorTileLocation, endTileLocation, expectedDependants);
        }
    }

    private void testNStandardTile(GameLogic gameLogic, String priorTileLocation, String endTileLocation,
                                   int numDependantsToAdd){
        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getPlayerByIndex(0).getOccupationCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        int numberOfHouseCards = gameLogic.getPlayerByIndex(0).getNumberOfHouseCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();
        int playerMoney = gameLogic.getPlayerByIndex(0).getCurrentMoney();
        int numberOfDependants = gameLogic.getPlayerByIndex(0).getNumberOfDependants();

        Player player = gameLogic.getCurrentPlayer();
        player.setCurrentLocation(new BoardLocation(priorTileLocation));

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

        assertEquals(numberOfDependants+numDependantsToAdd, gameLogic.getPlayerByIndex(0).getNumberOfDependants());

        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(numberOfHouseCards, gameLogic.getPlayerByIndex(0).getNumberOfHouseCards());
        assertNull(gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(endTileLocation, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());
        assertEquals(occupationCard, gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(playerMoney, gameLogic.getPlayerByIndex(0).getCurrentMoney());
    }

    private void testBabyStopTile(GameLogic gameLogic, String priorTileLocation, String endTileLocation, int expectedDependants){
        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getPlayerByIndex(0).getOccupationCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        int numberOfHouseCards = gameLogic.getPlayerByIndex(0).getNumberOfHouseCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();
        int playerMoney = gameLogic.getPlayerByIndex(0).getCurrentMoney();
        int numberOfDependants = gameLogic.getPlayerByIndex(0).getNumberOfDependants();

        Player player = gameLogic.getCurrentPlayer();
        player.setCurrentLocation(new BoardLocation(priorTileLocation));

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());

        initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

        //check that the correct number of kids has been added
        assertEquals(numberOfDependants+expectedDependants, gameLogic.getPlayerByIndex(0).getNumberOfDependants());

        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(numberOfHouseCards, gameLogic.getPlayerByIndex(0).getNumberOfHouseCards());
        assertNull(gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(endTileLocation, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());
        assertEquals(occupationCard, gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(playerMoney, gameLogic.getPlayerByIndex(0).getCurrentMoney());
    }

}
