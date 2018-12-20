package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpinToWinStatesTest {
    private static final int NUM_PLAYERS = 2;
    private final int CHOICE_INDEX = 0;

    @Test
    void testSpinToWinMinimumNumberOfPlayers(){
        GameLogic gameLogic = configureSpinToWinStateTest();

        Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

        // Must store initial balances of players here to compare
        // against after the SpinToWin tile has been processed
        ArrayList<Integer> initialPlayerBalances = new ArrayList<>();

        int numberOfPlayers = gameLogic.getNumberOfPlayers();

        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
            int playerMoney = gameLogic.getPlayerByIndex(playerIndex).getCurrentMoney();
            initialPlayerBalances.add(playerMoney);
        }

        // Send back ACK
        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        // Should now be a request for the current player to spin twice
        // Therefore we require a LargeDecisionRequest to be received from the logic here
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        // Ensure current player is being prompted for the FIRST of two numbers
        assertEquals(currentPlayerUnderTest.getPlayerNumber(), ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());

        HashMap<Integer, ArrayList<Integer>> playerIndexChoiceMap = new HashMap<>();
        playerIndexChoiceMap.put(0, new ArrayList<>());
        playerIndexChoiceMap.put(1, new ArrayList<>());

        ArrayList<Chooseable> numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick the first number contained in the valid choices
        int chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        playerIndexChoiceMap.get(0).add(chosenNumber);

        // Must send back a LargeDecisionResponse, containing
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                                                     LifeGameMessageTypes.LargeDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        // Ensure current player is being prompted for the SECOND of two numbers
        assertEquals(currentPlayerUnderTest.getPlayerNumber(), ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());
        numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick a number contained in the valid choices
        chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        playerIndexChoiceMap.get(0).add(chosenNumber);

        // Must send back a LargeDecisionResponse, containing
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                                                     LifeGameMessageTypes.LargeDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        int currentPlayerIndex = gameLogic.getCurrentPlayerIndex();
        int nextPlayerIndex = gameLogic.getNextPlayerIndex(currentPlayerIndex);

        // Must ensure that the next player is being prompted for their ONLY choice
        assertEquals(nextPlayerIndex, ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());
        numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick the first number contained in the valid choices
        chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        // Add this choice to the numbers associated with player 2 (index 1)
        playerIndexChoiceMap.get(1).add(chosenNumber);

        // Must send back a LargeDecisionResponse, containing
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                LifeGameMessageTypes.LargeDecisionResponse);

        // Set spinner to be random number between 1 and 10, inclusive, as in the actual game
        gameLogic.setSpinner(new Spinner());

        boolean chosenNumberHasBeenSpun = false;
        int numberOfSpinToWinIterations = 0;
        int winningPlayerIndex = 0;

        // SpinRequest in
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());


        while(!chosenNumberHasBeenSpun) {
            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);

            // SpinResult in
            messageFromLogic = gameLogic.handleInput(messageToLogic);
            assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType());

            // Retrieve the number spun
            int numberSpun = ((SpinResultMessage) messageFromLogic).getSpinResult();

            int winningIndex = parseSpinResult(numberSpun, playerIndexChoiceMap);

            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);

            // Either an AckRequest comes in if someone has won
            // Or a SpinRequest comes in if no one has won yet
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            switch (messageFromLogic.getLifeGameMessageType()) {
                case AckRequest:
                    chosenNumberHasBeenSpun = true;
                    winningPlayerIndex = winningIndex;
                    break;
                case SpinRequest:
                    break;
            }

            numberOfSpinToWinIterations++;
        }

        // Must check that the player who won has had their balance updated by the
        // appropriate amount
        int winningPlayerInitialBalance = initialPlayerBalances.get(winningPlayerIndex);
        int initialBalancePlusExpectedWinnings = winningPlayerInitialBalance + GameConfig.spin_to_win_prize_money;
        int winningPlayerSubsequentBalance = gameLogic.getPlayerByIndex(winningPlayerIndex).getCurrentMoney();

        assertEquals(initialBalancePlusExpectedWinnings, winningPlayerSubsequentBalance);

        // Ensure that the other player's balance has not changed
        int losingPlayerIndex = (winningPlayerIndex + 1) % 2;
        int losingPlayerInitialBalance = initialPlayerBalances.get(losingPlayerIndex);
        int losingPlayerSubsequentBalance = gameLogic.getPlayerByIndex(losingPlayerIndex).getCurrentMoney();

        assertEquals(losingPlayerInitialBalance, losingPlayerSubsequentBalance);
    }

    @SuppressWarnings("Duplicates")
    @Test
    void testSpinToWinMinimumNumberOfPlayersIncorrectInputs(){
        GameLogic gameLogic = configureSpinToWinStateTest();

        Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

        // Must store initial balances of players here to compare
        // against after the SpinToWin tile has been processed
        ArrayList<Integer> initialPlayerBalances = new ArrayList<>();

        int numberOfPlayers = gameLogic.getNumberOfPlayers();

        for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
            int playerMoney = gameLogic.getPlayerByIndex(playerIndex).getCurrentMoney();
            initialPlayerBalances.add(playerMoney);
        }

        // Send back ACK
        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        // Should now be a request for the current player to spin twice
        // Therefore we require a LargeDecisionRequest to be received from the logic here
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        // Ensure current player is being prompted for the FIRST of two numbers
        assertEquals(currentPlayerUnderTest.getPlayerNumber(), ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());

        HashMap<Integer, ArrayList<Integer>> playerIndexChoiceMap = new HashMap<>();
        playerIndexChoiceMap.put(0, new ArrayList<>());
        playerIndexChoiceMap.put(1, new ArrayList<>());

        ArrayList<Chooseable> numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick the first number contained in the valid choices
        int chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        playerIndexChoiceMap.get(0).add(chosenNumber);

        // Send back an incorrect LargeDecisionResponse, containing
        int oversizedChoiceIndex = 10;

        messageToLogic = new DecisionResponseMessage(oversizedChoiceIndex,
                LifeGameMessageTypes.LargeDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        // Attempt to recover from incorrect size sent
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                LifeGameMessageTypes.LargeDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());


        // Ensure current player is being prompted for the SECOND of two numbers
        assertEquals(currentPlayerUnderTest.getPlayerNumber(), ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());
        numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick a number contained in the valid choices
        chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        playerIndexChoiceMap.get(0).add(chosenNumber);

        // Must send back a LargeDecisionResponse, containing
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                LifeGameMessageTypes.LargeDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.LargeDecisionRequest, messageFromLogic.getLifeGameMessageType());

        int currentPlayerIndex = gameLogic.getCurrentPlayerIndex();
        int nextPlayerIndex = gameLogic.getNextPlayerIndex(currentPlayerIndex);

        // Must ensure that the next player is being prompted for their ONLY choice
        assertEquals(nextPlayerIndex, ((DecisionRequestMessage) messageFromLogic).getRelatedPlayer());
        numberChoices = ((DecisionRequestMessage) messageFromLogic).getChoices();

        // Pick the first number contained in the valid choices
        chosenNumber = Integer.parseInt(numberChoices.get(0).displayChoiceDetails());
        // Add this choice to the numbers associated with player 2 (index 1)
        playerIndexChoiceMap.get(1).add(chosenNumber);

        // Must send back a LargeDecisionResponse, containing
        messageToLogic = new DecisionResponseMessage(CHOICE_INDEX,
                LifeGameMessageTypes.LargeDecisionResponse);

        // Set spinner to be random number between 1 and 10, inclusive, as in the actual game
        gameLogic.setSpinner(new Spinner());

        boolean chosenNumberHasBeenSpun = false;
        int numberOfSpinToWinIterations = 0;
        int winningPlayerIndex = 0;

        // SpinRequest in
        messageFromLogic = gameLogic.handleInput(messageToLogic);
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());


        while(!chosenNumberHasBeenSpun) {
            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);

            // SpinResult in
            messageFromLogic = gameLogic.handleInput(messageToLogic);
            assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType());

            // Retrieve the number spun
            int numberSpun = ((SpinResultMessage) messageFromLogic).getSpinResult();

            int winningIndex = parseSpinResult(numberSpun, playerIndexChoiceMap);

            messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);

            // Either an AckRequest comes in if someone has won
            // Or a SpinRequest comes in if no one has won yet
            messageFromLogic = gameLogic.handleInput(messageToLogic);

            switch (messageFromLogic.getLifeGameMessageType()) {
                case AckRequest:
                    chosenNumberHasBeenSpun = true;
                    winningPlayerIndex = winningIndex;
                    break;
                case SpinRequest:
                    break;
            }

            numberOfSpinToWinIterations++;
        }

        // Must check that the player who won has had their balance updated by the
        // appropriate amount
        int winningPlayerInitialBalance = initialPlayerBalances.get(winningPlayerIndex);
        int initialBalancePlusExpectedWinnings = winningPlayerInitialBalance + GameConfig.spin_to_win_prize_money;
        int winningPlayerSubsequentBalance = gameLogic.getPlayerByIndex(winningPlayerIndex).getCurrentMoney();

        assertEquals(initialBalancePlusExpectedWinnings, winningPlayerSubsequentBalance);

        // Ensure that the other player's balance has not changed
        int losingPlayerIndex = (winningPlayerIndex + 1) % 2;
        int losingPlayerInitialBalance = initialPlayerBalances.get(losingPlayerIndex);
        int losingPlayerSubsequentBalance = gameLogic.getPlayerByIndex(losingPlayerIndex).getCurrentMoney();

        assertEquals(losingPlayerInitialBalance, losingPlayerSubsequentBalance);
    }

    private static GameLogic configureSpinToWinStateTest(){
        String priorToSpinToWinTileLocation = "v";

        GameLogic spinToWinPrimedGameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);

        spinToWinPrimedGameLogic.getCurrentPlayer().setCurrentLocation(new BoardLocation(priorToSpinToWinTileLocation));

        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage messageFromLogic = spinToWinPrimedGameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");

        return spinToWinPrimedGameLogic;
    }

    private int parseSpinResult(int spinResult, HashMap<Integer, ArrayList<Integer>> indexToChoicesMapping){
        for(HashMap.Entry<Integer,ArrayList<Integer>> kvp : indexToChoicesMapping.entrySet()) {
            if(kvp.getValue().contains(spinResult)) {
                return kvp.getKey();
            }
        }
        return -1;
    }
}