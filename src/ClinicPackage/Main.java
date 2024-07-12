package ClinicPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	
	final private static int MAXIMUMTILEX = Byte.MAX_VALUE;
	
	final private static int MAXIMUMTILEY = Byte.MAX_VALUE;
	
	public static int maxTilesX = 1;
	
	public static int maxTilesY = 1;
	
	public static Tile[][] tile = new Tile[MAXIMUMTILEX][MAXIMUMTILEY];
	
	public int RoomID = 2;
	
	final private static int MAXPLAYERS = 1; //Not multiplayer so we only need 1 player
	
	public static Player[] player = new Player[MAXPLAYERS]; 
	
	/** Used to initiate variables and other bits of memory/information when openning the game <br />
	 * Runs when the game opens or until the Instance of the game is loaded.
	 * Make sure that 'Instance = this;' is the last line of this method
	 */ 
	public void Initialisation()
	{
		FileIO.CheckFolderspace();
		//AssetBank.InitiliseTextures();
		//Logging.Log("Initialised Textures", LoggingType.Base);
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			player[plr] = new Player();
		}
		Logging.Log("Initialised Players", LoggingType.Base);
		//Logging.Log("/RoomLayoutData/MapLayout" + RoomID + ".rld");
		LoadRoom();
		//Code runs here
		Logging.Log("Initialisation Finished", LoggingType.Base);
		Instance = this;
	}
	
	public void LoadRoom()
	{
		try
		{
			InputStream stream = getClass().getResourceAsStream("/RoomLayoutData/MapLayout" + RoomID + ".rld");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			int mapX = 0;
			int mapY = 0;
			
			maxTilesX = Integer.parseInt(reader.readLine());
			maxTilesY = Integer.parseInt(reader.readLine());
			while (mapX < maxTilesX && mapY < maxTilesY)
			{
				String line = reader.readLine();
				
				while (mapX < maxTilesX)
				{
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[mapX]);
					tile[mapX][mapY] = new Tile();
					tile[mapX][mapY].Type = num;
					mapX++;
				}
				if (mapX >= maxTilesX)
				{
					mapX = 0;
					mapY++;
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
		RenderTileArray(graphics);
		DrawPlayers(graphics);
	}
	
	public void RenderTileArray(Graphics graphics)
	{
		int mapX = 0;
		int mapY = 0;
		
		while (mapX < maxTilesX && mapY < maxTilesY)
		{
			DrawTiles(graphics, mapX, mapY);
			mapX++;
			if (mapX >= maxTilesX)
			{
				mapX = 0;
				mapY++;
			}
		}
	}
	
	public void DrawTiles(Graphics graphics, int x, int y)
	{
		Image value = null;
		if (tile[x][y].Type == 1)
		{
			value = Texture2D.Get("FloorTest");
		}
		else if (tile[x][y].Type == 2)
		{
			value = Texture2D.Get("WallTest");
		}
		if (value != null)
		{
			graphics.drawImage(value, x * 32, y * 32, 32, 32, null);
		}
	}
	
	public void DrawPlayers(Graphics graphics)
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			graphics.drawImage(Texture2D.Get("PlayerTest"), player[plr].x, player[plr].y, 32, 32, null);
		}
	}
}
