package ie.ucd.engac.ui;

import java.awt.*;

public class UIEventMessage implements Drawable{

    private static final int MSG_Y_POS = 50;
    private static final int MSG_X_POS_BASE = 640;

    private String eventMessage;

    UIEventMessage() {
        this.eventMessage = "Game Started";
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

        int text_width = graphics.getFontMetrics().stringWidth(eventMessage); //centering
        int messageXPos = MSG_X_POS_BASE - text_width/2;
        graphics.drawString(eventMessage,messageXPos,MSG_Y_POS);

        graphics.setFont(currentFont); //reset font
    }
}
