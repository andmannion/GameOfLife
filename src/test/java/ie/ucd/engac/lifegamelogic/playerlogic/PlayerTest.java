package ie.ucd.engac.lifegamelogic.playerlogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private final int playerNumber = 1;

    @BeforeEach
    void setUp() {
        player = new Player(playerNumber);
    }

    @AfterEach
    void tearDown() {
        player = null;
    }

    @Test
    void getPlayerNumber() {
        assertEquals(playerNumber,player.getPlayerNumber(), "Player number dont match");
    }

    @Test
    void getPlayerColour() {
        assertEquals(PlayerColour.fromInt(playerNumber),player.getPlayerColour(),"Play colour doesnt match");
    }

    @Test
    void computeRetirementBonuses() {
        Random random = new Random(System.nanoTime());
        int max = random.nextInt(4);
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(1,1);

        for (int i=0;i<max;i++){
            player.addHouseCard(gameLogic.getTopHouseCard());
            player.addActionCard(gameLogic.getTopActionCard());
            player.addDependants(1);
            gameLogic.takeOutALoan(player.getPlayerNumber());
        }

        for (int inc=0;inc<4;inc++) {
            int bonus = player.computeRetirementBonuses(inc);
            int calc_bonus = (4-inc)*GameConfig.ret_bonus_remaining;
            calc_bonus += max*GameConfig.ret_bonus_action;
            calc_bonus += (max-1)*GameConfig.ret_bonus_kids; //minus 1 for wife
            assertEquals(calc_bonus, bonus,"Retirement bonus calculation incorrect");
        }
    }

    @Test
    void setgetCurrentLocation() { //TODO setter test in more detail
        String location = "aa";
        player.setCurrentLocation(new BoardLocation(location));
        BoardLocation boardLocation = player.getCurrentLocation();
        assertEquals(location, boardLocation.getLocation(),"Location does not match");

    }

    @Test
    void getNumberOfDependants() {
        //player init with 0 dependants
        int numberOfDependants = player.getNumberOfDependants();
        assertEquals(0,numberOfDependants,"init dependants not zero");
        for (int inc = 0;inc<10;inc++){
            player.addDependants(1);
            numberOfDependants = player.getNumberOfDependants();
            assertEquals(inc+1,numberOfDependants,"Number of dependants not get correctly");
        }
    }

    @Test
    void addDependants() {
    }

    @Test
    void getCareerPath() {
    }

    @Test
    void setCareerPath() {
    }

    @Test
    void getOccupationCard() {
    }

    @Test
    void setOccupationCard() {
    }

    @Test
    void getCurrentMoney() {
        int currentMoney = player.getCurrentMoney();
        int incMoney = 100;
        assertEquals(GameConfig.starting_money,currentMoney,"init money incorrect");
        player.addToBalance(incMoney);
        currentMoney = player.getCurrentMoney();
        assertEquals(GameConfig.starting_money+incMoney,currentMoney,"added money incorrect");
        player.subtractFromBalance(2*incMoney);
        currentMoney = player.getCurrentMoney();
        assertEquals(GameConfig.starting_money-incMoney,currentMoney,"subtracted money incorrect");
        player.subtractFromBalance(GameConfig.starting_money-incMoney);
        currentMoney = player.getCurrentMoney();
        assertEquals(0,currentMoney,"zero money incorrect");
    }

    @Test
    void addToBalance() {
    }

    @Test
    void subtractFromBalance() {
    }

    @Test
    void getNumberOfLoans() {
    }

    @Test
    void getTotalLoansOutstanding() {
    }

    @Test
    void addActionCard() {
    }

    @Test
    void getActionCards() {
    }

    @Test
    void getNumberOfActionCards() {
    }

    @Test
    void getHouseCards() {
    }

    @Test
    void addHouseCard() {
    }

    @Test
    void getNumberOfHouseCards() {
    }

    @Test
    void sellHouseCard() {
    }

    @Test
    void setMaritalStatus() {
    }

    @Test
    void getMaritalStatus() {
    }

    @Test
    void getPendingBoardForkChoice() {
    }

    @Test
    void setPendingBoardForkChoice() {
    }

    @Test
    void getMovesRemaining() {
    }

    @Test
    void setMovesRemaining() {
    }
}