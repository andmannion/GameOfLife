package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.messaging.*;

import java.awt.*;
import java.util.ArrayList;

public class ChoosePawnsState extends GameState {

    @Override
    public void enter(GameLogic gameLogic) {

    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;

        if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.StartupMessage){
            //TODO
            ArrayList<Pawn> pawns = new ArrayList<>();
            pawns.add(new Pawn(0.0,0.0, Color.pink, 1,0));
            pawns.add(new Pawn(0.0,0.0, Color.red, 2,0));

            LifeGameMessage replyMessage = new UIConfigMessage(pawns,"Game Ready!");
            gameLogic.setResponseMessage(replyMessage);
        }
        else if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse){
            LifeGameMessage replyMessage = constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber());
            gameLogic.setResponseMessage(replyMessage);
            nextState = new PathChoiceState();
        }

        return nextState;
    }

    @Override
    public void exit(GameLogic gameLogic) {

    }


}
