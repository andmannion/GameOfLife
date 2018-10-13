package ie.ucd.engac;

import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogicInterface;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.MessageReceiverAndResponder;
import ie.ucd.engac.messaging.MessagingInterface;
import ie.ucd.engac.ui.GameUI;

import javax.swing.*;
import java.awt.*;

public class GameEngine implements Runnable {
	private final String LOGIC_BOARD_CONFIG_FILE_LOCATION = "src/main/resources/LogicGameBoard/GameBoardConfig.json"; 
    private static final int PANWIDTH = 1280; //TODO what is the best way to
    private static final int PANHEIGHT = 720; //TODO manage the window size?

    //objects relating to life game
    private LifeGame lifeGameParent;

    //objects that need to be drawn
    private GameUI gameUI;

    //game logic
    //private GameLogic gameLogic;

    //objects for rendering process
    private JPanel renderTarget;
    private static final int FRAME_TIME = 1/30;
    private Graphics graphics;
    private Image backBuffer;
    private volatile boolean running;
    private Thread renderingThread;
    //TODO should I be using synchronized methods?


    GameEngine(LifeGame lifeGame, JPanel jPanel, int numPlayers){
        //TODO redo this constructor
        this.renderTarget = jPanel;
        lifeGameParent = lifeGame;

        LogicGameBoard logicGameBoard = new LogicGameBoard(LOGIC_BOARD_CONFIG_FILE_LOCATION);
        
        MessageReceiverAndResponder<LifeGameMessage> messageRecieverAndResponder = new GameLogicInterface(logicGameBoard, numPlayers);        
        MessagingInterface<LifeGameMessage> messagingInterface = new MessagingInterface<LifeGameMessage>(messageRecieverAndResponder);
        
        gameUI = new GameUI(this,renderTarget); 
        /* TODO: Please add a messagingInterface parameter to the constructor of this GameUI object
        *		 i.e. gameUI = new GameUI(this, renderTarget, messagingInterface);
    	*/

    }

    public void quitGame(){
        running = false;
        System.out.println("Stopped rendering");
        lifeGameParent.returnToMainMenu();
    }

    void beginGame(){
        renderingThread = new Thread(this);
        System.out.println("Starting renderingThread");
        renderingThread.start();
    } // end of startGame()

    public int getPanelHeight() {
        return PANHEIGHT;
    }

    public int getPanelWidth() {
        return PANWIDTH;
    }


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

            //updateStuff();
            renderPanel();
            paintPanel();

            timeAfter = System.nanoTime();

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
    }

    private void paintPanel(){
        Graphics tempGraphics;
        try {
            tempGraphics = renderTarget.getGraphics(); //initialise
            //if initialised & backBuffer exits draw new image
            if ((tempGraphics != null) && (backBuffer != null)) {
                //TODO do we require an observer?
                tempGraphics.drawImage(backBuffer, 0, 0,null);
            }
        }
        catch (Exception e){
            System.out.println("loadPanel() in GameEngine failed");
        }
    }
}
