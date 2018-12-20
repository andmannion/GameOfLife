package ie.ucd.engac.ui;

import ie.ucd.engac.LifeGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MainMenu extends JPanel implements ActionListener {

    private static final int PANEL_WIDTH = 640;
    private static final int PANEL_HEIGHT = 480;

    private LifeGame lifeGameParent;

    private JButton newGameButton;
    private JButton quitGameButton;
    private JButton playButton;
    private JComboBox<String> jCombo;
    private JTextArea jTextArea;

    private int numPlayers = 2;

    public MainMenu(LifeGame lifeGame){
        super();
        lifeGameParent = lifeGame;
        setBackground(Color.lightGray);
        Dimension sizePreferences = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(sizePreferences);
        setFocusable(true);
        requestFocus();

        GridBagLayout mainMenuManager = new GridBagLayout();
        setLayout(mainMenuManager);
        BufferedImage splashImage = null;

        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.fill = GridBagConstraints.VERTICAL;
        menuConstraints.gridx = 1;
        menuConstraints.gridy = 0;

        try {
            splashImage = ImageIO.read(Objects.requireNonNull(LifeGame.class.getClassLoader().getResource("splash_header.jpg")));
        } catch (Exception exception) {
            System.err.println("Title image failed to load.\n" + exception.toString());
        }

        JLabel pictureLabel = null;
        if (splashImage != null) {
            pictureLabel = new JLabel(new ImageIcon(splashImage));

            add(pictureLabel,menuConstraints);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        constructButtonArea(buttonPanel);
        menuConstraints.gridx = 1;
        menuConstraints.gridy = 2;
        add(buttonPanel,menuConstraints);
    }

    private void constructButtonArea(JPanel buttonPanel){
        GridBagConstraints newGameButtonConstraints = new GridBagConstraints();
        newGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        newGameButtonConstraints.gridx = 1;
        newGameButtonConstraints.gridy = 0;
        newGameButton = new JButton("New Game");
        newGameButton.setActionCommand("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(this);
        buttonPanel.add(newGameButton,newGameButtonConstraints);

        GridBagConstraints quitGameButtonConstraints = new GridBagConstraints();
        quitGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        quitGameButtonConstraints.gridx = 1;
        quitGameButtonConstraints.gridy = 2;
        quitGameButton = new JButton("Exit");
        quitGameButton.setActionCommand("Exit");
        quitGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitGameButton.addActionListener(this);
        buttonPanel.add(quitGameButton,quitGameButtonConstraints);


        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.gridx = 1;
        playButtonConstraints.gridy = 0;
        playButton = new JButton("Play");
        playButton.setActionCommand("Play");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setVisible(false);
        playButton.addActionListener(this);
        buttonPanel.add(playButton,playButtonConstraints);

        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridx = 1;
        textConstraints.gridy = 2;
        String text = "Number of players:";
        jTextArea = new JTextArea(text);
        jTextArea.setBackground(Color.lightGray);
        jTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        jTextArea.setVisible(false);
        buttonPanel.add(jTextArea,textConstraints);

        GridBagConstraints jComboConstraints = new GridBagConstraints();
        jComboConstraints.fill = GridBagConstraints.HORIZONTAL;
        jComboConstraints.gridx = 1;
        jComboConstraints.gridy = 2;
        String[] numPlayersList = { "2", "3", "4"};
        jCombo = new JComboBox<>(numPlayersList);
        jCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        jCombo.addActionListener(this::setNumPlayers);
        jCombo.setSelectedIndex(0);
        jCombo.setVisible(false);
        buttonPanel.add(jCombo,jComboConstraints);
    }

    private void newGame(){
        setVisibilityMainScreen(false);
        setVisibilityNumPlayers(true);
    }
    public void returnToMainMenu(){
        setVisibilityMainScreen(true);
        setVisibilityNumPlayers(false);
    }

    private void setVisibilityMainScreen(boolean bool){
        newGameButton.setVisible(bool);
        quitGameButton.setVisible(bool);
    }

    private void setVisibilityNumPlayers(boolean bool){
        jCombo.setVisible(bool);
        jTextArea.setVisible(bool);
        playButton.setVisible(bool);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) e.getSource();
        if (source instanceof JButton){
            switch (((JButton) source).getActionCommand()){
                case "New Game": newGame(); break;
                case "Exit":     lifeGameParent.dispose(); break;
                case "Play":     lifeGameParent.initialiseGame(numPlayers);
            }
        }
        else
            System.err.println("Error, unhandled action event" + e.getSource());
    }

    private void setNumPlayers(ActionEvent e) {
        JComboBox comboBox = (JComboBox)e.getSource();
        numPlayers = comboBox.getSelectedIndex()+2;
    }
}
