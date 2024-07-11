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
	
	final public static String GameName = "ClinicPuzzelGame"; //Shouldn't have spaces
	
	final public static int maxTilesX = 12;
	
	final public static int maxTilesY = 10;
		
	final private static int MAXPLAYERS = 1;
	
	public static Player[] player = new Player[MAXPLAYERS]; //Not multiplayer so we only need 1 player
	
	/** Used to initiate variables and other bits of memory/information when openning the game <br />
	 * Runs when the game opens or until the Instance of the game is loaded.
	 * Make sure that 'Instance = this;' is the last line of this method
	 */ 
	public void Initialisation()
	{
		FileIO.CheckFolderspace();
		AssetBank.InitiliseTextures();
		Logging.Log("Initialised Textures", LoggingType.Base);
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			player[plr] = new Player();
		}
		Logging.Log("Initialised Players", LoggingType.Base);
		//Code runs here
		Logging.Log("Initialisation Finished", LoggingType.Base);
		Instance = this;
	}
	
	/**Runs every frame, used for literally anything <br />
	 * make sure you have conditions around certain actions as memory leaks are very common when working with 
	 * unsafe code inside of update methods
	 */
	public void Update()
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			player[plr].Update();
		}
	}
	
	/**The method that does all of the game's drawing. This is seperate from Update as it repaints the screen accordingly to every update. <br />
	 * NOTE: ANYTHING DRAWN OUTSIDE OF THIS METHOD **NOT** BE UPDATED
	 */
	public void DoDraw(Graphics graphics)
	{
		DrawTiles(graphics);
		DrawPlayers(graphics);
	}
	
	public void DrawTiles(Graphics graphics)
	{
		int mapX = 0;
		int mapY = 0;
		
		while (mapX < maxTilesX && mapY < maxTilesY)
		{
			graphics.drawImage(AssetBank.TileTestFloor, mapX * 32, mapY * 32, 32, 32, null);
			mapX++;
			if (mapX >= maxTilesX)
			{
				mapX = 0;
				mapY++;
			}
		}
	}
	
	public void DrawPlayers(Graphics graphics)
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			graphics.drawImage(AssetBank.PlayerTest, player[plr].x, player[plr].y, 32, 32, null);
		}
	}
}
