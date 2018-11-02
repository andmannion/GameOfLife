package TestOnly;

import ie.ucd.engac.lifegamelogic.Spinnable;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogic;
import ie.ucd.engac.lifegamelogic.gamestatehandling.PathChoiceState;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHelpers {
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
            System.out.println(gameLogic.getNumberOfUninitialisedPlayers() + " Players remaining");
            assertEquals(LifeGameMessageTypes.OptionDecisionRequest, responseMessage.getLifeGameMessageType());

            //choose a path for this player
            int choiceIndex = PathChoiceState.COLLEGE_CAREER_CHOICE_INDEX;
            choiceMessage = new DecisionResponseMessage(choiceIndex);

            responseMessage = gameLogic.handleInput(choiceMessage);
            assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());
            Player player = gameLogic.getCurrentPlayer();

            //force player back to "a"
            player.setCurrentLocation(new BoardLocation("a"));
            //send back a spin response
            spinMessage = new SpinResponseMessage();
            responseMessage = gameLogic.handleInput(spinMessage);
            assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

            ackMessage = new AckResponseMessage();
            responseMessage = gameLogic.handleInput(ackMessage);
        }


        assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());

        //set object to use the function spinner
        gameLogic.setSpinner(spinner);

        System.out.println("Set up initial conditions");
        System.out.println("-------------------------");
        return gameLogic;
    }
}
