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

	public int maxPanels = 7;
	
	public int panelSelected = maxPanels;
	
	private int[] panelX = new int[maxPanels];
	
	private int[] panelY = new int[maxPanels];
	
	private int[] panelWidth = new int[maxPanels];
	
	private int[] panelHeight = new int[maxPanels];
	
	private String[] panelText = new String[maxPanels];
	
	private boolean TileSet = true;
	
	//private int[] tilePanels = new int[2];
	
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
		
		if (TileSet)
		{
			for (int t = 0; t <= TileID.MaxIDs; t++)
			{
				Image tile = Main.TileImage(t);
				Texture2D.DrawStaticAsset(g, tile, RPBx + 16 + (32 * t) * 2 + (8 * t), RPBy + 16 + 3 * 32, null, 2f, 2f);
			}
		}
		else
		{
			for (int t = 0; t <= FurnitureID.MaxIDs; t++)
			{
				Image furn = Main.FurnitureImage(t);
				Texture2D.DrawStaticAsset(g, furn, RPBx + 16 + (32 * t) * 2 + (8 * t), RPBy + 16 + 3 * 32, new Rectangle(0, 0, 32, 32), 2f, 2f);
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
							break;
						case 1: //Height
							break;
						case 2: //Import
							break;
						case 3: //Export
							break;
						case 4: //Tiles
							TileSet = true;
							break;
						case 5: //Furniture
							TileSet = false;
							break;
						case 6: //Leave
							BackButton();
							break;
					}
				}
			}
		}
	}
	
	private void BackButton()
	{
		Main.LevelEditorOpen = false;
		Main.Instance.BackToMainMenu();
		CloseRequest();
	}
}
