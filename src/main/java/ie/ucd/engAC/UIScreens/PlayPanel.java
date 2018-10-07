package ie.ucd.engAC.UIScreens;

import ie.ucd.engAC.LifeGame;
import ie.ucd.engAC.LifeGameLogic.Bank;
import ie.ucd.engAC.LifeGameLogic.PlayerLogic.Player;
import ie.ucd.engAC.UIScreens.UISubPanels.GameBoard;
import ie.ucd.engAC.UIScreens.UISubPanels.GameHUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayPanel extends JPanel implements Runnable,ActionListener {

    private static final int PANWIDTH = 1280;
    private static final int PANHEIGHT = 720;
    private static long randomSeed = 7777777777777777L;

    //rng for spinner
    private Random random;

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
        random = new Random((randomSeed + System.nanoTime()));
        
        ie.ucd.engAC.LifeGameLogic.GameBoard.GameBoard logicGameBoard = new ie.ucd.engAC.LifeGameLogic.GameBoard.GameBoard("src/main/resources/GameBoard/GameBoardConfig.json");
                
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

    private int spinTheWheel(){
        int temp = random.nextInt(9)+1;
        System.out.println(temp);
        return temp;
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            //https://docs.oracle.com/javase/tutorial/extra/fullscreen/rendering.html
            //attempting to use active rendering & double buffering
            renderPanel();
            paintPanel();
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
