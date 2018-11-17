package ie.ucd.engac.lifegamelogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.ShadowPlayer;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        final int numberOfPlayers = 1;
        final int fixedSpinnerValue = 1;
        gameLogic = TestHelpers.setupTestGenericPreconditions(numberOfPlayers,fixedSpinnerValue);
    }

    @AfterEach
    void tearDown() {
        gameLogic = null;
    }

    /**
     * test that method retires the player correctly when they have no loans
     */
    @Test
    void retireCurrentPlayerWithoutLoans(){
        //setup player
        Player player = gameLogic.getCurrentPlayer();
        int initMoney = player.getCurrentMoney();
        int expectedEndMoney = initMoney + player.computeRetirementBonuses(0); //other test confirms this works

        gameLogic.retireCurrentPlayer();

        int currentPlayerListSize = gameLogic.getPlayers().size();
        ArrayList<Player> retiredPlayers = gameLogic.getRankedRetiredPlayers();

        assertEquals(0, currentPlayerListSize, "Player not removed from ArrayList of players.");
        assertEquals(1, retiredPlayers.size(), "Player not added to ArrayList of retirees."); //TODO is this bad? - acquired indirectly via sorted list

        Player retiredPlayer = retiredPlayers.get(0);

        assertEquals(expectedEndMoney, retiredPlayer.getCurrentMoney(), "Expected end money is not correct.");
    }

    /**
     * test that method retires the player correctly when they have loans
     */
    @Test
    void retireCurrentPlayerWithLoans(){
        //setup player
        Player player = gameLogic.getCurrentPlayer();
        gameLogic.takeOutALoan(player.getPlayerNumber());

        int initMoney = player.getCurrentMoney();
        int expectedEndMoney = initMoney + player.computeRetirementBonuses(0); //other test confirms this works
        expectedEndMoney -= GameConfig.repayment_amount; //account for the loan repayment

        gameLogic.retireCurrentPlayer();

        int currentPlayerListSize = gameLogic.getPlayers().size();
        ArrayList<Player> retiredPlayers = gameLogic.getRankedRetiredPlayers();

        assertEquals(0, currentPlayerListSize, "Player not removed from ArrayList of players.");
        assertEquals(1, retiredPlayers.size(), "Player not added to ArrayList of retirees."); //TODO is this bad? - acquired indirectly via sorted list

        Player retiredPlayer = retiredPlayers.get(0);

        assertEquals(expectedEndMoney, retiredPlayer.getCurrentMoney(), "Expected end money is not correct.");
    }

    /**
     * test that method retires the right player
     */
    @Test
    void retireCurrentPlayerGeqTwoRemain(){
        //override @BeforeEach
        final int numberOfPlayers = 2;
        final int fixedSpinnerValue = 1;
        gameLogic = TestHelpers.setupTestGenericPreconditions(numberOfPlayers,fixedSpinnerValue);

        //setup player
        Player player = gameLogic.getCurrentPlayer();
        Player otherPlayer = gameLogic.getPlayerByIndex(gameLogic.getCurrentPlayerIndex()+1);
        gameLogic.takeOutALoan(player.getPlayerNumber());

        int initMoney = player.getCurrentMoney();
        int expectedEndMoney = initMoney + player.computeRetirementBonuses(0); //other test confirms this works
        expectedEndMoney -= GameConfig.repayment_amount; //account for the loan repayment

        gameLogic.retireCurrentPlayer();

        Player remainingPlayer = gameLogic.getCurrentPlayer();
        int currentPlayerListSize = gameLogic.getPlayers().size();
        ArrayList<Player> retiredPlayers = gameLogic.getRankedRetiredPlayers();

        assertEquals(numberOfPlayers-1, currentPlayerListSize, "Incorrect number of players removed from ArrayList of players.");
        assertEquals(numberOfPlayers-1, retiredPlayers.size(), "Incorrect number of players added to ArrayList of retirees."); //TODO is this bad? - acquired indirectly via sorted list

        Player retiredPlayer = retiredPlayers.get(0);

        assertEquals(player,retiredPlayer,"Retired the wrong player");
        assertEquals(otherPlayer,remainingPlayer,"Removed the wrong player.");

        assertEquals(expectedEndMoney, retiredPlayer.getCurrentMoney(), "Expected end money is not correct.");
    }

    /**
     * test that method fails when no players exist
     */
    @Test
    void retireCurrentPlayerWhenNoneRemain(){
        gameLogic.retireCurrentPlayer();
        int currentPlayerListSize = gameLogic.getPlayers().size();
        //now none remain so trt to remove

        assertThrows(RuntimeException.class, () -> gameLogic.retireCurrentPlayer(),"Tried retiring with empty player ArrayList & it did not error.");

        ArrayList<Player> retiredPlayers = gameLogic.getRankedRetiredPlayers();
        assertEquals(0, currentPlayerListSize, "Player not removed from ArrayList of players.");
        assertEquals(1, retiredPlayers.size(), "Player not added to ArrayList of retirees."); //TODO is this bad? - acquired indirectly via sorted list
    }


    @TestFactory
    Collection<DynamicTest> runSingleDynamicTest() {
        return Collections.singletonList(
                DynamicTest.dynamicTest("Add test",
                        this::getShadowPlayerTest));
    }

    /**
     *  other tests will ensure invalid data is not assigned to these variables
     *  just need to test that the are set correctly.
     *  these fields have not got getters, therefore must test via reflection
     */
    private void getShadowPlayerTest(){
        final int numberOfPlayers = 1;
        final int fixedSpinnerValue = 1;
        gameLogic = TestHelpers.setupTestGenericPreconditions(numberOfPlayers,fixedSpinnerValue); //BeforeEach not called

        int playerNumber;

        Field fPlayerNumber;
                int playerIndex = gameLogic.getCurrentPlayerIndex();
        Player targetPlayer = gameLogic.getCurrentPlayer();
        ShadowPlayer sp = gameLogic.getShadowPlayer(playerIndex);

        try{
            fPlayerNumber = ShadowPlayer.class.getDeclaredField("playerNumber");

            fPlayerNumber.setAccessible(true);

            playerNumber = (int)fPlayerNumber.get(sp);

            assertEquals(targetPlayer.getPlayerNumber(),playerNumber, "attribute mismatch");

        }
        catch (Exception ex){
            fail(ex);
        }





    }

}