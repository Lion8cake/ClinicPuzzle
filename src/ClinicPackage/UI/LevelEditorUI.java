package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Hashtable;
import java.util.function.Supplier;

import ClinicPackage.*;
import ClinicPackage.IDs.FurnitureID;
import ClinicPackage.IDs.TileID;
import Lion8cake.Texture2D;

public class LevelEditorUI extends UIElement {

	//Panels
	public int maxPanels = 7;
	public int panelSelected = maxPanels;
	private int[] panelX = new int[maxPanels];
	private int[] panelY = new int[maxPanels];
	private int[] panelWidth = new int[maxPanels];
	private int[] panelHeight = new int[maxPanels];
	private String[] panelText = new String[maxPanels];

	//Tiles UI
	public int maxTiles;
	public int tilesSelected = -1;
	private int[] tileX;
	private int[] tileY;
	private int[] tileWidth;
	private int[] tileHeight;
	
	//Tiles
	private boolean TileSet = true;
	private boolean LoadedTiles = false;
	private int mouseHeldTile = -1;
	private int mouseHeldFrameX = 0;
	private int mouseHeldFrameY = 0;
	
	//Values edited through text
	public boolean textFieldOpen = false;
	public int roomWidth = 1;
	public int roomHeight = 1;
	public File importedSave = null;
	public Path exportedSave = null;
	
	//Virtual Space
	public boolean RefreshRoom = true;
	public Tile[][] virtualtiles = new Tile[Main.MAXIMUMTILEX][Main.MAXIMUMTILEY];
	public Furniture[][] virtualfurn = new Furniture[Main.MAXIMUMTILEX][Main.MAXIMUMTILEY];
	private static Hashtable<String, BufferedImage> roomDictionary = new Hashtable<String, BufferedImage>();
	private int mapX = 0;
	private int mapY = 0;
	
