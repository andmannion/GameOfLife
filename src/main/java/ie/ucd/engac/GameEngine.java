package ie.ucd.engac;

import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogicInterface;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;
import ie.ucd.engac.messaging.MessagingInterface;
import ie.ucd.engac.ui.GameUI;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("ALL")
public class GameEngine implements Runnable {

	private static final String LOGIC_BOARD_CONFIG_FILE_LOCATION = "src/main/resources/LogicGameBoard/GameBoardConfig.json";
    private static final int PANWIDTH = 1280; //TODO what is the best way to manage the window size?
    private static final int PANHEIGHT = 720; //TODO make this work on computers that have window borders

    //objects relating to life game
    private LifeGame lifeGameParent;

    //objects that need to be drawn
    private GameUI gameUI;

    //objects for rendering process
    private JPanel renderTarget;
    private static final int FRAME_TIME = (1000/30);
    private Graphics graphics;
    private Image backBuffer;
    private volatile boolean running;
    private Thread renderingThread;



    /**
     * Constructor for GameEngine class.
     * @param lifeGame      Parent LifeGame that created the engine.
     * @param jPanel        Rendering target panel.
     * @param numPlayers    Number of players in the game.
     */
    GameEngine(LifeGame lifeGame, JPanel jPanel, int numPlayers){
        this.renderTarget = jPanel;
        lifeGameParent = lifeGame;

        LogicGameBoard logicGameBoard = new LogicGameBoard(LOGIC_BOARD_CONFIG_FILE_LOCATION);
        
        MessageReceiverAndResponder<LifeGameMessage> messageReceiverAndResponder = new GameLogicInterface(logicGameBoard, numPlayers);
        MessagingInterface<LifeGameMessage> messagingInterface = new MessagingInterface<>(messageReceiverAndResponder);
        
        gameUI = new GameUI(this,renderTarget,messagingInterface);
    } // end of constructor

    /**
     * Stops the rendering process & returns to the main menu.
     */
    public void quitGame(){
        running = false;
        System.out.println("Stopped rendering"); //TODO remove
        lifeGameParent.returnToMainMenu();
    } // end of quitGame()

    /**
     * Starts the rendering process.
     */
    void beginGame(){
        renderingThread = new Thread(this);
        System.out.println("Starting renderingThread"); //TODO remove
        renderingThread.start();
    } // end of beginGame()

    /**
     * Returns the panel height.
     * @return  integer height of the panel.
     */
    public int getPanelHeight() {
        return PANHEIGHT;
    } // end of getPanelHeight()

    /**
     * Returns the panel width.
     * @return  integer width of the panel.
     */
    public int getPanelWidth() {
        return PANWIDTH;
    } // end of getPanelWidth()


    /**
     * Run function used by the rendering thread.
     */
    @Override
    public void run() {
        running = true;

        long timeBefore;
        long timeAfter;
        long leftOverFrameTime = 0L;
        int remainingFrameTime;
        int excessFrameTime;

        while(running) {
            //https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html
            //attempting to use active rendering & double buffering
            timeBefore = System.nanoTime();

            gameUI.updateCurrentUIScreen();
            renderPanel();
            paintPanel();

            timeAfter = System.nanoTime();

            //compute remaining time in this frame so thread can sleep, rather than render excessively quickly
            remainingFrameTime = FRAME_TIME - (int) ((timeBefore-timeAfter+leftOverFrameTime)/1000000L);
            leftOverFrameTime = 0L;
            if (remainingFrameTime > 0) {
                try {
                    Thread.sleep(remainingFrameTime);
                } catch (InterruptedException sleepCutShort) {
                    //Not needed for a single thread, if we implement a 2nd need to catch this
                    leftOverFrameTime = remainingFrameTime - (System.nanoTime() - timeAfter);
                }
            }
            else {
                excessFrameTime = -remainingFrameTime;
                while (excessFrameTime > FRAME_TIME){
                    //updateStuff();
                    excessFrameTime -= FRAME_TIME;
                }
            }

        } //end of while(running)
    } //end of run()

    /**
     * Called in run() to draw the current representation of the game to the back buffer.
     */
    private void renderPanel(){
        if (backBuffer == null){ //cannot do this in constructor, must do it here each time
            backBuffer = renderTarget.createImage(PANWIDTH, PANHEIGHT);
            if (backBuffer == null) { //if create image somehow failed
                System.out.println("image null");//TODO delete
                return;
            }
            else
                graphics = backBuffer.getGraphics();
        }
        graphics.setColor(Color.lightGray);
        graphics.fillRect (0, 0, PANWIDTH, PANHEIGHT);

        gameUI.draw(graphics);
    } // end of renderPanel()

    /**
     * Called in run() to draw the backbuffer on the target panel.
     */
    private void paintPanel(){
        Graphics tempGraphics;
        try {
            tempGraphics = renderTarget.getGraphics(); //initialise
            //if initialised & backBuffer exits draw new image
            if ((tempGraphics != null) && (backBuffer != null)) {
                tempGraphics.drawImage(backBuffer, 0, 0,null);
            }
        }
        catch (Exception e){
            System.out.println("loadPanel() in GameEngine failed");
        }
    } // end of paintPanel()
}