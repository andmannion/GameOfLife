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

public class HolidayTileTest {
    private final static int NUM_PLAYERS = 2;


    @Test
    void testRegularHolidayTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        String priorTileLocation = "ap";
        String endTileLocation = "aq";
        testGenericHolidayTile(gameLogic,priorTileLocation,endTileLocation);
    }

    @Test
    void testStopHolidayTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 2);
        String priorTileLocation = "aaap";
        String endTileLocation = "aaaq";
        testGenericHolidayTile(gameLogic,priorTileLocation,endTileLocation);
    }

    private void testGenericHolidayTile(GameLogic gameLogic, String priorTileLocation,String endTileLocation){
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

        assertEquals(LifeGameMessageTypes.SpinResult, responseMessage.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        responseMessage = gameLogic.handleInput(spinMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");

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
