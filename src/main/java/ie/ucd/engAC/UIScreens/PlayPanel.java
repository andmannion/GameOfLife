package main.java.ie.ucd.engAC.UIScreens;

import main.java.ie.ucd.engAC.LifeGame;
import main.java.ie.ucd.engAC.LifeGameLogic.Bank;
import main.java.ie.ucd.engAC.LifeGameLogic.PlayerLogic.Player;
import main.java.ie.ucd.engAC.UIScreens.UISubPanels.GameBoard;
import main.java.ie.ucd.engAC.UIScreens.UISubPanels.GameHUD;

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
    private Image tempImage;
    private boolean running;
    private Thread thread;


    public PlayPanel(LifeGame lifeGame, int numPlayers){
        super();
        setVisible(false);
        setBackground(Color.white);
        setPreferredSize( new Dimension(PANWIDTH, PANHEIGHT));
        JTextField textField = new JTextField("PlayPanel");
        add(textField);
        this.numPlayers = numPlayers;
        lifeGameParent = lifeGame;
        playerList = new ArrayList<Player>();
        bank = new Bank();
        for(int i = 0;i<numPlayers;i++){
            Player player = new Player(i);
            playerList.add(player);
        }
        random = new Random((randomSeed + System.nanoTime()));
        gameBoard = new GameBoard();
        gameHUD = new GameHUD(this); //need to pass the panel to get the playerinfo
    }

    public void closeGame(){
        running = false;
        System.out.println("Stopped rendering");
    }

    public void beginGame(){//TODO redo this as my own
        if (thread == null || !running) {
            thread = new Thread(this);
            System.out.println("Starting thread");
            thread.start();
        }
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
            renderPanel();
            loadPanel();
        }
    }

    private void renderPanel(){ //TODO redo this
        if (tempImage == null){
            tempImage = createImage(PANWIDTH, PANHEIGHT);
            if (tempImage == null) {
                System.out.println("image null");
                return;
            }
            else
                graphics = tempImage.getGraphics();
        }
        graphics.setColor(Color.lightGray);
        graphics.fillRect (0, 0, PANWIDTH, PANHEIGHT);

        graphics.setColor(Color.green);
        gameHUD.draw(graphics);
    }

    private void loadPanel(){ //TODO redo this as my own
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (tempImage != null)) {
                g.drawImage(tempImage, 0, 0, null);
            }

            g.dispose();
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
