package ie.ucd.engAC.UIScreens;

import ie.ucd.engAC.LifeGame;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SplashScreen extends JPanel {

    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    private BufferedImage splashImage;

    private LifeGame lifeGameParent;
    private Dimension sizePreferences = new Dimension(PANWIDTH,PANHEIGHT);

    public SplashScreen(LifeGame lifeGame){
        lifeGameParent = lifeGame;

        setBackground(Color.gray);
        setPreferredSize(sizePreferences);
        setFocusable(true);
        requestFocus();
        try {
            splashImage = ImageIO.read(new File("./src/ie/ucd/engAC/UIScreens/kermit.jpg"));
        } catch (IOException exception) {
            System.out.println(exception);
        }
        JLabel pictureLabel = new JLabel(new ImageIcon(splashImage));
        add(pictureLabel);
    }
}