	@Override
	public void SetStaticDefaults() {
		KeyboardInputs = true;
		Width = Main.ScreenWidth - 32;
		Height = Main.ScreenHeight - 32;
		x = 8;
		y = 8;
		
		panelText[0] = "Width: ";
		panelText[1] = "Height: ";
		panelText[2] = "Import Room";
		panelText[3] = "Save Room";
		panelText[4] = "Tiles";
		panelText[5] = "Furniture";
		panelText[6] = "Back";
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		int RPBwidth = 10 * 32;
		int RPBheight = Height - 32;
		int RPBx = x + (Width - RPBwidth) - 16;
		int RPBy = y + 16;
		UIElement.DrawPanel(g, imgIn, RPBx, RPBy, RPBwidth, RPBheight);
		
		//Virtual Room Space
		int bgX = x + 10;
		int bgY = y + 16;
		int bgWidth = 20 * 32;
		int bgHeight = 17 * 32;
		Color oldcolor = g.getColor();
		g.setColor(Color.black);
		g.fillRect(bgX, bgY, bgWidth, bgHeight);
		g.setColor(oldcolor);
		
		String key = "";
		for (int k = 0; k < roomWidth; k++)
		{
			for (int l = 0; l < roomHeight; l++)
			{
				if (virtualtiles[k][l] != null && virtualfurn[k][l] != null)
				{
					key += virtualtiles[k][l].Type;
					key += virtualfurn[k][l].Type;
					key += virtualfurn[k][l].xFrame;
					key += virtualfurn[k][l].yFrame;
				}
			}
		}
		key += roomWidth + roomHeight + "roomImage";
		BufferedImage MapDisplay = roomDictionary.get(key);
		float tileSize = 1f;
		if (roomWidth > 20 || roomHeight > 16)
			tileSize = 0.5f;
		if (MapDisplay == null)
		{
			MapDisplay = new BufferedImage(roomWidth * 32, roomHeight * 32, BufferedImage.TYPE_INT_RGB);
			Graphics g2 = MapDisplay.createGraphics();
			for (int i = 0; i < roomWidth; i++)
			{
				for (int j = 0; j < roomHeight; j++)
				{
					Image value = null;
					int k = 32 * i;
					int l = 32 * j;
					if (virtualtiles[i][j] != null)
					{
						value = Main.TileImage(virtualtiles[i][j].Type);
					}
					if (value != null)
					{
						Texture2D.DrawStaticAsset(g2, value, k, l, null, 1f, 1f);
					}
					Rectangle frame = null;
					if (virtualfurn[i][j] != null)
					{
						frame = new Rectangle(virtualfurn[i][j].xFrame, virtualfurn[i][j].yFrame, 32, 32);
						value = Main.FurnitureImage(virtualfurn[i][j].Type);
					}
					if (value != null && frame != null)
					{
						Texture2D.DrawStaticAsset(g2, value, k, l, frame, 1f, 1f);
					}
				}
			}
			g2.dispose();
			roomDictionary.put(key, MapDisplay);
		}
		mapX = (int)(bgX + (bgWidth - MapDisplay.getWidth() * tileSize) / 2);
		mapY = (int)(bgY + (bgHeight - MapDisplay.getHeight() * tileSize) / 2);
		Texture2D.DrawStaticAsset(g, MapDisplay, mapX, mapY, null, tileSize, tileSize);

		int grid;
        for (grid = 0; grid < roomHeight + 1; grid++) {
            g.drawLine(mapX, mapY + grid * (int)(32 * tileSize), mapX + (int)(32 * tileSize) * roomWidth, mapY + grid * (int)(32 * tileSize));
        }
        for (grid = 0; grid < roomWidth + 1; grid++) {
            g.drawLine(mapX + grid * (int)(32 * tileSize), mapY, mapX + grid * (int)(32 * tileSize), mapY + roomHeight * (int)(32 * tileSize));
        }
		
		for (int d = 0; d < maxPanels; d++)
		{
			if (d != 6)
				panelWidth[d] = 5 * 32;
			panelHeight[d] = 3 * 32;
			switch (d)
			{
				case 0: 
					panelDrawing(g, d, x + 8, y + 16 + 17 * 32 + 16);
					break;
				case 1: 
					panelDrawing(g, d, x + 8 + panelWidth[d], y + 16 + 17 * 32 + 16);
					break;
				case 2: 
					panelDrawing(g, d, x + 8 + (panelWidth[d] * 2), y + 16 + 17 * 32 + 16);
					break;
				case 3: 
					panelDrawing(g, d, x + 8 + (panelWidth[d] * 3), y + 16 + 17 * 32 + 16);
					break;
				case 4:
					panelDrawing(g, d, RPBx, RPBy);
					break;
				case 5:
					panelDrawing(g, d, RPBx + (RPBwidth / 2), RPBy);
					break;
				case 6:
					panelWidth[d] = 9 * 32;
					panelDrawing(g, d, RPBx + 16, RPBy + RPBheight - panelHeight[d]);
					break;
			}
		}
		
		if (LoadedTiles)
		{
			for (int t = 0; t < maxTiles; t++)
			{
				int tX = t % 4;
				int tY = 0;
				int row = Math.toIntExact(t / 4);
				tY = (18 * row) * 4;
				Image tile = TileSet ? Main.TileImage(t) : Main.FurnitureImage(t);
				tileX[t] = RPBx + 16 + (32 * tX) * 2 + (8 * tX);
				tileY[t] = RPBy + 16 + 3 * 32 + tY;
				tileWidth[t] = 32;
				tileHeight[t] = 32;
				BufferedImage Select = Texture2D.ColoredRect(17, 17, Color.white);
				BufferedImage tileGotten = Texture2D.ColoredRect(17, 17, Color.yellow);
				if (t == tilesSelected)
					Texture2D.DrawStaticAsset(g, Select, tileX[t] - 1, tileY[t] - 1, null, 4f, 4f);
				if (t == mouseHeldTile)
					Texture2D.DrawStaticAsset(g, tileGotten, tileX[t] - 1, tileY[t] - 1, null, 4f, 4f);
				Texture2D.DrawStaticAsset(g, tile, tileX[t], tileY[t], new Rectangle(t == mouseHeldTile ? mouseHeldFrameX : 0, t == mouseHeldTile ? mouseHeldFrameY : 0, tileWidth[t], tileHeight[t]), 2f, 2f);
			}
		}
		
		float textScale = 2f;
		String text = "Welcome to the level editor, used to create levels/rooms. Does not include NPC placement or interactable points";
		Image Text = Main.texture2D.DrawText(text, 10 * 32, 3 * 32, Color.WHITE);
		int textX = x + 16;
		int textY = y + 32 + 20 * 32;
		Image Textbg = Main.texture2D.DrawText(text, 10 * 32, 3 * 32, Color.BLACK);
		Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
		Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
	}
	
