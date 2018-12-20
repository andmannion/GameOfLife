package ie.ucd.engac.lifegamelogic.cards.actioncards;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionCardDeckTest {
    @Test
    void TestInitialiseDefaultActionCardDeck(){
        int ACTION_DECK_DEFAULT_SIZE = 55;

        TestHelpers.importGameConfig();

        CardDeck<ActionCard> testActionCardDeck = new CardDeck<>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));

        // Must be 55 cards in total in the default deck
        assertEquals(ACTION_DECK_DEFAULT_SIZE, testActionCardDeck.getNumberOfRemainingCards());

        // Must loop through all the cards and ensure correct numbers of each type were present
        int numberOfRemainingCards = testActionCardDeck.getNumberOfRemainingCards();

        ArrayList<ActionCard> actionCardDeckAsList = new ArrayList<>();

        // Get entire deck of cards as list to filter and test
        while(numberOfRemainingCards > 0){
            actionCardDeckAsList.add(testActionCardDeck.popTopCard());
            numberOfRemainingCards--;
        }

        List<CareerChangeActionCard> careerChangeActionCards = actionCardDeckAsList.stream()
                .filter(c -> c.getActionCardType() == ActionCardTypes.CareerChange)
                .map(c -> (CareerChangeActionCard) c)
                .collect(Collectors.toList());

        List<PlayersPayActionCard> playersPayActionCards = actionCardDeckAsList.stream()
                .filter(c -> c.getActionCardType() == ActionCardTypes.PlayersPay)
                .map(c -> (PlayersPayActionCard) c)
                .collect(Collectors.toList());

        List<PayTheBankActionCard> payTheBankActionCards = actionCardDeckAsList.stream()
                .filter(c -> c.getActionCardType() == ActionCardTypes.PayTheBank)
                .map(c -> (PayTheBankActionCard) c)
                .collect(Collectors.toList());

        List<GetCashFromBankActionCard> getCashFromBankActionCards = actionCardDeckAsList.stream()
                .filter(c -> c.getActionCardType() == ActionCardTypes.GetCashFromBank)
                .map(c -> (GetCashFromBankActionCard) c)
                .collect(Collectors.toList());

        AssertCorrectDefaultCareerChangeActionCards(careerChangeActionCards);
        AssertCorrectDefaultPlayersPayActionCards(playersPayActionCards);
        AssertCorrectDefaultPayTheBankActionCards(payTheBankActionCards);
        AssertCorrectDefaultGetCashFromBankActionCards(getCashFromBankActionCards);
    }

    @Test
    void TestRemoveAllCardsWithoutReplacement(){
        TestHelpers.importGameConfig();

        CardDeck<ActionCard> testActionCardDeck = new CardDeck<>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));

        int numberOfCardsRemaining = testActionCardDeck.getNumberOfRemainingCards();

        while(numberOfCardsRemaining-- > 0){
            testActionCardDeck.popTopCard();
        }

        // Ensure that trying to pop an empty ActionCardDeck does not cause an exception to occur
        testActionCardDeck.popTopCard();

        // Ensure that you can add a card to an empty deck and pop it again
        ActionCard testActionCard = new CareerChangeActionCard();

        testActionCardDeck.addCardToBottom(testActionCard);
        assertEquals(1, testActionCardDeck.getNumberOfRemainingCards());

        ActionCard returnedActionCard = testActionCardDeck.popTopCard();
        assertEquals(testActionCard, returnedActionCard);
    }

    @Test
    void TestShuffleDeck(){
        TestHelpers.importGameConfig();

        CardDeck<ActionCard> actionCardTestDeck = new CardDeck<>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));

        // Make sure that no cards have been added or removed by the .shuffle() operation
        int numberOfCardsInDeckBeforeShuffle = actionCardTestDeck.getNumberOfRemainingCards();

        actionCardTestDeck.shuffle();
        int numberOfCardsInDeckAfterShuffle = actionCardTestDeck.getNumberOfRemainingCards();
        assertEquals(numberOfCardsInDeckBeforeShuffle, numberOfCardsInDeckAfterShuffle);

        /* Make sure that the cards popped from the top appear in a relatively
           different order to how they are declared in the configuration file.
        */
        // Create another Deck to shuffle

        CardDeck<ActionCard> secondActionCardTestDeck = new CardDeck<>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));
        secondActionCardTestDeck.shuffle();

        int initialNumberOfCards = actionCardTestDeck.getNumberOfRemainingCards();
        int numMatchingIndices = 0;

        while(initialNumberOfCards-- > 0){
            if(actionCardTestDeck.popTopCard().equals(secondActionCardTestDeck.popTopCard())){
                numMatchingIndices++;
            }
        }

        assertTrue(numMatchingIndices < 10);
    }

    private void AssertCorrectDefaultCareerChangeActionCards(List<CareerChangeActionCard> careerChangeActionCards){
        int DEFAULT_NUM_CAREER_CHANGE_CARDS = 5;

        assertEquals(DEFAULT_NUM_CAREER_CHANGE_CARDS, careerChangeActionCards.size());
    }

    private void AssertCorrectDefaultPlayersPayActionCards(List<PlayersPayActionCard> playersPayActionCards){
        int DEFAULT_NUM_PLAYERS_PAY_CARDS = 10;
        int DEFAULT_PLAYERS_PAY_AMOUNT = 20000;

        assertEquals(DEFAULT_NUM_PLAYERS_PAY_CARDS, playersPayActionCards.size());

        for (PlayersPayActionCard ppAC:
             playersPayActionCards) {
            assertEquals(DEFAULT_PLAYERS_PAY_AMOUNT, ppAC.getAmountToPay());
        }
    }

    @SuppressWarnings("Duplicates")
    private void AssertCorrectDefaultPayTheBankActionCards(List<PayTheBankActionCard> payTheBankActionCards){
        int DEFAULT_NUM_PAY_THE_BANK_CARDS = 20;
        int NUM_10K = 4;
        int NUM_20K = 4;
        int NUM_30K = 4;
        int NUM_40K = 4;
        int NUM_50K = 4;

        assertEquals(DEFAULT_NUM_PAY_THE_BANK_CARDS, payTheBankActionCards.size());

        // Must check that the amounts to pay for the PayTheBank ActionCards are as specified
        // <AmountToPay, NumberOfTheseCards>
        HashMap<Integer, Integer> amountToPayNumberOfCardsMap = new HashMap<>();
        amountToPayNumberOfCardsMap.put(10000, NUM_10K);
        amountToPayNumberOfCardsMap.put(20000, NUM_20K);
        amountToPayNumberOfCardsMap.put(30000, NUM_30K);
        amountToPayNumberOfCardsMap.put(40000, NUM_40K);
        amountToPayNumberOfCardsMap.put(50000, NUM_50K);

        for (Map.Entry<Integer, Integer> amountMap : amountToPayNumberOfCardsMap.entrySet()) {
            assertEquals(amountMap.getValue().intValue(), payTheBankActionCards.stream()
                    .filter(c -> c.getValue() == amountMap.getKey())
                    .collect(Collectors.toList())
                    .size());
        }
    }

    @SuppressWarnings("Duplicates")
    private void AssertCorrectDefaultGetCashFromBankActionCards(List<GetCashFromBankActionCard> getCashFromBankActionCards){
        int DEFAULT_NUM_GET_CASH_FROM_BANK_CARDS = 20;
        int NUM_10K = 4;
        int NUM_20K = 4;
        int NUM_30K = 4;
        int NUM_40K = 4;
        int NUM_50K = 4;

        assertEquals(DEFAULT_NUM_GET_CASH_FROM_BANK_CARDS, getCashFromBankActionCards.size());

        // Must check that the amounts to pay for the PayTheBank ActionCards are as specified
        // <AmountToPay, NumberOfTheseCards>
        HashMap<Integer, Integer> cashFromBankNumberOfCardsMap = new HashMap<>();
        cashFromBankNumberOfCardsMap.put(10000, NUM_10K);
        cashFromBankNumberOfCardsMap.put(20000, NUM_20K);
        cashFromBankNumberOfCardsMap.put(30000, NUM_30K);
        cashFromBankNumberOfCardsMap.put(40000, NUM_40K);
        cashFromBankNumberOfCardsMap.put(50000, NUM_50K);

        for (Map.Entry<Integer, Integer> amountMap : cashFromBankNumberOfCardsMap.entrySet()) {
            assertEquals(amountMap.getValue().intValue(), getCashFromBankActionCards.stream()
                    .filter(c -> c.getAmountToPay() == amountMap.getKey())
                    .collect(Collectors.toList())
                    .size());
        }
    }
}