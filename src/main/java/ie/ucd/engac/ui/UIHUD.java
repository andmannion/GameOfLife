package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.ShadowPlayer;

import java.awt.*;

public class UIHUD implements Drawable {

    private ShadowPlayer sPlayer;
    private GameUI gameUI;
    private Rectangle rectangle;
    private int spinResult;

    private final static int PLAYER_LOC = 0;
    private final static int BANK_LOC   = 1;
    private final static int LOANS_LOC  = 2;
    private final static int DEPEND_LOC = 3;
    private final static int CAREER_LOC = 4;
    private final static int HOUSE_LOC  = 5;
    private final static int ACTION_LOC = 6;


    private int firstStringX;
    private int firstStringY;
    private int stringLengthX;
    private int stringLengthY;

    private int cropAvoidance;
    private final int spinResultX;

    UIHUD(GameUI gameUI, int hudStartY){
        this.gameUI = gameUI;

        int panelHeight = gameUI.getPanelHeight();
        int panelWidth = gameUI.getPanelWidth();

        int boxLengthY = panelHeight - hudStartY;
        int boxStartX = 0;

        firstStringX = 5; //gap
        firstStringY = hudStartY +Math.round(0.08f* boxLengthY);
        stringLengthX = Math.round(0.1f* panelWidth);
        stringLengthY = Math.round(0.125f* boxLengthY);
        cropAvoidance = panelHeight/10;

        rectangle = new Rectangle(boxStartX, hudStartY, panelWidth, boxLengthY);

        spinResultX = panelWidth-3*cropAvoidance;
    }

    private int getStringWidth(String string,Graphics graphics){
        return graphics.getFontMetrics().stringWidth(string); //centring
    }

    void setSpinResult(int spinResult) {
        this.spinResult = spinResult;
    }


    void updateFields(ShadowPlayer shadowPlayer){
        this.sPlayer = shadowPlayer;
    }
    public void draw(Graphics graphics){
        switch(gameUI.getUIState()){
            case Init:
                break;
            case WaitingForSpin: case PostSpin:
                if(sPlayer != null){
                    try{
                        drawHUD(graphics);
                    }
                    catch (Exception e){
                        System.err.println("Exception in UIHUD.draw() " + e.toString());
                    }
                }
                break;
            case CardChoice:
            case LargeChoice:
                if(sPlayer != null){
                    try{
                        drawHUD(graphics);
                    }
                    catch (Exception e){
                        System.err.println("Exception in UIHUD.draw() " + e.toString());
                    }
                }
            case WaitingForAck:
                if(sPlayer != null){
                    try{
                        drawHUD(graphics);
                    }
                    catch (Exception e){
                        System.err.println("Exception in UIHUD.draw() " + e.toString());
                    }
                }
            case EndGame:
                break;
            default:
                break;
        }
    }

    private void drawHUD(Graphics graphics) {
        graphics.setColor(UIColours.HUD_AREA_COLOUR);
        graphics.fillRoundRect(rectangle.x-cropAvoidance,rectangle.y,(rectangle.width/2)+cropAvoidance,rectangle.height+cropAvoidance,rectangle.height/4, rectangle.height/4);
        graphics.setColor(Color.black);
        graphics.drawRoundRect(rectangle.x-cropAvoidance,rectangle.y,(rectangle.width/2)+cropAvoidance,rectangle.height+cropAvoidance,rectangle.height/4, rectangle.height/4);
        graphics.setColor(sPlayer.getPlayerColour());
        String string = "Player " + sPlayer.playerNumToString();
        graphics.drawString(string,firstStringX, firstStringY+stringLengthY*PLAYER_LOC);
        graphics.setColor(Color.black);
        graphics.drawString(" " +  sPlayer.currentTileToString(),firstStringX+(getStringWidth(string,graphics)), firstStringY+stringLengthY*PLAYER_LOC);
        //+ "" + sPlayer.currentTileToString()
        graphics.drawString("Bank Balance: "+ sPlayer.bankBalToString(),        firstStringX, firstStringY+stringLengthY*BANK_LOC);
        graphics.drawString("Number of loans: " + sPlayer.numLoansToString(),   firstStringX, firstStringY+stringLengthY*LOANS_LOC);
        graphics.drawString("Career Card: " + sPlayer.careerCardToString(),     firstStringX, firstStringY+stringLengthY*DEPEND_LOC);
        graphics.drawString("House Cards: " + sPlayer.houseCardsToString(),     firstStringX, firstStringY+stringLengthY*CAREER_LOC);
        graphics.drawString("Dependants: "+ sPlayer.dependantsToString(),       firstStringX, firstStringY+stringLengthY*HOUSE_LOC);
        graphics.drawString("Action Cards: " + sPlayer.actionCardsToString(),   firstStringX, firstStringY+stringLengthY*ACTION_LOC);

        drawSpinResult(graphics,(rectangle.width/2)+cropAvoidance,firstStringY+stringLengthY*ACTION_LOC);
    }

    private void drawSpinResult(Graphics graphics,int endX, int startY) {
        Font currentFont = graphics.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.5F);
        graphics.setFont(newFont); //want to use a bigger font
        graphics.setColor(Color.black);
        String string = "Last spin: "+ spinResult;
        int text_width = graphics.getFontMetrics().stringWidth(string); //centering
        graphics.drawString(string,endX-2*text_width,startY);
        graphics.setFont(currentFont); //reset font
    }
}


