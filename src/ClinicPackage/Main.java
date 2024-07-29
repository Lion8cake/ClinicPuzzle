package ClinicPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ClinicPackage.Logging.LoggingType;
import ClinicPackage.IDs.TileID;
import ClinicPackage.IO.FileIO;
import ClinicPackage.UI.TextBoxUI;
import ClinicPackage.UI.UIElement;
import Lion8cake.Texture2D;

public class Main 
{
	/**The instance of Main when running the game.
	 * Used to get anything that isn't static inside of Main.
	 */
	public static Main Instance;
	
	final public static String GameName = "ClinicPuzzelGame"; //Shouldn't have spaces
	
	public static Texture2D texture2D;
	
	//Window Size
	public int ScreenWidth;
	
	public int ScreenHeight;
	
	//Camera
	public int cameraX;
	
	public int cameraY;
	
	public int cameraCenteredX;
	
	public int cameraCenteredY;
	
	public static float drawScale = 2f;
	
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
	
	public static boolean[] tileSolid = new boolean[Byte.MAX_VALUE];
	
	public static boolean[] tileInteractable = new boolean[Byte.MAX_VALUE];
	
	//Rooms
	public int RoomID = 2;
	
	//Players
	final private static int MAXPLAYERS = 1; //Not multiplayer so we only need 1 player
	
	public static Player[] player = new Player[MAXPLAYERS]; 
	
	public static int myPlayer;
	
	//UIs
	public static UIElement UI = new UIElement();
	
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
		texture2D = new Texture2D();
	
		//Code runs here
		Logging.Log("Initialisation Finished", LoggingType.Base);
		Instance = this;
	}
	
	public void InitiateTileSettings()
	{
		tileSolid[0] = true;
		tileSolid[1] = false;
		tileSolid[2] = true;
		tileSolid[3] = true;
		tileInteractable[0] = false;
		tileInteractable[1] = false;
		tileInteractable[2] = false;
		tileInteractable[3] = true;
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
		texture2D.drawScale = drawScale;
		cameraX = (int)(player[myPlayer].x * drawScale);
		cameraY = (int)(player[myPlayer].y * drawScale);
		UI.UIUpdate();
		//Logging.Log("Scale: " + drawScale);
	}
	
	/**The method that does all of the game's drawing. This is seperate from Update as it repaints the screen accordingly to every update. <br />
	 * NOTE: ANYTHING DRAWN OUTSIDE OF THIS METHOD **NOT** BE UPDATED
	 */
	public void DoDraw(Graphics graphics)
	{
		RenderTileArray(graphics);
		DrawPlayers(graphics);
		UI.UIDraw(graphics);
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
			if (tile[x][y].Type == TileID.TestFloor)
			{
				value = Texture2D.Get("FloorTest");
			}
			else if (tile[x][y].Type == TileID.TestWall)
			{
				value = Texture2D.Get("WallTest");
			}
			else if (tile[x][y].Type == TileID.TestObject)
			{
				value = Texture2D.Get("InteractionObjectTest");
			}
		}
		if (value != null)
		{
			//graphics.drawImage(value, i, j, 32, 32, null);
			texture2D.DrawAsset(graphics, value, i, j, 1f);
		}
	}
	
	public void DrawPlayers(Graphics graphics)
	{
		for (int plr = 0; plr < MAXPLAYERS; plr++)
		{
			Player playr = player[plr];
			Image img = Texture2D.Get("PlayerTest");
			
			int frameX = img.getWidth(null) / playr.frameXCount * playr.frameX;
			int frameY = img.getHeight(null) / playr.frameYCount * playr.frameY;
			int frameWidth = img.getWidth(null) / playr.frameXCount;
			int frameHeight = img.getHeight(null) / playr.frameYCount;
			playr.sourceFrame = new Rectangle(frameX, frameY, frameWidth, frameHeight);
			
			int i = (int)(playr.x * drawScale) - cameraX + (cameraCenteredX - tileSize / 2);
			int j = (int)(playr.y * drawScale) - cameraY + (cameraCenteredY - tileSize / 2);
			texture2D.DrawAsset(graphics, img, i, j, playr.sourceFrame, 1f);
			
			/*if (Player.IsInteracting)
			{
				DrawAsset(graphics, Texture2D.Get("TestBarrier"), i, j, 1f);
			}*/
			//Hitbox Drawing
			/*int i2 = (int)(player[plr].hitbox.x * drawScale) - cameraX + (cameraCenteredX - tileSize / 2);
			int j2 = (int)(player[plr].hitbox.y * drawScale) - cameraY + (cameraCenteredY - tileSize / 2);
			graphics.drawImage(Texture2D.Get("TestBarrier"), i2, j2, player[plr].hitbox.width, player[plr].hitbox.height, null);*/
		}
	}
	
	public static void Zoom(float zoomAmount)
	{
		drawScale += zoomAmount;
		if (drawScale < 1f)
			drawScale = 1f;
		if (drawScale > 4f)
			drawScale = 4f;
	}
	
	public static void TileInteraction(int type, int x, int y)
	{
		if (type == TileID.TestObject)
		{
			String str = "Interaction, accepted";
			Logging.Log(str);
			TextBoxUI textBox = new TextBoxUI(str);
			UI.Apphend(textBox);
		}
	}
}