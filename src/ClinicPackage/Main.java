package ClinicPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ClinicPackage.Logging.*;
import ClinicPackage.IDs.*;
import ClinicPackage.IO.*;
import ClinicPackage.UI.*;
import Lion8cake.Texture2D;

public class Main 
{
	/**The instance of Main when running the game.
	 * Used to get anything that isn't static inside of Main.
	 */
	public static Main Instance;
	
	final public static String GameName = "ClinicPuzzelGame"; //Shouldn't have spaces
	
	public static Texture2D texture2D;
	
	public static boolean InGame = false;
	
	//files
	public static boolean CreateGame = false;
	
	public static int LoadedSave = -1;
	
	public boolean[] savefileExists = new boolean[5]; //5 save files, checks for if a file exists

	//Window Size
	public static int ScreenWidth;
	
	public static int ScreenHeight;
	
	public static boolean ResolutionChange;
	
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
	
	public static Furniture[][] furniture = new Furniture[MAXIMUMTILEX][MAXIMUMTILEY];
	
	public static int tileSize = 32;
	
	//Tile Settings
	public static boolean[] tileSolid = new boolean[Byte.MAX_VALUE];
	
	public static boolean[] furnitureInteractable = new boolean[Byte.MAX_VALUE];
	
	public static boolean[] furnitureSolid = new boolean[Byte.MAX_VALUE];
	
	//Rooms
	public int RoomID = 2;
	
	//Players
	final private static int MAXPLAYERS = 1; //Not multiplayer so we only need 1 player
	
	public static Player[] player = new Player[MAXPLAYERS]; 
	
	public static int myPlayer;
	
	//UIs
	public static UIElement UI;
	
	public static boolean textBoxOpen = false;
	
	public int Menu = 0;
	
	public boolean InMainMenu = false;
	
	public static boolean MenuUIActive = false;
	
	//Settings
	public static int Sound = 0;
	
	public static int Music = 0;
	
	public static int ResolutionType = 0;
	
	/** Used to initiate variables and other bits of memory/information when openning the game <br />
	 * Runs when the game opens or until the Instance of the game is loaded.
	 * Make sure that 'Instance = this;' is the last line of this method
	 */ 
	public void Initialisation()
	{
		Instance = this;
		FileIO.CheckFolderspace();
		OptionsIO.LoadSettings();
		SaveFileIO.FileSavesSettup();
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
		InMainMenu = true;
		//LoadRoom();
		InitiateTileSettings();
		texture2D = new Texture2D();
		texture2D.FontFileName = "CatCodeFont";
		texture2D.drawScale = drawScale;
		UI = new UIElement();
		
		//Code runs here
		Logging.Log("Initialisation Finished", LoggingType.Base);
	}
	
