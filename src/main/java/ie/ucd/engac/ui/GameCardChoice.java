package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;
import java.awt.*;
import java.util.ArrayList;

public class GameCardChoice  implements Drawable {

    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 419;
    private static final int CHOICE_Y_POS = 216;

    private GameUI gameUI;
    private ArrayList<Chooseable> choices;

    private volatile String choice1 = "";
    private volatile String choice2 = "";

    GameCardChoice(GameUI gameUI){
        this.gameUI = gameUI;
    }

    void setChoices(ArrayList<Chooseable> choices){
        this.choices = choices; //TODO remove
        choice1 = choices.get(0).displayChoiceDetails();
        choice2 = choices.get(1).displayChoiceDetails();
    }

    @Override
    public void draw(Graphics graphics) {
        if(gameUI.getUIState() == UIState.CardChoice){
            graphics.setColor(Color.black);
            graphics.drawString(choice1,CARD_CHOICE_LHS_GAP,CHOICE_Y_POS);
            graphics.drawString(choice2,CARD_CHOICE_LHS_GAP+CARD_CHOICE_INTER_GAP,CHOICE_Y_POS);
        }
        //else do nothing

    }
}
