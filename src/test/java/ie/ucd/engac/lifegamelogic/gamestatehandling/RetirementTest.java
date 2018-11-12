package ie.ucd.engac.lifegamelogic.gamestatehandling;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.EndGameMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetirementTest {
    private static final int NUM_PLAYERS = 4;
    @Test
    void testRetirement(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        int ret;
        ArrayList<Integer> maxes = new ArrayList<Integer>();
        for (int i=0;i<NUM_PLAYERS;i++){
            ret = setupPlayerWithItems(gameLogic);
            gameLogic.setNextPlayerToCurrent();
            maxes.add(ret);
        }
        //TODO add loans
        ArrayList<Integer> endBalances = new ArrayList<>();
        for (int i=0;i<NUM_PLAYERS;i++){
            ret = retirePlayer(gameLogic, i,maxes.get(i));
            endBalances.add(ret);
        }
        Collections.sort(endBalances);

        //advance to end of game to get ranked players
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        assertEquals(LifeGameMessageTypes.EndGameMessage, responseMessage.getLifeGameMessageType());
        EndGameMessage endGameMessage = (EndGameMessage) responseMessage;

        //compare game rankings with test rankings, ensures final money and order is correct
        ArrayList<Player> rankedPlayers = endGameMessage.getRankedPlayers();
        for (int i=0;i<NUM_PLAYERS;i++){
            assertEquals((int)endBalances.get(i),rankedPlayers.get(i).getCurrentMoney());
        }
    }

    private int setupPlayerWithItems(GameLogic gameLogic){
        String location = "aaaav";
        Random random = new Random(System.nanoTime());
        int max = random.nextInt(4);
        Player player = gameLogic.getCurrentPlayer();
        for (int i=0;i<max;i++){
            player.addHouseCard(gameLogic.getTopHouseCard());
            player.addActionCard(gameLogic.getTopActionCard());
            player.addDependants(1);
        }
        player.setCurrentLocation(new BoardLocation(location));
        return max;
    }

    private int retirePlayer(GameLogic gameLogic, int numRetiredAlready, int max){
        //get player money, cards, houses, dependants
        Player player = gameLogic.getCurrentPlayer();
        int initMoney = player.getCurrentMoney();
        int numActionCards = player.getNumberOfActionCards();
        int numDependants = player.getNumberOfDependants();
        ArrayList<HouseCard> houseCards = player.getHouseCards();

        // Mock messages to logic, performing move functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        int testCost;
        int testMoney = initMoney;
        System.out.println("max" + max);
        for(int i=0;i<max;max--){
            //perform computation in test
            testCost = houseCards.get(i).getSpinForSalePrice(true); //true as set spin to 1
            testMoney += testCost;

            //perform computation in logic
            assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());
            // Mock messages to logic, performing  functionality
            initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
        }
        assertEquals(0,player.getNumberOfHouseCards());
        //card should be sold by no so do the maths on the players money
        testMoney += (NUM_PLAYERS-numRetiredAlready)*GameConfig.ret_bonus_remaining;
        testMoney += numActionCards*GameConfig.ret_bonus_action;
        testMoney += (numDependants-1)*GameConfig.ret_bonus_kids;
        assertEquals(testMoney,player.getCurrentMoney());

        //check that next player is ready to go
        assertTrue(responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckRequest || responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.EndGameMessage);
        if(responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckRequest){
            assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());
            initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());
        }

        return testMoney;
    }
}
