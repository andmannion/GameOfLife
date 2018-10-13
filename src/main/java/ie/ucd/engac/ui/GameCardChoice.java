package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.ui.*;
import java.awt.*;
import java.util.ArrayList;

public class GameCardChoice  implements Drawable {

    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 419;
    private static final int CHOICE_Y_POS = 216;

    private GameUI gameUI;
    private ArrayList<Chooseable> choices;

    GameCardChoice(GameUI gameUI){
        this.gameUI = gameUI;
    }

    void setChoices(ArrayList<Chooseable> choices){
        this.choices = choices;
    }

    @Override
    public void draw(Graphics graphics) {
        if(gameUI.getUIState() == UIState.CardChoice){
            graphics.setColor(Color.black);
            graphics.drawString(choices.get(0).displayChoiceDetails(),CARD_CHOICE_LHS_GAP,CHOICE_Y_POS);
            graphics.drawString(choices.get(1).displayChoiceDetails(),CARD_CHOICE_LHS_GAP+CARD_CHOICE_INTER_GAP,CHOICE_Y_POS);
        }
        else System.out.println("Error");

    }
}
