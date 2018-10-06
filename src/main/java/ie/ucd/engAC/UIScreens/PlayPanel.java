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

    private LifeGame lifeGameParent;
    private static final int PANWIDTH = 1280;
    private static final int PANHEIGHT = 720;
    private ArrayList<Player> playerList;
    private Bank bank;
    private Random random;
    private static long randomSeed = 7777777777777777L;
    private GameHUD gameHUD;
    private GameBoard gameBoard;
    private int numPlayers;
    private Graphics graphics;
    private Image tempImage;
    private int currentPlayer;
    private boolean running;
    private Thread thread;
    //TODO thread terminator on exit


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
        gameHUD = new GameHUD(this);
    }

    private int spinTheWheel(){
        int temp = random.nextInt(9)+1;
        System.out.println(temp);
        return temp;
    }

    private void renderPanel(){
        if (tempImage == null){
            tempImage = createImage(PANWIDTH, PANHEIGHT);
            if (tempImage == null) {
                System.out.println("image null");
                return;
            }
            else
                graphics = tempImage.getGraphics();
        }
        graphics.setColor(Color.white);
        graphics.fillRect (0, 0, PANWIDTH, PANHEIGHT);

        graphics.setColor(Color.green);
        gameHUD.draw(graphics);
    }
    private void loadPanel(){ //TODO redo this as my own
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (tempImage != null))
                g.drawImage(tempImage, 0, 0, null);
            g.dispose();
        }
        catch (Exception e){
            System.out.println("loadPanel() in PlayPanel failed");
        }
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

    @Override
    public void run() {
        //TODO
        running = true;

        while(running) {
            renderPanel();
            loadPanel();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
