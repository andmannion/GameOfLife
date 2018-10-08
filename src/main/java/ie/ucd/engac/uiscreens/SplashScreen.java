package ie.ucd.engac.uiscreens;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SplashScreen extends JPanel {
    //TODO Just delete this

    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    private BufferedImage splashImage;

    public SplashScreen(){
        super();
        Dimension sizePreferences = new Dimension(PANWIDTH,PANHEIGHT);
        setBackground(Color.gray);
        setPreferredSize(sizePreferences);
        setFocusable(true);
        requestFocus();
        try {
            splashImage = ImageIO.read(new File("./src/resources/kermit.jpg"));
        } catch (IOException exception) {
            System.out.println("Ex. in SplashScreen" + exception);
        }
        JLabel pictureLabel = new JLabel(new ImageIcon(splashImage));
        add(pictureLabel);
    }
}
