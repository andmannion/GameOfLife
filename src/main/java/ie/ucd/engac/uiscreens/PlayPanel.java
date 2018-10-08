package ie.ucd.engac.uiscreens;

import ie.ucd.engac.LifeGame;
import ie.ucd.engac.lifegamelogic.banklogic.Bank;
import ie.ucd.engac.lifegamelogic.gameboardlogic.LogicGameBoard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.uiscreens.uisubpanels.GameBoard;
import ie.ucd.engac.uiscreens.uisubpanels.GameHUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayPanel extends JPanel implements Runnable,ActionListener {

    private static final int PANWIDTH = 1280;
    private static final int PANHEIGHT = 720;

    //objects relating to life game
    private LifeGame lifeGameParent;
    private ArrayList<Player> playerList;
    private Bank bank;

    //tracking of the player that requires drawing
    private int numPlayers;
    private int currentPlayer;

    //objects that need to be drawn
    private GameHUD gameHUD;
    private GameBoard gameBoard;

    //objects for rendering process
    private final int FRAMETIME = 1/30;
    private Graphics graphics;
    private Image backBuffer;
    private boolean running;
    private Thread renderingThread;


    public PlayPanel(LifeGame lifeGame, int numPlayers){
        super();
        setVisible(false);
        setBackground(Color.white);
        setPreferredSize( new Dimension(PANWIDTH, PANHEIGHT));
        JTextField textField = new JTextField("PlayPanel");
        add(textField);
        this.numPlayers = numPlayers;
        lifeGameParent = lifeGame;
        playerList = new ArrayList<>();
        bank = new Bank();
        for(int i = 0;i<numPlayers;i++){
            Player player = new Player(i);
            playerList.add(player);
        }

        //LogicGameBoard logicGameBoard = new LogicGameBoard("src/main/resources/logicgameboard/GameBoardConfig.json");
        LogicGameBoard logicGameBoard = new LogicGameBoard("src/main/resources/LogicGameBoard/GameBoardConfig.json");

        gameBoard = new GameBoard();
        gameHUD = new GameHUD(this); //need to pass the panel to get the playerinfo
    }

    public void closeGame(){
        running = false;
        System.out.println("Stopped rendering");
    }

    public void beginGame(){
        renderingThread = new Thread(this);
        System.out.println("Starting renderingThread");
        renderingThread.start();
    } // end of startGame()

    public Player getCurrentPlayer(){
        return playerList.get(currentPlayer);
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            //https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html
            //attempting to use active rendering & double buffering
            long timeBefore = System.nanoTime();
            renderPanel();
            paintPanel();
            long timeAfter = System.nanoTime();
            //TODO needs protection against negative times
            int sleepTime = FRAMETIME - (int) ((timeBefore-timeAfter)/1000000L);
            try{
                Thread.sleep(sleepTime);
            }
            catch (Exception sleepException){
                //TODO what goes here?
                System.out.println("Sleep ex." + sleepException);
            }
        }
    }

    private void renderPanel(){
        if (backBuffer == null){ //cannot do this in constructor, must do it here each time
            backBuffer = createImage(PANWIDTH, PANHEIGHT);
            if (backBuffer == null) { //if create image somehow failed
                System.out.println("image null");
                return;
            }
            else
                graphics = backBuffer.getGraphics();
        }
        graphics.setColor(Color.lightGray);
        graphics.fillRect (0, 0, PANWIDTH, PANHEIGHT);

        gameHUD.draw(graphics);
    }

    private void paintPanel(){
        Graphics tempGraphics;
        try {
            tempGraphics = this.getGraphics(); //initialise
            //if initialised & backBuffer exits draw new image
            if ((tempGraphics != null) && (backBuffer != null)) {
                //TODO do we require an observer?
                tempGraphics.drawImage(backBuffer, 0, 0,null);
            }
        }
        catch (Exception e){
            System.out.println("loadPanel() in PlayPanel failed");
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
