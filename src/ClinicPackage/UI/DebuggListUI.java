package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class DebuggListUI extends UIElement {
	public int panelSelected = 0;

	public int maxPanels = 2;
	
	private int[] panelX = new int[maxPanels];
	
	private int[] panelY = new int[maxPanels];
	
	private int[] panelWidth = new int[maxPanels];
	
	private int[] panelHeight = new int[maxPanels];
	
	private String[] panelText = new String[maxPanels];
	
	private DebuggMenuUI uiParent = null;
	
	public DebuggListUI(DebuggMenuUI debuggButton)
	{
		uiParent = debuggButton;
	}
	
	@Override
	public void SetStaticDefaults()
	{
		KeyboardInputs = false;
		
		Width = 8 * 32;
		Height = Main.ScreenHeight - 32;
		x = Main.ScreenWidth - Width - 16;
		y = 0;
		
		panelText[0] = "Level Editor";
		panelText[maxPanels - 1] = "Back";
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image img = Texture2D.Get("TestUIPanel");
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		for (int d = 0; d < maxPanels; d++)
		{
			panelWidth[d] = 7 * 32;
			panelHeight[d] = 3 * 32;
			panelX[d] = x + 16;
			
			if (d == maxPanels - 1) //Back Button
			{
				panelY[d] = y + Height - panelHeight[d];
				UIElement.DrawPanel(g, panelSelected == d ? img : imgIn, panelX[d], panelY[d], panelWidth[d], panelHeight[d]);
			}
			else
			{
				panelY[d] = y + 16 + (panelHeight[d] * d);
				UIElement.DrawPanel(g, panelSelected == d ? img : imgIn, panelX[d], panelY[d], panelWidth[d], panelHeight[d]);
			}
			
			float textScale = 2f;
			String text = panelText[d];
			Image Text = Main.texture2D.DrawText(text, -1, -1, Color.WHITE);
			int textX = (int)(panelX[d] + (panelWidth[d] / 2) - (Text.getWidth(null) * textScale / 2));
			int textY = (int)(panelY[d] + (panelHeight[d] / 2) - (Text.getHeight(null) * textScale / 2));
			Image Textbg = Main.texture2D.DrawText(text, -1, -1, Color.BLACK);
			Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
			Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
		}
	}
	
	@Override
	public void Update()
	{
		if (Main.InsideRectangle(new Rectangle(x, y, Width, Height), Main.MouseWorld))
		{
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
							case 0:
								LevelEditorUI ui = new LevelEditorUI();
								Main.UI.Apphend(ui);
								Main.BigDebugMenuIsOpen = true;
								Main.LevelEditorOpen = true;
								Main.DebugIconOpen = false;
								uiParent.CloseRequest();
								CloseRequest();
								break;
							case 1:
								uiParent.ListOpen = false;
								KeyInputDelay = 30;
								CloseRequest();
								break;
						}
					}
				}
			}
		}
	}
}
