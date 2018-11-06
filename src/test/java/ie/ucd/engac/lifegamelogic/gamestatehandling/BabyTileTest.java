package ie.ucd.engac.lifegamelogic.gamestatehandling;

import TestOnly.TestHelpers;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinResponseMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BabyTileTest {
    private static int NUM_PLAYERS = 2;

    @Test
    void testStandardBabyTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        String priorTileLocation = "ap";
        String endTileLocation = "aq";

        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getTopCollegeCareerCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        int numberOfHouseCards = gameLogic.getPlayerByIndex(0).getNumberOfHouseCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();
        int playerMoney = gameLogic.getPlayerByIndex(0).getCurrentMoney();
        int numberOfDependants = gameLogic.getPlayerByIndex(0).getNumberOfDependants();

        Player player = gameLogic.getCurrentPlayer();
        player.setCurrentLocation(new BoardLocation(priorTileLocation));

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new SpinResponseMessage();
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

        assertEquals(numberOfDependants+1, gameLogic.getPlayerByIndex(0).getNumberOfDependants());

        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(numberOfHouseCards, gameLogic.getPlayerByIndex(0).getNumberOfHouseCards());
        assertNull(gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(endTileLocation, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());

    }


}
