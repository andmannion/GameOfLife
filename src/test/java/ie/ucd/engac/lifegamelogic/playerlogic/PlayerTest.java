package ie.ucd.engac.lifegamelogic.playerlogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
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
    void addDependants() {
        assertEquals(0,player.getNumberOfDependants(),"init with >0 dependants");
        player.addDependants(-1);
        assertEquals(0,player.getNumberOfDependants(),"added negative number erroneously");
        player.addDependants(1);
        assertEquals(1,player.getNumberOfDependants(),"init with >0 dependants");
        player.addDependants(3);
        assertEquals(4,player.getNumberOfDependants(),"init with >0 dependants");
    }

    @Test
    void sellHouseCard() {
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(1,1);
        HouseCard houseCard = gameLogic.getTopHouseCard();
        int initMoney = player.getCurrentMoney();
        //test sale of odd number
        player.addHouseCard(houseCard);
        player.sellHouseCard(0,1);
        assertEquals(0,player.getNumberOfHouseCards(),"player still has house card");
        int expectedMoney = initMoney+houseCard.getSpinForSalePrice(true);
        assertEquals(expectedMoney,player.getCurrentMoney(),"odd: incorrect money");
        //test sale of even number
        player.addHouseCard(houseCard);
        player.sellHouseCard(0,2);
        assertEquals(0,player.getNumberOfHouseCards(),"player still has house card");
        expectedMoney = expectedMoney+houseCard.getSpinForSalePrice(false);
        assertEquals(expectedMoney,player.getCurrentMoney(),"even: incorrect money");
    }

}