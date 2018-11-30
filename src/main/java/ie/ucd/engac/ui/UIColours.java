package ie.ucd.engac.ui;

import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.awt.*;

public class UIColours {
    public static final Color STOP_COLOUR = new Color(240,170,117);
    public static final Color ACTION_COLOUR = new Color(211,210,130);
    public static final Color PAYDAY_COLOUR = new Color(149,220,142);
    public static final Color HOLIDAY_COLOUR = new Color(112,224,193);
    public static final Color S2W_COLOUR = new Color(85,220,225);
    public static final Color BABY_COLOUR = new Color(167,179,242);
    public static final Color HOUSE_COLOUR = new Color(238,160,213);
    public static final Color CARD_COLOUR = new Color(224,216,188);
    public static final Color HUD_AREA_COLOUR = new Color(107,99,84);

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
            default:
                return Color.lightGray;

        }
    }
}
