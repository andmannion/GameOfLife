package ie.ucd.engac.lifegamelogic.gamestatehandling;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameEngine;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinResponseMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaydayTileTest {
    private final static int NUM_PLAYERS = 2;
    private final static String PRIOR_TILE_LOCATION = "aag";
    private final static String ONE_TILES_AHEAD = "aah";
    private final static String TWO_TILES_AHEAD = "aai";
    private final static int PAYDAY_LANDED_ON_BONUS = 100000;

    @Test
    void testPassingOver() {
        LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
        Spinnable testSpinner = new TestSpinner(2);
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(gameBoard, NUM_PLAYERS, testSpinner);

        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getTopCollegeCareerCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();

        Player player = gameLogic.getCurrentPlayer();
        int playerInitMoney = player.getCurrentMoney();

        player.setOccupationCard(occupationCard);

        int playerSalary = player.getOccupationCard().getSalary();
        player.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        // Mock messages to logic, performing pathChoiceState functionality
        LifeGameMessage initialMessage = new SpinResponseMessage();
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

        assertEquals(playerInitMoney+playerSalary, player.getCurrentMoney());

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
        LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
        Spinnable testSpinner = new TestSpinner(1);
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(gameBoard, NUM_PLAYERS, testSpinner);

        // Assert preconditions
        OccupationCard occupationCard = gameLogic.getTopCollegeCareerCard();
        int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
        CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
        MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();

        Player player = gameLogic.getCurrentPlayer();
        int playerInitMoney = player.getCurrentMoney();

        player.setOccupationCard(occupationCard);

        int playerSalary = player.getOccupationCard().getSalary();
        player.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        // Mock messages to logic, performing pathChoiceState functionality
        LifeGameMessage initialMessage = new SpinResponseMessage();
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

        assertEquals(playerInitMoney+playerSalary+PAYDAY_LANDED_ON_BONUS, player.getCurrentMoney());

        assertEquals(0, gameLogic.getNumberOfUninitialisedPlayers());
        assertEquals(occupationCard, gameLogic.getPlayerByIndex(0).getOccupationCard());
        assertEquals(ONE_TILES_AHEAD, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertNull(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice());
        assertEquals(numberOfActionCards, gameLogic.getPlayerByIndex(0).getNumberOfActionCards());
        assertEquals(careerPath, gameLogic.getPlayerByIndex(0).getCareerPath());
        assertEquals(maritalStatus, gameLogic.getPlayerByIndex(0).getMaritalStatus());
    }

}
