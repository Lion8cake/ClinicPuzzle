package ClinicPackage;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ClinicPackage.Inputs.InputHandler;

public class Game extends JPanel implements Runnable {
	/**
	 * Version of the program
	 */
	private static final long serialVersionUID = 1L;
	
	public static int screenWidth = 32 * 32;
	public static int screenHeight = 32 * 24;
	public static Point windowPos = new Point(0, 0);
	InputHandler gameKeyHandler = new InputHandler();
	Thread gameThread;
	Main gameSystem;
	
	/**Tied how many frames the game runs its calculations at. <br />
	 * THIS HAS NO EFFECT ON INGAME FRAMES <br />
	 * DO NOT EDIT! THIS IS TO KEEP THE GAME FROM RUNNING TOO FAST!
	 */
	final private static int _ANCHOREDFPS = 60;
	
	final private static double _NANOSECONDFPS = 1000000000/_ANCHOREDFPS;
	
	private static Game Instance;
	
	public static JFrame Parent = null;
	
	public Game(Main main, JFrame parent)
	{
		Parent = parent;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		//this.setBounds(20, 20, screenWidth, screenHeight);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(gameKeyHandler);
		this.setFocusable(true);
		gameSystem = main;
		startGameThread();
		Instance = this;
	}

	public void startGameThread()
	{
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		double frameDelta = 0;
		long nanoTimeOld = System.nanoTime();
		long nanoTime;
		while (gameThread != null)
		{
			nanoTime = System.nanoTime();
			frameDelta += (nanoTime - nanoTimeOld) / _NANOSECONDFPS;
			nanoTimeOld = nanoTime;
			
			if (frameDelta >= 1)
			{
				Main main = gameSystem;
				Main.ScreenWidth = screenWidth;
				Main.ScreenHeight = screenHeight;
				if (Main.Instance == null)
				{
					main.Initialisation();
				}
				int winX = screenWidth;
				int winY = screenHeight;
				main.Update();
				if (winX != screenWidth || winY != screenHeight)
				{
					Main.ScreenSizeChange = true;
				}
				repaint();
				
				frameDelta--;
			}
		}
		JFrame window = Parent;
		
		window.add(this);
		window.setPreferredSize(new Dimension(screenWidth, screenHeight));
		window.pack();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		gameSystem.DoDraw(g);
		g.dispose();
	}
	
	/**Terminates the game process effectively closing the JPanel <br />
	 * Use carefully when calling, especially around unsaved data.
	 */
	public static void CloseGame()
	{
		SwingUtilities.getWindowAncestor(Instance).dispose();
	}
}
