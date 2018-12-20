package ie.ucd.engac.ui;

import java.awt.*;

class UIColours {
    static final Color STOP_COLOUR = new Color(255,100,100);
    static final Color ACTION_COLOUR = new Color(211,210,130);
    static final Color PAYDAY_COLOUR = new Color(149,220,142);
    static final Color HOLIDAY_COLOUR = new Color(112,224,193);
    static final Color S2W_COLOUR = new Color(85,220,225);
    static final Color BABY_COLOUR = new Color(167,179,242);
    static final Color HOUSE_COLOUR = new Color(238,160,213);
    static final Color CARD_COLOUR = new Color(224,216,188);
    static final Color HUD_AREA_COLOUR = new Color(208,214,222	);
    static final Color START_COLOUR = new Color(206,214,180);

    static Color getTileColour(String type){
        switch (type){
            case "Action":
                return ACTION_COLOUR;
            case "Stop":
                return STOP_COLOUR;
            case "Holiday":
                return HOLIDAY_COLOUR;
            case "Payday":
                return PAYDAY_COLOUR;
            case "SpinToWin":
                return S2W_COLOUR;
            case "Baby": case "Twins":
                return BABY_COLOUR;
            case "House":
                return HOUSE_COLOUR;
            case "Start":
                return START_COLOUR;
            default:
                return STOP_COLOUR;

        }
    }
}
