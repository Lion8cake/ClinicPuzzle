package ClinicPackage;

import java.awt.Graphics;

import ClinicPackage.Logging.LoggingType;
import ClinicPackage.IO.FileIO;
import Lion8cake.Texture2D;

public class Main 
{
	/**The instance of Main when running the game.
	 * Used to get anything that isn't static inside of Main.
	 */
	public Main Instance;
	
	public static boolean kUp = false;
	
	public static boolean kDown = false;
	
	public static boolean kLeft = false;
	
	public static boolean kRight = false;
	
	//Remove in favor for a Player object
	public int playerX = 0;
	
	public int playerY = 0;
	
	public int playerSpeed = 4;
	
	final public static String GameName = "ClinicPuzzelGame"; //Shouldn't have spaces
	
	/** Used to initiate variables and other bits of memory/information when openning the game <br />
	 * Runs when the game opens or until the Instance of the game is loaded.
	 * Make sure that 'Instance = this;' is the last line of this method
	 */ 
	public void Initialisation()
	{
		FileIO.CheckFolderspace();
		//Code runs here
		Logging.Log("Initialised", LoggingType.Base);
		Instance = this;
	}
	
	/**Runs every frame, used for literally anything <br />
	 * make sure you have conditions around certain actions as memory leaks are very common when working with 
	 * unsafe code inside of update methods
	 */
	public void Update()
	{
		Logging.Log("Key Up is pressed: " + kUp, LoggingType.Base);
		if (kUp)
		{
			playerY -= playerSpeed;
		}
		if (kDown)
		{
			playerY += playerSpeed;
		}
		if (kLeft)
		{
			playerX -= playerSpeed;
		}
		if (kRight)
		{
			playerX += playerSpeed;
		}
	}
	
	/**The method that does all of the game's drawing. This is seperate from Update as it contains both a
	 * graphics parameter and repaints the screen accordingly to every update. <br />
	 * NOTE: ANYTHING DRAWN OUTSIDE OF THIS METHOD OR ANY METHODS CALLED INSIDE WILL **NOT** BE UPDATED
	 */
	public void DoDraw(Graphics graphics)
	{
		graphics.drawImage(Texture2D.Get("PlayerTest"), playerX, playerY, 32, 32, null);
	}
}
