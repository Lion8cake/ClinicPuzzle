package ClinicPackage;
import javax.swing.JFrame;
import Lion8cake.Texture2D;

public class ClinicPuzzel {
	public static void main(String[] str)
	{	
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setLocationRelativeTo(null);
		window.setBounds(20, 20, 0, 0);
		
		Logging.InitiateLogs();
		Game clinicPuzzel = new Game(new Main());
		window.add(clinicPuzzel);
		window.pack();
		
		window.setIconImage(Texture2D.GetIcon());
		window.setTitle("Game");
		window.setVisible(true);
	}
}
