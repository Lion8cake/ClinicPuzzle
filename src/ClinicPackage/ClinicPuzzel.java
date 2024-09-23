package ClinicPackage;
import java.awt.Dimension;

import javax.swing.JFrame;
import Lion8cake.Texture2D;

public class ClinicPuzzel { //This is where the main method is kept, being how the Jframe is created.
	public static void main(String[] str)
	{	
		JFrame window = new JFrame(); //we create a J frame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Properly allow the Jframe to close the application
		window.setResizable(false); //Remove the ability to resize the window
		window.setLocationRelativeTo(null); //Set it so there is no location of the window
		window.setBounds(0, 0, 0, 0); //and set the window size to 0
		
		Logging.InitiateLogs(); //Startup the logs - Logs are found at src/ClinicPackage/Logging.java
		Game clinicPuzzel = new Game(new Main(), window); //Create the game - game is found at src/ClinicPackage/Game
		window.add(clinicPuzzel);
		window.setPreferredSize(new Dimension(Game.screenWidth, Game.screenHeight));//set the window size
		window.pack(); //pack the Jframe so it accepts the game and window size change
		
		window.setIconImage(Texture2D.GetIcon()); //Set the programs Icon
		window.setTitle("Clinic Puzzel - Game Engine Tech Demo");//...and name
		window.setVisible(true); //and mark it as visible
    }
}