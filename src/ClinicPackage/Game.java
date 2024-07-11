package ClinicPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import ClinicPackage.Inputs.InputHandler;

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
	InputHandler gameKeyHandler = new InputHandler();
	Thread gameThread; //To here, remnants from a game making tutorial (im smart but ive never made the jump from barebones/nothing to a game making platform through java ok ;-;)
	//Maybe remove?, ill most likely have a settings option for window size in the future so who knows what'll happen with this.
	
	Main gameSystem;
	
	/**Tied how many frames the game runs its calculations at. <br />
	 * THIS HAS NO EFFECT ON INGAME FRAMES <br />
	 * DO NOT EDIT! THIS IS TO KEEP THE GAME FROM RUNNING TOO FAST!
	 */
	final private static int _ANCHOREDFPS = 60;
	
	public Game(Main main)
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(gameKeyHandler);
		this.setFocusable(true);
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
		double actionInterval = 1000000000/_ANCHOREDFPS;
		double upcomingActionTime = System.nanoTime() + actionInterval;
		
		while (gameThread != null)
		{
			Main main = gameSystem;
			if (main.Instance == null)
			{
				main.Initialisation();
			}
			main.Update();
			repaint();
			
			try {
				double remainingActionTime = upcomingActionTime - System.nanoTime();
				remainingActionTime = remainingActionTime/1000000;
				if (remainingActionTime < 0)
					remainingActionTime = 0;
				
				Thread.sleep((long)remainingActionTime);
				upcomingActionTime += actionInterval;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		gameSystem.DoDraw(g);
		g.dispose();
	}
}
