package ClinicPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {
	/**
	 * Version of the program
	 */
	private static final long serialVersionUID = 1L;
	
	final int originalBlockSize = 16; //From here
	final int scale = 3;
	final int blockSize = originalBlockSize * scale;
	final int screenWidth = blockSize * 16;
	final int screenHeight = blockSize * 12;
	Thread gameThread; //To here, remnants from a game making tutorial (im smart but ive never made the jump from barebones/nothing to a game making platform through java ok ;-;)
	//Maybe remove?, ill most likely have a settings option for window size in the future so who knows what'll happen with this.
	
	Main gameSystem;
	
	public Game(Main main)
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		gameSystem = main;
		startGameThread();
	}
	
	public void startGameThread()
	{
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		while (gameThread != null)
		{
			Main main = gameSystem;
			if (main.Instance == null)
			{
				main.Initialisation();
			}
			main.Update();
			repaint();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		gameSystem.DoDraw(g);
		g.dispose();
	}
}
