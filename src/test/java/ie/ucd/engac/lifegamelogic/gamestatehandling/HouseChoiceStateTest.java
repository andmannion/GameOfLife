package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
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

        public static GameLogic setupTestGenericPreconditions(LogicGameBoard gameBoard, int numberOfPlayers, Spinnable spinner){
            //setup object with non functional spinner
            GameLogic gameLogic = new GameLogic(gameBoard, numberOfPlayers, new TestSpinner(0));

            LifeGameMessage initialMessage;
            LifeGameMessage responseMessage;
            LifeGameMessage choiceMessage;
            LifeGameMessage spinMessage;
            LifeGameMessage ackMessage;

            initialMessage = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
            responseMessage = gameLogic.handleInput(initialMessage);

            while (gameLogic.getNumberOfUninitialisedPlayers() > 0) {

                assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.OptionDecisionRequest);

                //choose a path for this player
                int choiceIndex = PathChoiceState.COLLEGE_CAREER_CHOICE_INDEX;
                choiceMessage = new DecisionResponseMessage(choiceIndex);

                responseMessage = gameLogic.handleInput(choiceMessage);
                assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.SpinRequest);

                //send back a spin response
                spinMessage = new SpinResponseMessage();
                responseMessage = gameLogic.handleInput(spinMessage);
                assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.AckRequest);

                ackMessage = new AckResponseMessage();
                responseMessage = gameLogic.handleInput(ackMessage);
            }


            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.SpinRequest);

            //set object to use the function spinner
            gameLogic.setSpinner(spinner);
            return gameLogic;
        }

        @Test
        void testNoAction() {
            // Set up test
            LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
            Spinnable testSpinner = new TestSpinner(1);
            GameLogic gameLogic = setupTestGenericPreconditions(gameBoard, NUM_PLAYERS, testSpinner);

            // Assert preconditions
            OccupationCard occupationCard = gameLogic.getPlayerByIndex(0).getOccupationCard();
            int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
            CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
            MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();


            //TODO somehow be waiting for a HandlePlayerMoveState
            Player player = gameLogic.getCurrentPlayer();
            player.addToBalance(1000000); //ensure player can buy, we are not testing loans

            int playerInitMoney = player.getCurrentMoney();

            player.setCurrentLocation(new BoardLocation("ab"));

            // Mock messages to logic, performing pathChoiceState functionality
            LifeGameMessage initialMessage = new SpinResponseMessage();
            LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

            assertEquals(LifeGameMessageTypes.LargeDecisionRequest, responseMessage.getLifeGameMessageType());

            // Provide mock UI response
            int choiceIndex = 0; //do nothing
            initialMessage = new LargeDecisionResponseMessage(choiceIndex);

            responseMessage = gameLogic.handleInput(initialMessage);
            assertEquals(responseMessage.getLifeGameMessageType(), LifeGameMessageTypes.AckRequest);

            // Assert that player 0 has been rxed no cards
            assertEquals(player.getHouseCards().size(), 0);
            assertEquals(player.getCurrentMoney(), playerInitMoney);

            assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS);
            assertEquals(gameLogic.getPlayerByIndex(0).getOccupationCard(), occupationCard);
            assertEquals(gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation(), "ac");
            assertEquals(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice(), null);
            assertEquals(gameLogic.getPlayerByIndex(0).getNumberOfActionCards(), numberOfActionCards);
            assertEquals(gameLogic.getPlayerByIndex(0).getCareerPath(), careerPath);
            assertEquals(gameLogic.getPlayerByIndex(0).getMaritalStatus(), maritalStatus);
        }

        /*@Test
        void testHousePurchaseNoLoan(){
            //PART 2 - CHOOSING A HOUSE
            // Set up test
            LogicGameBoard gameBoard = new LogicGameBoard(GameEngine.LOGIC_BOARD_CONFIG_FILE_LOCATION);
            Spinnable testSpinner = new TestSpinner(1);
            GameLogic gameLogic = new GameLogic(gameBoard, NUM_PLAYERS, testSpinner);
            while (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                gameLogic.decrementNumberOfUninitialisedPlayers();
            }

            OccupationCard occupationCard = gameLogic.getPlayerByIndex(0).getOccupationCard();
            int numberOfActionCards = gameLogic.getPlayerByIndex(0).getNumberOfActionCards();
            CareerPathTypes careerPath = gameLogic.getPlayerByIndex(0).getCareerPath();
            MaritalStatus maritalStatus = gameLogic.getPlayerByIndex(0).getMaritalStatus();


            //TODO somehow be waiting for a HandlePlayerMoveState
            Player player = gameLogic.getCurrentPlayer();
            player.addToBalance(1000000); //ensure player can buy, we are not testing loans

            // Assert preconditions
            assertEquals(player.getHouseCards().size(), 0);

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

            HouseCard playerHouseCard = player.getHouseCards().get(0);

            // Assert that player 0 has been rxed a card, that it is the correct one & the correct $ deducted
            assertEquals(player.getHouseCards().size(), 1);
            assertEquals(playerHouseCard, houseCard);
            assertEquals(player.getCurrentMoney(), playerInitMoney-houseCost);

            //check that nothing else has changed
            assertEquals(gameLogic.getNumberOfUninitialisedPlayers(), NUM_PLAYERS);
            assertEquals(gameLogic.getPlayerByIndex(0).getOccupationCard(), occupationCard);
            assertEquals(gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation(), "ac");
            assertEquals(gameLogic.getPlayerByIndex(0).getPendingBoardForkChoice(), null);
            assertEquals(gameLogic.getPlayerByIndex(0).getNumberOfActionCards(), numberOfActionCards);
            assertEquals(gameLogic.getPlayerByIndex(0).getCareerPath(), careerPath);
            assertEquals(gameLogic.getPlayerByIndex(0).getMaritalStatus(), maritalStatus);

        }*/
    }