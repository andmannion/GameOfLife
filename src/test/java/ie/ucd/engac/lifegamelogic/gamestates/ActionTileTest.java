package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.GetCashFromBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.PayTheBankActionCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTileTest {
    private static final int NUM_PLAYERS = 4;

    @RepeatedTest(33)
    void testActionTile(){
        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        String priorTile = "c";
        String actionTile = "d";

        Player player = gameLogic.getCurrentPlayer();
        player.setCurrentLocation(new BoardLocation(priorTile));

        player.setOccupationCard(gameLogic.getTopCollegeCareerCard());

        OccupationCard occupationCard = player.getOccupationCard();
        OccupationCardTypes occupationCardType = occupationCard.getOccupationCardType();
        int numberOfActionCards = player.getNumberOfActionCards();
        int initMoney = player.getCurrentMoney();

        //assert preconditions
        assertEquals(0, numberOfActionCards);

        // Mock messages to logic, performing turn functionality
        LifeGameMessage initialMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage responseMessage = gameLogic.handleInput(initialMessage);

        LifeGameMessageTypes lifeGameMessageType = responseMessage.getLifeGameMessageType();

        assertTrue(lifeGameMessageType == LifeGameMessageTypes.AckRequest || lifeGameMessageType == LifeGameMessageTypes.LargeDecisionRequest || lifeGameMessageType == LifeGameMessageTypes.OptionDecisionRequest);
        switch (lifeGameMessageType){
            case AckRequest: //simple type
                int actionCardBalanceChange = simpleActionCardTest(player);
                int finalMoney = initMoney + actionCardBalanceChange;
                assertEquals(finalMoney,player.getCurrentMoney(), "failed simple card type");
                break;
            case OptionDecisionRequest: //career change
                careerChangeCardTest(gameLogic);
                assertEquals(occupationCardType,player.getOccupationCard().getOccupationCardType());

                CollegeCareerCard oldCard = ((CollegeCareerCard)occupationCard);
                CollegeCareerCard newCard = ((CollegeCareerCard)gameLogic.getPlayerByIndex(0).getOccupationCard());

                assertNotSame(oldCard, newCard, "did not assign new career card");

                break;
            case LargeDecisionRequest:
                playersPayCardTest(gameLogic);
                break;
            default:
                break;
        }

        assertEquals(actionTile, gameLogic.getPlayerByIndex(0).getCurrentLocation().getLocation());
        assertEquals(numberOfActionCards+1,player.getNumberOfActionCards(),"ensure player has 1 more a card");
    }

    private void playersPayCardTest(GameLogic gameLogic) {
        Random random = new Random(System.nanoTime());
        int choiceIndex = random.nextInt(NUM_PLAYERS-1); //generate index to choose
        int chosenTargetIndex = choiceIndex+1; //we know that the chosen player index is choice+1 as the p.u.t. is 0

        //save balances before
        int targetPlayerInitBalance = gameLogic.getPlayerByIndex(chosenTargetIndex).getCurrentMoney();
        int playerInitBalance = gameLogic.getCurrentPlayer().getCurrentMoney();

        LifeGameMessage messageToLogic = new DecisionResponseMessage(choiceIndex, LifeGameMessageTypes.LargeDecisionResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        //check transaction
        int targetPlayerEndBalance = gameLogic.getPlayerByIndex(chosenTargetIndex).getCurrentMoney();
        int playerEndBalance = gameLogic.getCurrentPlayer().getCurrentMoney();

        assertEquals(playerInitBalance + GameConfig.players_pay_amount, playerEndBalance);
        assertEquals(targetPlayerInitBalance - GameConfig.players_pay_amount, targetPlayerEndBalance);

    }


    private void careerChangeCardTest(GameLogic gameLogic){ //TODO duplicate code from NightSchoolSetup/Graduation
        int firstCollegeCareerCardChoice = 0;
        // Just choose the first CollegeCareerCard
        LifeGameMessage messageToLogic = new DecisionResponseMessage(firstCollegeCareerCardChoice,LifeGameMessageTypes.OptionDecisionResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        //check correct message was received
        assertEquals(LifeGameMessageTypes.AckRequest,messageFromLogic.getLifeGameMessageType());

    }

    private int simpleActionCardTest(Player player){
        ActionCard actionCard = player.getActionCards().get(0);
        switch (actionCard.getActionCardType()){
            case PayTheBank:
                return -((PayTheBankActionCard) actionCard).getValue();
            case GetCashFromBank:
                return ((GetCashFromBankActionCard) actionCard).getAmountToPay();
            default:
                return 0;
        }
    }
}
