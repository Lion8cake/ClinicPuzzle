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
	
	public static float drawScale = 1f;
	
	//Distance rendered around the player
	private static int renderDistX;
	
	private static int renderDistY;
	
	public static int renderX = 12;
	
	public static int renderY = 12;
	
	//Tiles
	final private static int MAXIMUMTILEX = Byte.MAX_VALUE;
	
	final private static int MAXIMUMTILEY = Byte.MAX_VALUE;
	
	public static int maxTilesX = 1;
	
	public static int maxTilesY = 1;
	
	public static Tile[][] tile = new Tile[MAXIMUMTILEX][MAXIMUMTILEY];
	
	public static int tileSize = 32;
	
	public static boolean[] tileSolid = new boolean[3];
	
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
		InitiateTileSettings();
		//Code runs here
		Logging.Log("Initialisation Finished", LoggingType.Base);
		Instance = this;
	}
	
	public void InitiateTileSettings()
	{
		tileSolid[0] = true;
		tileSolid[1] = false;
		tileSolid[2] = true;
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
		tileSize = (int)(32 * drawScale);
		renderDistX = (int)(renderX * drawScale);
		renderDistY = (int)(renderY * drawScale);
		cameraX = (int)(player[myPlayer].x * drawScale);
		cameraY = (int)(player[myPlayer].y * drawScale);
		//Logging.Log("Scale: " + drawScale);
	}
	
	/**The method that does all of the game's drawing. This is seperate from Update as it repaints the screen accordingly to every update. <br />
	 * NOTE: ANYTHING DRAWN OUTSIDE OF THIS METHOD **NOT** BE UPDATED
	 */
	public void DoDraw(Graphics graphics)
	{
		RenderTileArray(graphics);
		DrawPlayers(graphics);
	}
	
	public static void DrawAsset(Graphics graphics, Image image, int x, int y, float Scale)
	{
		int sizeX = (int)(image.getWidth(null) * drawScale * Scale);
		int sizeY = (int)(image.getHeight(null) * drawScale * Scale);
		graphics.drawImage(image, x, y, sizeX, sizeY, null);
	}
	
	public void RenderTileArray(Graphics graphics)
	{
		for (int mapX = (cameraX / tileSize) - renderDistX; mapX < (cameraX / tileSize) + 1 + renderDistX; mapX++)
		{
			for (int mapY = (cameraY / tileSize) - renderDistY; mapY < (cameraY / tileSize) + 1 + renderDistY; mapY++)
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
		int i = (x * tileSize) - cameraX + (cameraCenteredX - tileSize / 2);
		int j = (y * tileSize) - cameraY + (cameraCenteredY - tileSize / 2);
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
			//graphics.drawImage(value, i, j, 32, 32, null);
			DrawAsset(graphics, value, i, j, 1f);
		}
	}
	
	public void DrawPlayers(Graphics graphics)
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			int i = (int)(player[plr].x * drawScale) - cameraX + (cameraCenteredX - tileSize / 2);
			int j = (int)(player[plr].y * drawScale) - cameraY + (cameraCenteredY - tileSize / 2);
			DrawAsset(graphics, Texture2D.Get("PlayerTest"), i, j, 1f);
			
			//Hitbox Drawing
			/*int i2 = (int)(player[plr].hitbox.x * drawScale) - cameraX + (cameraCenteredX - tileSize / 2);
			int j2 = (int)(player[plr].hitbox.y * drawScale) - cameraY + (cameraCenteredY - tileSize / 2);
			graphics.drawImage(Texture2D.Get("TestBarrier"), i2, j2, player[plr].hitbox.width, player[plr].hitbox.height, null);*/
		}
	}
	
	public static void Zoom(float zoomAmount)
	{
		drawScale += zoomAmount;
		if (drawScale < 0.5f)
			drawScale = 0.5f;
		if (drawScale > 2f)
			drawScale = 2f;
	}
}