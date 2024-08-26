package ClinicPackage;
import java.awt.Dimension;

import javax.swing.JFrame;
import Lion8cake.Texture2D;

public class ClinicPuzzel {
	public static void main(String[] str)
	{	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	private static void createAndShowGUI() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setBounds(0, 0, 0, 0);
		
		Logging.InitiateLogs();
		Game clinicPuzzel = new Game(new Main(), window);
		window.add(clinicPuzzel);
		window.setPreferredSize(new Dimension(Game.screenWidth, Game.screenHeight));
		window.pack();
		
		window.setIconImage(Texture2D.GetIcon());
		window.setTitle("Game");
		window.setVisible(true);
    }
}