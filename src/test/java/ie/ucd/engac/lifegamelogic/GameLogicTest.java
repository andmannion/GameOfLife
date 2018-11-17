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
        int numDependants;
        int numActionCards;
        int loans;
        int numLoans;
        int bankBalance;

        Field fPlayerNumber;
        Field fNumDependants;
        Field fNumActionCards;
        Field fLoans;
        Field fNumLoans;
        Field fBankBalance;

        GameBoardTile currentTile;
        OccupationCard occupation;
        int martialStatus;
        ArrayList<HouseCard> houses;

        Field fCurrentTile;
        Field fOccupation;
        Field fMartialStatus;
        Field fHouses;

        int playerIndex = gameLogic.getCurrentPlayerIndex();
        Player targetPlayer = gameLogic.getCurrentPlayer();
        ShadowPlayer sp = gameLogic.getShadowPlayer(playerIndex);

        try{
            fPlayerNumber = ShadowPlayer.class.getDeclaredField("playerNumber");
            fNumDependants = ShadowPlayer.class.getDeclaredField("numDependants");
            fNumActionCards = ShadowPlayer.class.getDeclaredField("numActionCards");
            fLoans = ShadowPlayer.class.getDeclaredField("loans");
            fNumLoans = ShadowPlayer.class.getDeclaredField("numLoans");
            fBankBalance = ShadowPlayer.class.getDeclaredField("bankBalance");
            fCurrentTile = ShadowPlayer.class.getDeclaredField("currentTile");
            fOccupation = ShadowPlayer.class.getDeclaredField("occupation");
            fMartialStatus = ShadowPlayer.class.getDeclaredField("martialStatus");
            fHouses = ShadowPlayer.class.getDeclaredField("houses");

            fPlayerNumber.setAccessible(true);
            fNumDependants.setAccessible(true);
            fNumActionCards.setAccessible(true);
            fLoans.setAccessible(true);
            fNumLoans.setAccessible(true);
            fBankBalance.setAccessible(true);
            fCurrentTile.setAccessible(true);
            fOccupation.setAccessible(true);
            fMartialStatus.setAccessible(true);
            fHouses.setAccessible(true);

            playerNumber = (int)fPlayerNumber.get(sp);
            numDependants = (int)fNumDependants.get(sp);
            numActionCards = (int)fNumActionCards.get(sp);
            loans = (int)fLoans.get(sp);
            numLoans = (int)fNumLoans.get(sp);
            bankBalance = (int)fBankBalance.get(sp);
            currentTile = (GameBoardTile)fCurrentTile.get(sp);
            occupation = (OccupationCard)fOccupation.get(sp);
            martialStatus = (int)fMartialStatus.get(sp);
            houses = (ArrayList<HouseCard>)fHouses.get(sp);

            assertEquals(targetPlayer.getPlayerNumber(),playerNumber, "attribute mismatch");
            assertEquals(targetPlayer.getNumberOfDependants(),numDependants, "attribute mismatch");
            assertEquals(targetPlayer.getNumberOfActionCards(),numActionCards, "attribute mismatch");
            assertEquals(targetPlayer.getTotalLoansOutstanding(gameLogic),loans, "attribute mismatch");
            assertEquals(targetPlayer.getNumberOfLoans(gameLogic),numLoans, "attribute mismatch");
            assertEquals(targetPlayer.getCurrentMoney(),bankBalance, "attribute mismatch");

            assertEquals(targetPlayer.getCurrentLocation(),currentTile, "attribute mismatch");
            assertEquals(targetPlayer.getOccupationCard(),occupation, "attribute mismatch");
            assertEquals(targetPlayer.getMaritalStatus().toInt(),martialStatus, "attribute mismatch");
            assertEquals(targetPlayer.getHouseCards(),houses,"attribute mismatch");
        }
        catch (Exception ex){
            fail(ex);
        }





    }

}