package ClinicPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
	
	//debug
	public static boolean debugg = true; //set to true when you want to have access to developer tools
	
	public static boolean BigDebugMenuIsOpen = false;
	
	public static boolean LevelEditorOpen = false;
	
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
	public final static int MAXIMUMTILEX = Byte.MAX_VALUE;
	
	public final static int MAXIMUMTILEY = Byte.MAX_VALUE;
	
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
	public int RoomID = 3;
	
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
	
	public static boolean DebugIconOpen = false;
	
	//Settings
	public static int Sound = 0;
	
	public static int Music = 0;
	
	public static int ResolutionType = 0;
	
	//Mouse
	public static Point MouseWorld = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public static boolean MouseClicked = false;
	
	public static boolean MouseHeld = false;
	
	//Text/Typing
	public static boolean IsTyping = false;
	
	public static String TypedText = "";
	
	public static boolean signaledFinishedText = false;
	
	public static boolean holdingShift = false;
	
	public static boolean capsLock = false;
	
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
		tileSolid[TileID.SturdyFloor] = false;
		furnitureInteractable[FurnitureID.ThrashCan] = true;
		furnitureSolid[FurnitureID.Air] = false;
		furnitureSolid[FurnitureID.PlayerDefaultPositionPoint] = false;
	}
	
	public void LoadRoom()
	{
		try
		{
			InputStream stream = getClass().getResourceAsStream("/RoomLayoutData/MapLayout" + RoomID + ".rld");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			int mapX = 0;
			int mapY = 0;
			Point playerDefaultPosition = new Point(0, 0);
			
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
				int furnXtextpos = 0;
				
				while (mapX < maxTilesX)
				{
					boolean gettingFurn = false;
					boolean split = false;
					String furnSettings = "";
					for (int i = furnXtextpos; i < line.length() && !split; i++)
					{
						if (gettingFurn)
						{
							if (line.charAt(i) == ']')
							{
								gettingFurn = false;
								split = true;
								String numbers[] = furnSettings.split(" ");
								furniture[mapX][mapY] = new Furniture();
								for (int j = 0; j < numbers.length; j++)
								{
									int num = Integer.parseInt(numbers[j]);
									switch (j)
									{
										case 0:
											furniture[mapX][mapY].Type = num;
											if (num == FurnitureID.PlayerDefaultPositionPoint)
											{
												playerDefaultPosition = new Point(mapX * 16, mapY * 16);
											}
											break;
										case 1:
											furniture[mapX][mapY].xFrame = num;
											break;
										case 2:
											furniture[mapX][mapY].yFrame = num;
											break;
									}
								}
								furnXtextpos = i;
								mapX++;
								break;
							}
							else
							{
								furnSettings += line.charAt(i);
							}
						}
						else if (line.charAt(i) == '[')
						{
							gettingFurn = true;
						}
					}
				}
				if (mapX >= maxTilesX)
				{
					mapX = 0;
					mapY++;
				}
			}
			reader.close();
			if (player[myPlayer].x == 0 && player[myPlayer].y == 0)
			{
				player[myPlayer].x = playerDefaultPosition.x;
				player[myPlayer].y = playerDefaultPosition.y;
			}
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
		BigDebugMenuIsOpen = false;
		UnloadRoom();
	}
	
	/**Runs every frame, used for literally anything <br />
	 * make sure you have conditions around certain actions as memory leaks are very common when working with 
	 * unsafe code inside of update methods
	 */
	public void Update()
	{
		if ((InMainMenu || Menu == 2 || Menu == 1 || Menu == 3) && !BigDebugMenuIsOpen)
		{
			DoMainMenu();
		}
		else if (!LevelEditorOpen)
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
		if (!BigDebugMenuIsOpen)
		{
			if (debugg && !DebugIconOpen)
			{
				DebuggMenuUI debuggui = new DebuggMenuUI();
				UI.Apphend(debuggui);
				DebugIconOpen = true;
			}
		}
		else
		{
			Menu = -1;
		}
		if (maxTilesX > 1 && maxTilesY > 1 && tile[maxTilesX][maxTilesY] != null && tile[maxTilesX][maxTilesY] != null)
		{
			UpdateTiles();
		}
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
		if (!IsTyping && !TypedText.isEmpty())
		{
			TypedText = "";
		}
		if (!Main.IsTyping)
		{
			signaledFinishedText = false;
		}
		MouseClicked = false;
	}
	
	public void UpdateTiles()
	{
		for (int mapX = (cameraX / tileSize) - renderDistX; mapX < (cameraX / tileSize) + 1 + renderDistX; mapX++)
		{
			for (int mapY = (cameraY / tileSize) - renderDistY; mapY < (cameraY / tileSize) + 1 + renderDistY; mapY++)
			{
				if (mapX >= 0 && mapY >= 0 && mapX < MAXIMUMTILEX && mapY < MAXIMUMTILEY)
				{
					if (furniture[mapX][mapY].Type == FurnitureID.PlayerDefaultPositionPoint)
					{
						furniture[mapX][mapY].Type = FurnitureID.Air;
					}
				}
			}
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
				if (mapX >= 0 && mapY >= 0 && mapX < MAXIMUMTILEX && mapY < MAXIMUMTILEY)
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
			value = TileImage(tile[x][y].Type);
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
		Rectangle frame = null;
		if (furniture[x][y] != null)
		{
			frame = new Rectangle(furniture[x][y].xFrame, furniture[x][y].yFrame, 32, 32);
			value = FurnitureImage(furniture[x][y].Type);
		}
		if (value != null && frame != null)
		{
			texture2D.DrawAsset(graphics, value, i, j, frame, 1f);
		}
	}
	
	public static Image TileImage(int type)
	{
		if (type > 0 && type <= TileID.MaxIDs)
		{
			return Texture2D.Get("Tile" + type);
		}
		return new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static Image FurnitureImage(int type)
	{
		if (!(type == FurnitureID.PlayerDefaultPositionPoint && !LevelEditorOpen))
		{
			if (type > 0 && type <= FurnitureID.MaxIDs)
			{
				return Texture2D.Get("Furniture" + type);
			}
		}
		return new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
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
	
	public static boolean InsideRectangle(Rectangle rectTarget, Point pos)
	{
		if (pos.x > rectTarget.x + rectTarget.width || pos.y > rectTarget.y + rectTarget.height)
		{
			return false;
		}
		if (pos.x > rectTarget.x && pos.y > rectTarget.y)
		{
			return true;
		}
		return false;
	}
	
	public static boolean StringIsDigit(String str)
	{
		if (str.length() <= 0)
		{
			return false;
		}
		boolean flag = true;
		for (int i = 0; i < str.length() && flag; i++)
		{
			char chara = str.charAt(i);
			if (!Character.isDigit(chara))
			{
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	public static void TileInteraction(Player player, int type, int x, int y)
	{
		if (type == FurnitureID.ThrashCan)
		{
			if (!textBoxOpen)
			{
				TextBoxUI textBox = new TextBoxUI(player, new String[] { "Sup", "How's your day been so far?", "hmm", "welp im just chilling in this void", "no clue where the rest of the world is", "bye" }, new int[] {0, 0, 0, 0, 0, 0}, new int[] {0, 0, 1, 0, 2, 0},  new String[] {"PortraitTest", "", "", ""});
				UI.Apphend(textBox);
			}
		}
	}
}