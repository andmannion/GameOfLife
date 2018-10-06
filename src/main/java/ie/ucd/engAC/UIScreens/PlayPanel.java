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
    private int currentPlayer;


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

        //TODO
    }

    private int spinTheWheel(){
        int temp = random.nextInt(9)+1;
        System.out.println(temp);
        return temp;
    }

    private void renderPanel(){
        gameHUD.draw(graphics);
    }
    public Player getCurrentPlayer(){
        return playerList.get(currentPlayer);
    }

    @Override
    public void run() {
        //TODO
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