	private void panelDrawing(Graphics g, int panel, int x, int y)
	{
		Image img = Texture2D.Get("TestUIPanel");
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		panelX[panel] = x;
		panelY[panel] = y;
		UIElement.DrawPanel(g, panel == panelSelected ? img : imgIn, panelX[panel], panelY[panel], panelWidth[panel], panelHeight[panel]);
		
		float textScale = 2f;
		String text = panelText[panel];
		Image Text = Main.texture2D.DrawText(text, -1, -1, Color.WHITE);
		int textX = (int)(panelX[panel] + (panelWidth[panel] / 2) - (Text.getWidth(null) * textScale / 2));
		int textY = (int)(panelY[panel] + (panelHeight[panel] / 2) - (Text.getHeight(null) * textScale / 2));
		Image Textbg = Main.texture2D.DrawText(text, -1, -1, Color.BLACK);
		Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
		Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
	}
	
	@Override
	public void Update()
	{
		if (Key_Back)
		{
			BackButton();
			return;
		}
		
		if (mouseHeldTile <= -1 && !TileSet)
		{
			mouseHeldFrameX = mouseHeldFrameY = 0;
		}
		
		if (!LoadedTiles)
		{
			maxTiles = 0;
			tilesSelected = -1;
			for (maxTiles = 0; maxTiles <= (TileSet ? TileID.MaxIDs : FurnitureID.MaxIDs); maxTiles++);
			tileX = new int[maxTiles];
			tileY = new int[maxTiles];
			tileWidth = new int[maxTiles];
			tileHeight = new int[maxTiles];
			LoadedTiles = true;
		}
		if (RefreshRoom)
		{
			if (exportedSave != null)
			{
				ExportRoom(exportedSave);
				exportedSave = null;
				roomWidth = 1;
				roomHeight = 1;
				for (int m = roomWidth; m < Byte.MAX_VALUE; m++)
				{
					for (int n = roomHeight; n < Byte.MAX_VALUE; n++)
					{
						virtualtiles[m][n] = null;
						virtualfurn[m][n] = null;
					}
				}
			}
			else if (importedSave != null)
			{
				LoadVirtualRoom(importedSave);
				importedSave = null;
			}
			else
			{
				for (int i = 0; i < roomWidth; i++)
				{
					for (int j = 0; j < roomHeight; j++)
					{
						virtualtiles[i][j] = new Tile();
						virtualfurn[i][j] = new Furniture();
					}
				}
			}
			RefreshRoom = false;
		}
		
		int panelsUnselected = 0;
		for (int pan = 0; pan < maxPanels; pan++)
		{
			if (Main.InsideRectangle(new Rectangle(panelX[pan], panelY[pan], panelWidth[pan], panelHeight[pan]), Main.MouseWorld))
			{
				panelSelected = pan;
				if (Main.MouseClicked && KeyInputDelay <= 0)
				{
					KeyInputDelay = 7;
					switch (pan)
					{
						case 0: //Width
							if (!textFieldOpen)
							{
								LevelEditorTextUI ui = new LevelEditorTextUI(this, 0);
								Main.UI.Apphend(ui);
								textFieldOpen = true;
							}
							break;
						case 1: //Height
							if (!textFieldOpen)
							{
								LevelEditorTextUI ui = new LevelEditorTextUI(this, 1);
								Main.UI.Apphend(ui);
								textFieldOpen = true;
							}
							break;
						case 2: //Import
							if (!textFieldOpen)
							{
								LevelEditorTextUI ui = new LevelEditorTextUI(this, 2);
								Main.UI.Apphend(ui);
								textFieldOpen = true;
							}
							break;
						case 3: //Export
							if (!textFieldOpen)
							{
								LevelEditorTextUI ui = new LevelEditorTextUI(this, 3);
								Main.UI.Apphend(ui);
								textFieldOpen = true;
							}
							break;
						case 4: //Tiles
							TileSet = true;
							mouseHeldTile = -1;
							LoadedTiles = false;
							break;
						case 5: //Furniture
							TileSet = false;
							mouseHeldTile = -1;
							LoadedTiles = false;
							break;
						case 6: //Leave
							BackButton();
							break;
					}
				}
			}
			else
			{
				panelsUnselected++;
			}
		}
		if (panelsUnselected >= maxPanels)
		{
			panelSelected = -1;
		}
		
		float tileSize = 1f;
		if (roomWidth > 20 || roomHeight > 16)
			tileSize = 0.5f;
		for (int g = 0; g < roomWidth; g++)
		{
			for (int h = 0; h < roomHeight; h++)
			{
				if (Main.InsideRectangle(new Rectangle((int)(mapX + (32 * g) * tileSize), (int)(mapY + (32 * h) * tileSize), (int)(32 * tileSize), (int)(32 * tileSize)), Main.MouseWorld))
				{
					if ((Main.MouseClicked || Main.MouseHeld) && mouseHeldTile > -1)
					{
						
						if (TileSet)
						{
							virtualtiles[g][h].Type = mouseHeldTile;
						}
						else
						{
							virtualfurn[g][h].Type = mouseHeldTile;
							virtualfurn[g][h].xFrame = mouseHeldFrameX;
							virtualfurn[g][h].yFrame = mouseHeldFrameY;
						}
					}
				}
			}
		}
		
		int tilesUnselected = 0;
		for (int t = 0; t < maxTiles; t++)
		{
			if (Main.InsideRectangle(new Rectangle(tileX[t], tileY[t], tileWidth[t] * 2, tileHeight[t] * 2), Main.MouseWorld))
			{
				tilesSelected = t;
				if (Main.MouseClicked && KeyInputDelay <= 0)
				{
					mouseHeldTile = tilesSelected;
					mouseHeldFrameX = mouseHeldFrameY = 0;
					KeyInputDelay = 7;
				}
			}
			else
			{
				tilesUnselected++;
			}
		}
		if (tilesUnselected >= maxTiles)
		{
			tilesSelected = -1;
		}
		
		if (!TileSet)
		{
			int heldTileWidth = Main.FurnitureImage(mouseHeldTile).getWidth(null);
			int heldTileHeight = Main.FurnitureImage(mouseHeldTile).getHeight(null);
			if (KeyInputDelay <= 0)
			{
				if (Key_Right)
				{
					if (mouseHeldFrameX + 32 >= heldTileWidth)
					{
						mouseHeldFrameX = 0;
					}
					else
					{
						mouseHeldFrameX += 32;
					}
					KeyInputDelay = 7;
				}
				if (Key_Left)
				{
					if (mouseHeldFrameX - 32 < 0)
					{
						mouseHeldFrameX = heldTileWidth - 32;
					}
					else
					{
						mouseHeldFrameX -= 32;
					}
					KeyInputDelay = 7;
				}
				if (Key_Down)
				{
					if (mouseHeldFrameY + 32 >= heldTileHeight)
					{
						mouseHeldFrameY = 0;
					}
					else
					{
						mouseHeldFrameY += 32;
					}
					KeyInputDelay = 7;
				}
				if (Key_Up)
				{
					if (mouseHeldFrameY - 32 < 0)
					{
						mouseHeldFrameY = heldTileHeight - 32;
					}
					else
					{
						mouseHeldFrameY -= 32;
					}
					KeyInputDelay = 7;
				}
			}
		}
		panelText[0] = "Width: " + roomWidth;
		panelText[1] = "Height: " + roomHeight;
	}
	
