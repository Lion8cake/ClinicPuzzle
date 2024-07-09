package ClinicPackage;
import javax.swing.JFrame;

import Lion8cake.Logging;
import Lion8cake.Texture2D;

public class ClinicPuzzel {
	public static void main(String[] str)
	{	
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);

		Game clinicPuzzel = new Game(new Main());
		window.add(clinicPuzzel);
		window.pack();
		
		Logging.Log("Game Started", Logging.LoggingType.Base);
		
		window.setIconImage(Texture2D.GetIcon());
		window.setTitle("Game");
		window.setVisible(true);
	}
}
