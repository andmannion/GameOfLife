package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.CareerPathTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HouseChoiceStateTest {
        private final int NUM_PLAYERS = 2;

        @Test
        void testNoAction() {
            // Set up test
            LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
            Spinnable testSpinner = new TestSpinner(1);
            GameLogic gameLogic = new GameLogic(gameBoard, NUM_PLAYERS, testSpinner);
            while (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                gameLogic.decrementNumberOfUninitialisedPlayers();
            }

            // Assert preconditions


            //TODO somehow be waiting for a HandlePlayerMoveState
            Player player = gameLogic.getCurrentPlayer();
            player.addToBalance(1000000); //ensure player can buy, we are not testing loans

            int playerInitMoney = player.getCurrentMoney();

            player.setCurrentLocation(new BoardLocation("ab"));

            // Mock messages to logic, performing pathChoiceState functionality
            LifeGameMessage initialMessage = new SpinResponseMessage();
            LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.LargeDecisionRequest);

            // Provide mock UI response
            int choiceIndex = 0; //do nothing
            initialMessage = new LargeDecisionResponseMessage(choiceIndex);

            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.AckRequest);

            // Assert that player 0 has been rxed no cards
            assertEquals(player.getHouseCards().size(), 0);
            assertEquals(player.getCurrentMoney(), playerInitMoney);
        }

        @Test
        void testHousePurchaseNoLoan(){
            //PART 2 - CHOOSING A HOUSE
// Set up test
            LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
            Spinnable testSpinner = new TestSpinner(1);
            GameLogic gameLogic = new GameLogic(gameBoard, NUM_PLAYERS, testSpinner);
            while (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                gameLogic.decrementNumberOfUninitialisedPlayers();
            }

            // Assert preconditions


            //TODO somehow be waiting for a HandlePlayerMoveState
            Player player = gameLogic.getCurrentPlayer();
            player.addToBalance(1000000); //ensure player can buy, we are not testing loans

            int playerInitMoney = player.getCurrentMoney();

            player.setCurrentLocation(new BoardLocation("ab"));

            // Mock messages to logic, performing pathChoiceState functionality
            LifeGameMessage initialMessage = new SpinResponseMessage();
            LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.LargeDecisionRequest);

            // Provide mock UI response
            int choiceIndex = 1; //buy a house
            initialMessage = new LargeDecisionResponseMessage(choiceIndex);

            responseMessage = gameLogic.handleInput(initialMessage);

            assertEquals(responseMessage.getLifeGameMessageType(),LifeGameMessageTypes.OptionDecisionRequest);

            ArrayList<Card> choices = gameLogic.getPendingCardChoices();
            int cardChoiceIndex = 0;
            HouseCard houseCard = (HouseCard)choices.get(cardChoiceIndex);
            int houseCost = houseCard.getPurchasePrice();
            initialMessage = new LargeDecisionResponseMessage(cardChoiceIndex);

            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.AckRequest);


            // Assert that player 0 has been rxed a carda
            assertEquals(player.getHouseCards().size(), 1);
            assertEquals(player.getCurrentMoney(), playerInitMoney-houseCost);

        }
    }