	/**Contains the settings for both tiles and furniture.
	 */
	public void InitiateTileSettings()
	{
		//Defaults
		for (int i = 0; i < tileSolid.length; i++)
		{
			tileSolid[i] = true;
			furnitureInteractable[i] = false;
			furnitureSolid[i] = true;
		}
		//Settings
		tileSolid[1] = false;
		furnitureInteractable[2] = true;
		furnitureSolid[0] = false;
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
			mapX = 0;
			mapY = 0;
			while (mapX < maxTilesX && mapY < maxTilesY)
			{
				String line = reader.readLine();
				
				while (mapX < maxTilesX)
				{
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[mapX]);
					furniture[mapX][mapY] = new Furniture();
					furniture[mapX][mapY].Type = num;
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
	
	public void UnloadRoom()
	{
		for (int x = 0; x < Byte.MAX_VALUE; x++)
		{
			for (int y = 0; y < Byte.MAX_VALUE; y++)
			{
				tile[x][y] = null;
				furniture[x][y] = null;
			}
		}
		maxTilesX = 1;
		maxTilesY = 1;
	}
	
	public void LoadGame()
	{
		Menu = -1;
		InMainMenu = false;
		LoadRoom();
	}
	
	public void DoMainMenu()
	{
		MenuUI mainMenu = null;
		if (!MenuUIActive)
		{
			mainMenu = new MenuUI(Menu);
			MenuUIActive = true;
		}
		if (mainMenu != null)
			UI.Apphend(mainMenu);
	}
	
	public void ChangeMainMenu(int Type)
	{
		Menu = Type;
		MenuUIActive = false;
	}
	
	public void BackToMainMenu()
	{
		InMainMenu = true;
		Menu = 0;
		MenuUIActive = false;
		UnloadRoom();
	}
	
	/**Runs every frame, used for literally anything <br />
	 * make sure you have conditions around certain actions as memory leaks are very common when working with 
	 * unsafe code inside of update methods
	 */
	public void Update()
	{
		if (InMainMenu || Menu == 2 || Menu == 1 || Menu == 3)
		{
			DoMainMenu();
		}
		else
		{
			for (int plr = 0; plr < MAXPLAYERS; plr++)
			{
				player[plr].Update();
			}
		}
		InGame = !InMainMenu;
		tileSize = (int)(32 * drawScale);
		renderDistX = (int)(renderX * drawScale);
		renderDistY = (int)(renderY * drawScale);
		cameraX = (int)(player[myPlayer].x * drawScale);
		cameraY = (int)(player[myPlayer].y * drawScale);
		cameraCenteredX = ScreenWidth / 2;
		cameraCenteredY = ScreenHeight / 2;
		textBoxOpen = false;
		UI.UIUpdate();
		switch(ResolutionType)
		{
			case 0: 
				Game.screenWidth = 32 * 32;
				Game.screenHeight = 32 * 24;
				break;
			case 1:
				Game.screenWidth = 32 * 48;
				Game.screenHeight = 32 * 32;
				break;
			case 2:
				Game.screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
				Game.screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
				break;
			case 3:
				Game.screenWidth = 32 * 16;
				Game.screenHeight = 32 * 12;
				break;
		}
		if (ResolutionType > 3)
			ResolutionType = 0;
		ResolutionChange = false;
		if (InGame && Player.kESC && Menu != 2)
		{
			Menu = 2;
		}
	}
	
	/**The method that does all of the game's drawing. This is seperate from Update as it repaints the screen accordingly to every update. <br />
	 * NOTE: ANYTHING DRAWN OUTSIDE OF THIS METHOD **NOT** BE UPDATED
	 */
	public void DoDraw(Graphics graphics)
	{
		DrawGame(graphics);
		UI.UIDraw(graphics);
	}
	
	public void DrawGame(Graphics graphics)
	{
		RenderTileArray(graphics);
		if (!InMainMenu)
		{
			DrawPlayers(graphics);
		}
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
					DrawFurniture(graphics, mapX, mapY);
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
		}
		if (value != null)
		{
			texture2D.DrawAsset(graphics, value, i, j, 1f);
		}
	}
	
	public void DrawFurniture(Graphics graphics, int x, int y)
	{
		Image value = null;
		int i = (x * tileSize) - cameraX + (cameraCenteredX - tileSize / 2);
		int j = (y * tileSize) - cameraY + (cameraCenteredY - tileSize / 2);
		if (furniture[x][y] != null)
		{
			if (furniture[x][y].Type == FurnitureID.TestFurnTop)
			{
				value = Texture2D.Get("TestFurn1");
			}
			else if (furniture[x][y].Type == FurnitureID.TestFurnTop2)
			{
				value = Texture2D.Get("TestFurn2");
			}
			else if (furniture[x][y].Type == FurnitureID.TestFurnMid)
			{
				value = Texture2D.Get("TestFurn3");
			}
			else if (furniture[x][y].Type == FurnitureID.TestFurnMid2)
			{
				value = Texture2D.Get("TestFurn4");
			}
			else if (furniture[x][y].Type == FurnitureID.TestFurnBottom)
			{
				value = Texture2D.Get("TestFurn5");
			}
			else if (furniture[x][y].Type == FurnitureID.TestFurnBottom2)
			{
				value = Texture2D.Get("TestFurn6");
			}
			else if (furniture[x][y].Type == FurnitureID.TestObject)
			{
				value = Texture2D.Get("InteractionObjectTest");
			}
		}
		if (value != null)
		{
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
	
	/**Optimised code by https://stackoverflow.com/questions/36599547/adding-blur-effect-to-bufferredimage-in-java
	 * @param img
	 * @return
	 */
	public BufferedImage BlurrImage(BufferedImage img)
	{
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int raster = 0; raster < img.getRaster().getNumBands(); raster++)
		{
		    for (int x = 1; x < img.getWidth() - 1; x++)
		    {
		        for (int y = 1; y < img.getHeight() - 1; y++)
		        {
		            int newPixel = 0;
		            for (int i = -1 ; i <= 1 ; i++)
		                for (int j = -1 ; j <= 1 ; j++)
		                    newPixel += img.getRaster().getSample(x + i, y + j, raster);
		            newPixel = (int)(newPixel / 9.0 + 0.5);
		            result.getRaster().setSample(x, y, raster, newPixel);
		         }
		    }
		}
		return result;
	}
	
	public static boolean IsStringNumeric(String[] string)
	{
		try {
			for (int i = 0; i < string.length; i++)
			{
				Integer.parseInt(string[i]);
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static void TileInteraction(Player player, int type, int x, int y)
	{
		if (type == FurnitureID.TestObject)
		{
			if (!textBoxOpen)
			{
				TextBoxUI textBox = new TextBoxUI(player, new String[] { "Sup", "How's your day been so far?", "hmm", "welp im just chilling in this void", "no clue where the rest of the world is", "bye" }, new int[] {0, 0, 0, 0, 0, 0}, new int[] {0, 0, 1, 0, 2, 0},  new String[] {"PortraitTest", "", "", ""});
				UI.Apphend(textBox);
			}
		}
	}
}