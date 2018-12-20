package ie.ucd.engac.lifegamelogic.playerlogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        int playerNumber = 1;
        player = new Player(playerNumber);
    }

    @AfterEach
    void tearDown() {
        player = null;
    }

    @Test
    void computeRetirementBonuses() {
        int max = 3; //give them 3 of each item
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
    void computeRetirementBonusForNonPlayer(){
        final int negativeNumberOfPlayersAlreadyRetired = -1;
        RuntimeException invalidNumber = assertThrows(RuntimeException.class, () -> player.computeRetirementBonuses(negativeNumberOfPlayersAlreadyRetired),"Tried retiring with negative param & it did not error.");

        final int largeNumberOfPlayersAlreadyRetired = GameConfig.max_num_players;
        invalidNumber = assertThrows(RuntimeException.class, () -> player.computeRetirementBonuses(largeNumberOfPlayersAlreadyRetired),"Tried retiring too large param & it did not error.");
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
        assertEquals(4L, 3.3, 3,"er");
    }

    @Test
    void sellHouseCard() {
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(1,1);
        HouseCard houseCard = gameLogic.getTopHouseCard();
        int initMoney = player.getCurrentMoney();
        //test sale of odd number
        player.addHouseCard(houseCard);
        HouseCard soldHouseCard = player.sellHouseCard(0,1);
        assertEquals(0,player.getNumberOfHouseCards(),"player still has house card");
        assertEquals(houseCard,soldHouseCard,"house card sold doesnt match assigned");

        int expectedMoney = initMoney+houseCard.getSpinForSalePrice(true);
        assertEquals(expectedMoney,player.getCurrentMoney(),"odd: incorrect money");
        //test sale of even number
        player.addHouseCard(houseCard);
        player.sellHouseCard(0,2);
        assertEquals(0,player.getNumberOfHouseCards(),"player still has house card");
        assertEquals(houseCard,soldHouseCard,"house card sold doesnt match assigned");
        expectedMoney = expectedMoney+houseCard.getSpinForSalePrice(false);
        assertEquals(expectedMoney,player.getCurrentMoney(),"even: incorrect money");
    }

    @Test
    void sellInvalidHouseCard() {
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(1,1);
        HouseCard houseCard = gameLogic.getTopHouseCard();
        int initMoney = player.getCurrentMoney();

        //test no cards in possession
        int intHCards = player.getNumberOfHouseCards();
        HouseCard soldHouseCard = player.sellHouseCard(0,1);
        assertEquals(intHCards,player.getNumberOfHouseCards(),"player sold house card erroneously");
        assertEquals(initMoney,player.getCurrentMoney(),"incorrect money");
        assertNull(soldHouseCard,"sold house card not null");

        //test below range
        player.addHouseCard(houseCard);
        intHCards = player.getNumberOfHouseCards();
        soldHouseCard = player.sellHouseCard(-1,1);
        assertEquals(intHCards,player.getNumberOfHouseCards(),"player sold house card erroneously");
        assertEquals(initMoney,player.getCurrentMoney(),"incorrect money");
        assertNull(soldHouseCard,"sold house card not null");

        //test below range
        player.addHouseCard(houseCard);
        intHCards = player.getNumberOfHouseCards();
        soldHouseCard = player.sellHouseCard(2,1);
        assertEquals(intHCards,player.getNumberOfHouseCards(),"player sold house card erroneously");
        assertEquals(initMoney,player.getCurrentMoney(),"incorrect money");
        assertNull(soldHouseCard,"sold house card not null");
    }

}