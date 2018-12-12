package ie.ucd.engac.lifegamelogic.gamestates;

import TestOnly.TestHelpers;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboard.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraduationStateTest {
    private static final int NUM_PLAYERS = 2;


    @Test
    void testOnePlayerGraduationStateLogic(){
        GameLogic gameLogic = configureGraduationStateTestGameLogic();

        Player currentPlayerUnderTest = gameLogic.getCurrentPlayer();

        assertEquals(1, currentPlayerUnderTest.getPlayerNumber());

        // Ensure that the player under test does not have a CollegeCareer card already
        assertNull(currentPlayerUnderTest.getOccupationCard());

        int firstCollegeCareerCardChoice = 0;

        // Just choose the first CollegeCareerCard
        LifeGameMessage messageToLogic = new DecisionResponseMessage(firstCollegeCareerCardChoice, LifeGameMessageTypes.OptionDecisionResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        // Now must assert that the player under test has been assigned a CollegeCareerCard
        assertEquals(OccupationCardTypes.CollegeCareer, currentPlayerUnderTest.getOccupationCard().getOccupationCardType());
    }


    private static GameLogic configureGraduationStateTestGameLogic(){
        String priorToGraduateTileLocation = "o";

        GameLogic gameLogic = TestHelpers.setupTestGenericPreconditions(NUM_PLAYERS, 1);
        gameLogic.getCurrentPlayer().setCurrentLocation(new BoardLocation(priorToGraduateTileLocation));

        LifeGameMessage messageToLogic = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        LifeGameMessage messageFromLogic = gameLogic.handleInput(messageToLogic);

        assertEquals(LifeGameMessageTypes.SpinResult, messageFromLogic.getLifeGameMessageType(),"Expected message not received");
        LifeGameMessage spinMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        messageFromLogic = gameLogic.handleInput(spinMessage);

        // GameLogic now expects a DecisionResponseMessage relating to the current player

        return gameLogic;
    }
}