	private void BackButton()
	{
		if (roomWidth == 1 && roomHeight == 1 && virtualtiles[0][0].Type == 0 && virtualfurn[0][0].Type == 0)
		{
			Main.LevelEditorOpen = false;
			Main.Instance.BackToMainMenu();
			CloseRequest();
		}
		else
		{
			Supplier<Runnable> d = () -> PopUpLeaveSurability(this);
			PopupUI popUp = new PopupUI("Do you want to leave? There is an unsaved room.", d);
			Main.UI.Apphend(popUp);
		}
		KeyInputDelay = 14;
	}
	
	private Runnable PopUpLeaveSurability(LevelEditorUI parent) {
		Main.LevelEditorOpen = false;
		Main.Instance.BackToMainMenu();
		parent.CloseRequest();
		return null;
	}
	
	private void LoadVirtualRoom(File LoadFile)
	{
		try
		{
			if (LoadFile != null)
			{
				BufferedReader reader = new BufferedReader(new FileReader(LoadFile));
				
				int mapX = 0;
				int mapY = 0;
				
				roomWidth = Integer.parseInt(reader.readLine());
				roomHeight = Integer.parseInt(reader.readLine());
				while (mapX < roomWidth && mapY < roomHeight)
				{
					String line = reader.readLine();
					
					while (mapX < roomWidth)
					{
						String numbers[] = line.split(" ");
						int num = Integer.parseInt(numbers[mapX]);
						virtualtiles[mapX][mapY] = new Tile();
						virtualtiles[mapX][mapY].Type = num;
						mapX++;
					}
					if (mapX >= roomWidth)
					{
						mapX = 0;
						mapY++;
					}
				}
				mapX = 0;
				mapY = 0;
				while (mapX < roomWidth && mapY < roomHeight)
				{
					String line = reader.readLine();
					int furnXtextpos = 0;
					
					while (mapX < roomWidth)
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
									virtualfurn[mapX][mapY] = new Furniture();
									for (int j = 0; j < numbers.length; j++)
									{
										int num = Integer.parseInt(numbers[j]);
										switch (j)
										{
											case 0:
												virtualfurn[mapX][mapY].Type = num;
												break;
											case 1:
												virtualfurn[mapX][mapY].xFrame = num;
												break;
											case 2:
												virtualfurn[mapX][mapY].yFrame = num;
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
					if (mapX >= roomWidth)
					{
						mapX = 0;
						mapY++;
					}
				}
				reader.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void ExportRoom(Path path)
	{
		if (Files.exists(path))
		{
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.createFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Files.exists(path))
		{
			try {
				FileChannel.open(path, StandardOpenOption.WRITE).truncate(0).close();
				PrintWriter rld = new PrintWriter(new BufferedWriter(new FileWriter(path.toString(), true)));
				rld.println(roomWidth);
				rld.println(roomHeight);
				for (int j = 0; j < roomHeight; j++)
				{
					String line = "";
					for (int i = 0; i < roomWidth; i++)
					{
						line += virtualtiles[i][j].Type;
						if (i < roomWidth)
							line += " ";
					}
					rld.println(line);
				}
				for (int j = 0; j < roomHeight; j++)
				{
					String line = "";
					for (int i = 0; i < roomWidth; i++)
					{
						line += "[" + virtualfurn[i][j].Type + " " + virtualfurn[i][j].xFrame + " " + virtualfurn[i][j].yFrame + "]";
					}
					rld.println(line);
				}
				rld.close();
				Logging.Log("Saved room under the name: " + path.toString(), Logging.LoggingType.File);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			Logging.Log("How did we get here? created file was not found, most likely failed to create the file.", Logging.LoggingType.File);
	}
}
