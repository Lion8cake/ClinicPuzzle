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
	
	//Window Size
	public int ScreenWidth;
	
	public int ScreenHeight;
	
	//Camera
	public int cameraX;
	
	public int cameraY;
	
	public int cameraCenteredX;
	
	public int cameraCenteredY;
	
	public static int drawScale = 32;
	
	//Distance rendered around the player
	public static int renderDistX = 10;
	
	public static int renderDistY = 10;
	
	//Tiles
	final private static int MAXIMUMTILEX = Byte.MAX_VALUE;
	
	final private static int MAXIMUMTILEY = Byte.MAX_VALUE;
	
	public static int maxTilesX = 1;
	
	public static int maxTilesY = 1;
	
	public static Tile[][] tile = new Tile[MAXIMUMTILEX][MAXIMUMTILEY];
	
	//Rooms
	public int RoomID = 2;
	
	//Players
	final private static int MAXPLAYERS = 1; //Not multiplayer so we only need 1 player
	
	public static Player[] player = new Player[MAXPLAYERS]; 
	
	public static int myPlayer;
	
	/** Used to initiate variables and other bits of memory/information when openning the game <br />
	 * Runs when the game opens or until the Instance of the game is loaded.
	 * Make sure that 'Instance = this;' is the last line of this method
	 */ 
	public void Initialisation()
	{
		FileIO.CheckFolderspace();
		//AssetBank.InitiliseTextures(); //AssetBank used to be a place that stored frequently used textures. Removed due to Texture2D optimisations
		//Logging.Log("Initialised Textures", LoggingType.Base);
		if (player[myPlayer] == null)
		{
			player[myPlayer] = new Player();
		}
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			player[plr] = new Player();
		}
		Logging.Log("Initialised Players", LoggingType.Base);
		//Logging.Log("/RoomLayoutData/MapLayout" + RoomID + ".rld");
		cameraCenteredX = ScreenWidth / 2;
		cameraCenteredY = ScreenHeight / 2;
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
		cameraX = player[myPlayer].x;
		cameraY = player[myPlayer].y;
		Logging.Log("Scale: " + drawScale);
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
		for (int mapX = (cameraX / 32) - renderDistX; mapX < (cameraX / 32) + 1 + renderDistX; mapX++)
		{
			for (int mapY = (cameraY / 32) - renderDistY; mapY < (cameraY / 32) + 1 + renderDistY; mapY++)
			{
				if (mapX > 0 && mapY > 0 && mapX < MAXIMUMTILEX && mapY < MAXIMUMTILEY)
				{
					DrawTiles(graphics, mapX, mapY);
				}
			}
		}
	}
	
	public void DrawTiles(Graphics graphics, int x, int y)
	{
		Image value = null;
		int i = (x * 32) - cameraX + (cameraCenteredX - 16);
		int j = (y * 32) - cameraY + (cameraCenteredY - 16);
		if (tile[x][y] != null)
		{
			if (tile[x][y].Type == 1)
			{
				value = Texture2D.Get("FloorTest");
			}
			else if (tile[x][y].Type == 2)
			{
				value = Texture2D.Get("WallTest");
			}
		}
		if (value != null)
		{
			graphics.drawImage(value, i, j, 32, 32, null);
		}
	}
	
	public void DrawPlayers(Graphics graphics)
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			int i = player[plr].x - cameraX + (cameraCenteredX - 16);
			int j = player[plr].y - cameraY + (cameraCenteredY - 16);
			graphics.drawImage(Texture2D.Get("PlayerTest"), i, j, 32, 32, null);
		}
	}
	
	public static void Zoom(int zoomAmount)
	{
		drawScale += zoomAmount;
	}
}
