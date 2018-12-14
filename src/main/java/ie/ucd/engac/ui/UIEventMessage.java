package ie.ucd.engac.ui;

import java.awt.*;

public class UIEventMessage implements Drawable{

    private int msgYPosBase;
    private int msgXPosBase;

    private String eventMessage;

    UIEventMessage(int panelWidth) {
        this.eventMessage = "Game Started";
        this.msgXPosBase = panelWidth/2;
        this.msgYPosBase = 10;
    }

    void updateEventMessage(String eventMessage){
        this.eventMessage = eventMessage;
    }

    @Override
    public void draw(Graphics graphics) {

        graphics.setColor(Color.black);
        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.5F);
        graphics.setFont(newFont); //want to use a bigger font

        int textWidth = graphics.getFontMetrics().stringWidth(eventMessage); //centering
        int textHeight = graphics.getFontMetrics().getHeight();
        int messageXPos = msgXPosBase - textWidth/2;
        graphics.drawString(eventMessage,messageXPos, msgYPosBase + textHeight/2);

        graphics.setFont(currentFont); //reset font
    }
}
