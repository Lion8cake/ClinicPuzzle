package ClinicPackage;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Game extends JPanel implements Runnable {
	/**
	 * Version of the program
	 */
	private static final long serialVersionUID = 1L;
	
	final int originalBlockSize = 16;
	final int scale = 3;
	
	final int blockSize = originalBlockSize * scale;
	
	final int screenWidth = blockSize * 16;
	final int screenHeight = blockSize * 12;
	
	Thread gameThread;
	
	Main gameSystem;
	
	public Game(Main main)
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		gameSystem = main;
		startGameThread();
	}
	
	public void startGameThread()
	{
		gameThread = new Thread(this);
		gameThread.start();
		//System.out.println("Thread Created");
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
		}
	}
}
