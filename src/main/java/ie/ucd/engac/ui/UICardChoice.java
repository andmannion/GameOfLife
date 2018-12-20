package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;

import java.awt.*;
import java.util.ArrayList;

public class UICardChoice implements Drawable {

    private final int choiceYCentre;
    private final int choiceYGap;
    private final int choiceCentre;
    private final int cardWidth;
    private final int cardHeight;

    private GameUI gameUI;

    private volatile String[] option1Split;
    private volatile String[] option2Split;

    UICardChoice(GameUI gameUI,int bottomBoundary){
        this.gameUI = gameUI;
        int panelWidth = gameUI.getPanelWidth();
        int panelHeight = gameUI.getPanelHeight();
        choiceCentre = (panelWidth/3);
        cardWidth = (panelWidth/6);
        cardHeight = (int)(cardWidth*1.618);
        choiceYGap = (panelHeight/48);
        choiceYCentre = (bottomBoundary/2);
    }

    void setChoices(ArrayList<Chooseable> options){
        String option1 = options.get(0).displayChoiceDetails();
        option1Split = option1.split("\n");

        String option2 = options.get(1).displayChoiceDetails();
        option2Split = option2.split("\n");

    }

    private void drawOption(Graphics graphics, String[] option, int xPos, int yPos){
        int inc = 0;
        graphics.setColor(UIColours.CARD_COLOUR);
        graphics.fillRoundRect(xPos,yPos,cardWidth,cardHeight,(cardWidth/4),(cardWidth/4));
        graphics.setColor(Color.black);
        graphics.drawRoundRect(xPos,yPos,cardWidth,cardHeight,(cardWidth/4),(cardWidth/4));
        for (String string:option){
            graphics.drawString(string,xPos+10,yPos+(cardWidth/4)+inc*choiceYGap);
            inc++;
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if(gameUI.getUIState() == UIState.CardChoice){
            graphics.setColor(Color.black);
            drawOption(graphics, option1Split, choiceCentre -(cardWidth/2), choiceYCentre-(cardHeight/2));
            drawOption(graphics, option2Split, 2* choiceCentre -(cardWidth/2), choiceYCentre-(cardHeight/2));
        }
        //else do nothing

    }
}
