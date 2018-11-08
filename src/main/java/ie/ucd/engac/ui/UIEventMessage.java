package ie.ucd.engac.ui;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import java.awt.*;

public class UIEventMessage implements Drawable{

    //private static final int FRAMES_TO_DISPLAY_FOR = 60;
    private static final int MSG_Y_POS = 50;
    private static final int MSG_X_POS_BASE = 640;

    private String eventMessage;
    private int remainingDisplayFrames;
    private int eventMessageLength;

    UIEventMessage() {
        this.eventMessage = "Game Started";
        this.eventMessageLength = eventMessage.length();
    }

    void updateEventMessage(String eventMessage){
        this.eventMessage = eventMessage;
        eventMessageLength = eventMessage.length();
        //remainingDisplayFrames = FRAMES_TO_DISPLAY_FOR;
    }

    @Override
    public void draw(Graphics graphics) {

        //if(remainingDisplayFrames > 0){
        graphics.setColor(Color.black);
        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.5F);
        graphics.setFont(newFont); //want to use a bigger font

        int text_width = graphics.getFontMetrics().stringWidth(eventMessage); //centring
        int messageXPos = MSG_X_POS_BASE - text_width/2;
        graphics.drawString(eventMessage,messageXPos,MSG_Y_POS);

            //remainingDisplayFrames--;
        graphics.setFont(currentFont); //reset font
        //}
    }
}
