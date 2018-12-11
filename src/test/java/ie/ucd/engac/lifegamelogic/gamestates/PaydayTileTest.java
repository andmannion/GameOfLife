package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
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

class PaydayTileTest {
    private final static int NUM_PLAYERS = 2;
    private final static String PRIOR_TILE_LOCATION = "aag";
    private final static String ONE_TILES_AHEAD = "aah";
    private final static String TWO_TILES_AHEAD = "aai";

    @Test
    void testPassingOver() {
        int fixedSpinnerValue = 2;
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, fixedSpinnerValue);

        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getTopCollegeCareerCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        int numberOfHouseCards = gameLogic.getPlayerByIndex(0).getNumberOfHouseCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();

        Player player = gameLogic.getCurrentPlayer();

        int playerInitMoney = player.getCurrentMoney();

        player.setOccupationCard(occupationCard);

        int playerBonusNumber = occupationCard.getBonusNumber();
        int playerSalary = player.getOccupationCard().getSalary();
        player.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        assertEquals(LifeGameMessageTypes.SpinResult, responseMessage.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        responseMessage = gameLogic.handleInput(spinMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");

        int expectedBalance;
        if (playerBonusNumber == fixedSpinnerValue){
            expectedBalance = playerInitMoney+playerSalary+occupationCard.getBonusPaymentAmount();
        }
        else{
            expectedBalance = playerInitMoney+playerSalary;
        }

        assertEquals(expectedBalance, player.getCurrentMoney());

        assertEquals(numberOfHouseCards, gameLogic.getPlayerByIndex(0).getNumberOfHouseCards());
        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(occupationCard, gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(TWO_TILES_AHEAD, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());
    }

    @Test
    void testLandingOn() {

        int fixedSpinnerValue = 1;
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, fixedSpinnerValue);


        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getTopCollegeCareerCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        int numberOfHouseCards = gameLogic.getPlayerByIndex(0).getNumberOfHouseCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();

        Player player = gameLogic.getCurrentPlayer();
        int playerInitMoney = player.getCurrentMoney();

        player.setOccupationCard(occupationCard);

        int playerBonusNumber = occupationCard.getBonusNumber();
        int playerSalary = player.getOccupationCard().getSalary();
        player.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        assertEquals(LifeGameMessageTypes.SpinResult, responseMessage.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        responseMessage = gameLogic.handleInput(spinMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");

        int expectedBalance;
        if (playerBonusNumber == fixedSpinnerValue){
            expectedBalance = playerInitMoney+playerSalary+occupationCard.getBonusPaymentAmount()+GameConfig.payday_landed_on_bonus;
        }
        else{

            expectedBalance = playerInitMoney+playerSalary+GameConfig.payday_landed_on_bonus;
        }

        assertEquals(expectedBalance, player.getCurrentMoney());

        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(numberOfHouseCards, gameLogic.getPlayerByIndex(0).getNumberOfHouseCards());
        assertEquals(occupationCard, gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(ONE_TILES_AHEAD, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());
    }

}
