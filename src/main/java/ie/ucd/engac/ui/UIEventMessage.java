package ie.ucd.engac.ui;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import java.awt.*;

public class UIEventMessage implements Drawable{

    private static final int FRAMES_TO_DISPLAY_FOR = 60;
    private static final int MSG_Y_POS = 25;
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
        remainingDisplayFrames = FRAMES_TO_DISPLAY_FOR;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.black);
        if(remainingDisplayFrames > 0){
            //int messageXPos = MSG_X_POS_BASE - text_width*eventMessageLength/2; //TODO figure out what width is
            graphics.drawString(eventMessage,0,MSG_Y_POS);
            remainingDisplayFrames--;
            //TODO drawString
        }
    }
}
