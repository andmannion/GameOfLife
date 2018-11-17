package ie.ucd.engac.lifegamelogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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

    @Test
    void getShadowPlayerTest(){

    }

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

    @Test
    void retireAPlayer(){

    }

}