package TestOnly;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.LifeGame;
import ie.ucd.engac.lifegamelogic.TestSpinner;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogic;
import ie.ucd.engac.lifegamelogic.gamestatehandling.PathChoiceState;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHelpers {
    public static GameLogic setupTestGenericPreconditions(int numberOfPlayers, int fixedSpinnerValue){
        //setup object with non functional spinner

        importGameConfig();
        GameLogic gameLogic = new GameLogic(new LogicGameBoard(GameConfig.game_board_config_file_location), numberOfPlayers, new TestSpinner(0));

        LifeGameMessage initialMessage;
        LifeGameMessage responseMessage;
        LifeGameMessage choiceMessage;
        LifeGameMessage spinMessage;
        LifeGameMessage ackMessage;

        initialMessage = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        responseMessage = gameLogic.handleInput(initialMessage);

        while (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
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
            spinMessage = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
            responseMessage = gameLogic.handleInput(spinMessage);
            assertEquals(LifeGameMessageTypes.AckRequest, responseMessage.getLifeGameMessageType());

            ackMessage = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
            responseMessage = gameLogic.handleInput(ackMessage);
        }

        assertEquals(LifeGameMessageTypes.SpinRequest, responseMessage.getLifeGameMessageType());

        //set object to use the function spinner
        gameLogic.setSpinner(new TestSpinner(fixedSpinnerValue));

        return gameLogic;
    }

    public static void importGameConfig() {
            InputStream inputStream;
            Properties properties = new Properties();
            try {
                inputStream = LifeGame.class.getClassLoader().getResourceAsStream("config.properties");//new FileInputStream("src/main/resources/config.properties");

                properties.load(inputStream);
            } catch (Exception exception) {
                System.err.println("config.properties not found.");
            }
            new GameConfig(properties);
    }
}
