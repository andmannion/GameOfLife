package main.java.ie.ucd.engAC.UIScreens;

import main.java.ie.ucd.engAC.LifeGame;
import main.java.ie.ucd.engAC.LifeGameLogic.Bank;
import main.java.ie.ucd.engAC.LifeGameLogic.PlayerLogic.Player;

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


    public PlayPanel(LifeGame lifeGame, ArrayList<Player> playerList, Bank bank){
        super();
        setVisible(false);
        lifeGameParent = lifeGame;
        this.playerList = playerList;
        this.bank = bank;
        setBackground(Color.white);
        setPreferredSize( new Dimension(PANWIDTH, PANHEIGHT));
        JTextField textField = new JTextField("PlayPanel");
        add(textField);
        random = new Random((randomSeed + System.nanoTime()));
        //TODO
    }

    private int spinTheWheel(){
        int temp = random.nextInt(9)+1;
        System.out.println(temp);
        return temp;
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
