package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import ClinicPackage.Main;
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
	
	//Values edited through text
	public boolean textFieldOpen = false;
	public int roomWidth = 1;
	public int roomHeight = 1;
	
	@Override
	public void SetStaticDefaults() {
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
		Image img = Texture2D.Get("TestUIPanel");
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		int RPBwidth = 10 * 32;
		int RPBheight = Height - 32;
		int RPBx = x + (Width - RPBwidth) - 16;
		int RPBy = y + 16;
		UIElement.DrawPanel(g, imgIn, RPBx, RPBy, RPBwidth, RPBheight);
		
		Color oldcolor = g.getColor();
		g.setColor(Color.black);
		g.fillRect(x + 10, y + 16, 20 * 32, 17 * 32);
		g.setColor(oldcolor);
		
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
				if (t % 4 == 0)
				{
					tY += (t / 4) * (18 * t);
				}
				Image tile = TileSet ? Main.TileImage(t) : Main.FurnitureImage(t);
				tileX[t] = RPBx + 16 + (32 * tX) * 2 + (8 * tX);
				tileY[t] = RPBy + 16 + 3 * 32 + tY;
				tileWidth[t] = 32;
				tileHeight[t] = 32;
				if (t == tilesSelected)
					Texture2D.DrawStaticAsset(g, img, tileX[t] - 1, tileY[t] - 1, new Rectangle(16, 16, 17, 17), 4f, 4f);
				Texture2D.DrawStaticAsset(g, tile, tileX[t], tileY[t], new Rectangle(0, 0, tileWidth[t], tileHeight[t]), 2f, 2f);
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
							break;
						case 3: //Export
							break;
						case 4: //Tiles
							TileSet = true;
							LoadedTiles = false;
							break;
						case 5: //Furniture
							TileSet = false;
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
		
		int tilesUnselected = 0;
		for (int t = 0; t < maxTiles; t++)
		{
			if (Main.InsideRectangle(new Rectangle(tileX[t], tileY[t], tileWidth[t] * 2, tileHeight[t] * 2), Main.MouseWorld))
			{
				tilesSelected = t;
				if (Main.MouseClicked && KeyInputDelay <= 0)
				{
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
		
		panelText[0] = "Width: " + roomWidth;
		panelText[1] = "Height: " + roomHeight;
	}
	
	private void BackButton()
	{
		Main.LevelEditorOpen = false;
		Main.Instance.BackToMainMenu();
		CloseRequest();
	}
}
