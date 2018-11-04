package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;
import java.awt.*;
import java.util.ArrayList;

public class UICardChoice implements Drawable {

    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 419;
    private static final int CHOICE_Y_POS = 216;
    private static final int CHOICE_Y_GAP = 15;

    private GameUI gameUI;

    private volatile String[] option1Split;
    private volatile String[] option2Split;

    UICardChoice(GameUI gameUI){
        this.gameUI = gameUI;
    }

    void setChoices(ArrayList<Chooseable> options){
        String option1 = options.get(0).displayChoiceDetails();
        option1Split = option1.split("\n");

        String option2 = options.get(1).displayChoiceDetails();
        option2Split = option2.split("\n");

    }

    private void drawOption(Graphics graphics, String[] option, int xPos, int yPos){
        int inc = 0;
        for (String string:option){
            graphics.drawString(string,xPos,yPos+inc*CHOICE_Y_GAP);
            inc++;
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if(gameUI.getUIState() == UIState.CardChoice){
            graphics.setColor(Color.black);
            drawOption(graphics, option1Split, CARD_CHOICE_LHS_GAP, CHOICE_Y_POS);
            drawOption(graphics, option2Split, CARD_CHOICE_LHS_GAP+CARD_CHOICE_INTER_GAP, CHOICE_Y_POS);
        }
        //else do nothing

    }
}
