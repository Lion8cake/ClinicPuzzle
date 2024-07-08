package ClinicPackage;
import javax.swing.JFrame;
import Lion8cake.Texture2D;

public class ClinicPuzzel {
	public static void main(String[] str)
	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(-10, 0, 1940, 1200);
		window.setIconImage(Texture2D.GetIcon());
		window.setTitle("Game");
		window.setVisible(true);
	}
}
