package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NightSchoolStateTest {
    private static final int NUM_PLAYERS = 2;
    private final String PRIOR_TILE_LOCATION = "am";
    private final String LIFE_PATH_INITIAL_TILE = "ao";
    private final String NIGHT_SCHOOL_INITIAL_TILE = "av";

    @Test
    void testAttendingNightSchool() {
        GameLogic gameLogic = configureNightSchoolStateTestGameLogic();

        Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

        assertEquals(1, gameLogic.getCurrentPlayer().getPlayerNumber());

        CollegeCareerCard initialCollegeCareerCard = (CollegeCareerCard) currentPlayerUnderTest.getOccupationCard();
        assertNotNull(initialCollegeCareerCard);

        int initialBalance = currentPlayerUnderTest.getCurrentMoney();

        currentPlayerUnderTest.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(), "Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Message should be requesting a choice to move to the family path or the night school path
        assertEquals(LifeGameMessageTypes.OptionDecisionRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new DecisionResponseMessage(NightSchoolState.ATTEND_NIGHT_SCHOOL_INDEX, LifeGameMessageTypes.OptionDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        // Assert that a new college career card has been assigned
        assertFalse(initialCollegeCareerCard.equals((CollegeCareerCard) currentPlayerUnderTest.getOccupationCard()));

        // Assert that night school fees have been deducted from the current player's balance
        int balanceDelta = initialBalance - currentPlayerUnderTest.getCurrentMoney();

        assertEquals(GameConfig.night_school_tuition_fees, balanceDelta);

        // Assert that the player gets another turn to spin
        assertEquals(1, gameLogic.getCurrentPlayer().getPlayerNumber());
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(), "Expected message not received");
        spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Assert that the player has been moved to the night school board path
        assertEquals(NIGHT_SCHOOL_INITIAL_TILE, currentPlayerUnderTest.getCurrentLocation().getLocation());

        // Should not be prompted for night school
        if (messageFromLogic.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionRequest) {
            Chooseable firstString = ((DecisionRequestMessage) messageFromLogic).getChoices().get(0);
            Chooseable secondString = ((DecisionRequestMessage) messageFromLogic).getChoices().get(1);

            assertNotEquals(NightSchoolState.KEEP_CAREER_MSG, firstString.displayChoiceDetails());
            assertNotEquals(NightSchoolState.ATTEND_NIGHT_SCHOOL_MSG, secondString.displayChoiceDetails());
        }
    }

    @Test
    void testNotAttendingNightSchool() {
        GameLogic gameLogic = configureNightSchoolStateTestGameLogic();

        Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

        assertEquals(1, gameLogic.getCurrentPlayer().getPlayerNumber());

        CollegeCareerCard initialCollegeCareerCard = (CollegeCareerCard) currentPlayerUnderTest.getOccupationCard();
        assertNotNull(initialCollegeCareerCard);

        int initialBalance = currentPlayerUnderTest.getCurrentMoney();

        currentPlayerUnderTest.setCurrentLocation(new BoardLocation(PRIOR_TILE_LOCATION));

        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(), "Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Message should be requesting a choice to move to the family path or the night school path
        assertEquals(LifeGameMessageTypes.OptionDecisionRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new DecisionResponseMessage(NightSchoolState.KEEP_CAREER_INDEX, LifeGameMessageTypes.OptionDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertTrue(initialCollegeCareerCard.equals((CollegeCareerCard) currentPlayerUnderTest.getOccupationCard()));

        // Assert that night school fees have been deducted from the current player's balance
        int balanceDelta = initialBalance - currentPlayerUnderTest.getCurrentMoney();

        assertEquals(0, balanceDelta);

        // Assert that the player gets another turn to spin
        assertEquals(1, gameLogic.getCurrentPlayer().getPlayerNumber());
        assertEquals(LifeGameMessageTypes.SpinRequest, messageFromLogic.getLifeGameMessageType());

        messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(), "Expected message not received");
        spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Assert that the player has been moved to the night school board path
        assertEquals(LIFE_PATH_INITIAL_TILE, currentPlayerUnderTest.getCurrentLocation().getLocation());
    }

    private static GameLogic configureNightSchoolStateTestGameLogic() {
        String priorToGraduateTileLocation = "o";
        int firstCollegeCareerCardChoice = 0;

        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);

        gameLogic.getCurrentPlayer().setCurrentLocation(new BoardLocation(priorToGraduateTileLocation));

        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(), "Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // Just choose the first CollegeCareerCard
        messageToLogic = new DecisionResponseMessage(firstCollegeCareerCardChoice, LifeGameMessageTypes.OptionDecisionResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        messageToLogic = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(messageToLogic);

        // Cheat here
        gameLogic.setNextPlayerToCurrent();

        return gameLogic;
    }
}
