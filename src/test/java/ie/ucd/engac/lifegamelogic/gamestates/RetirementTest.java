package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.EndGameMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetirementTest {
    private static final int NUM_PLAYERS = 4;
    @Test
    void testRetirement(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        int ret;
        ArrayList<Integer> maxes = new ArrayList<>();
        for (int i=0;i<NUM_PLAYERS;i++){
            ret = setupPlayerWithItems(gameLogic);
            gameLogic.setNextPlayerToCurrent();
            maxes.add(ret);
        }
        ArrayList<Integer> endBalances = new ArrayList<>();
        int max;
        for (int i=0;i<NUM_PLAYERS;i++){
            max = maxes.get(i);
            ret = retirePlayer(gameLogic, i, max);
            endBalances.add(ret);
        }
        endBalances.sort(Collections.reverseOrder());

        //advance to end of game to get ranked players
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        assertEquals(LifeGameMessageTypes.EndGameMessage, responseMessage.getLifeGameMessageType(),"Expected message not received");
        EndGameMessage endGameMessage = (EndGameMessage) responseMessage;

        //compare game rankings with test rankings, ensures final money and order is correct
        ArrayList<Player> rankedPlayers = endGameMessage.getRankedPlayers();
        for (int i=0;i<NUM_PLAYERS;i++){
            assertEquals((int)endBalances.get(i),rankedPlayers.get(i).getCurrentMoney());
        }
    }

    private int setupPlayerWithItems(GameLogic gameLogic){
        String location = "aaaav";
        int max = 3; //give them 3 of each item
        Player player = gameLogic.getCurrentPlayer();
        for (int i=0;i<max;i++){
            player.addHouseCard(gameLogic.getTopHouseCard());
            player.addActionCard(gameLogic.getTopActionCard());
            player.addDependants(1);
            gameLogic.takeOutALoan(player.getPlayerNumber());
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
        int numLoans = gameLogic.getNumberOfLoans(player.getPlayerNumber());
        ArrayList<HouseCard> houseCards = player.getHouseCards();

        // Mock messages to logic, performing move functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);
        assertEquals(LifeGameMessageTypes.SpinResult, responseMessage.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        responseMessage = gameLogic.handleInput(spinMessage);

        int testCost;
        int testMoney = initMoney;

        for(int i=0;i<max;max--){
            //perform computation in test
            testCost = houseCards.get(i).getSpinForSalePrice(true); //true as set spin to 1
            testMoney += testCost;

            //perform computation in logic
            assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");
            // Mock messages to logic, performing  functionality
            initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(LifeGameMessageTypes.SpinResult, responseMessage.getLifeGameMessageType(),"Expected message not received");
            initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
        }
        assertEquals(0,player.getNumberOfHouseCards());
        //card should be sold by now so do the maths on the players money, adding the relevant bonuses
        testMoney += (NUM_PLAYERS-numRetiredAlready)*GameConfig.ret_bonus_remaining;
        testMoney += numActionCards*GameConfig.ret_bonus_action;
        testMoney += (numDependants-1)*GameConfig.ret_bonus_kids;
        testMoney -= numLoans*GameConfig.repayment_amount;
        assertEquals(testMoney,player.getCurrentMoney());

        //check that next player is ready to go
        assertTrue(responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckRequest || responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.EndGameMessage);
        if(responseMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckRequest){
            assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");
            initialMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType(),"Expected message not received");
        }

        return testMoney;
    }
}
