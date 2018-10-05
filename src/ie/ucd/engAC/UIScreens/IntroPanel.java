package ie.ucd.engAC.UIScreens;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class IntroPanel extends JPanel {
	private static final int PANWIDTH = 640;
	private static final int PANHEIGHT = 480;

	private Dimension sizePreferences = new Dimension(PANWIDTH, PANHEIGHT);

	public IntroPanel() {
		setBackground(Color.gray);
		setPreferredSize(sizePreferences);
		setFocusable(true);
		requestFocus();
		
		
	}
}
