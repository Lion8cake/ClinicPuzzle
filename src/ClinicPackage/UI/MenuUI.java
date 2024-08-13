package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import ClinicPackage.Game;
import ClinicPackage.Main;
import ClinicPackage.Player;
import Lion8cake.Texture2D;

public class MenuUI extends UIElement {
	
	public MenuUI(int menuType)
	{
		MainMenuType = menuType;
	}
	
	public int MainMenuType = -1;
	
	public int panelSelected = 0;
	
	public int maxPanels = 1;
	
	public int moveWait = 0;
	
	public String[] panelText = new String[Byte.MAX_VALUE];
	
	public int KeyInputDelay = 0;
	
	@Override
	public void SetStaticDefaults()
	{
		x = 20;
		y = 20;
		Width = 32 * 14;
		Height = 32 * 4;
		for (int i = 0; i < Byte.MAX_VALUE; i++)
		{
			panelText[i] = "";
		}
		
		if (MainMenuType == 0)
		{
			KeyInputDelay = 30;
		}
	}
	
	@Override
	public void Draw(Graphics graphics) {
		switch (MainMenuType)
		{
			case 0:
				{
					for (int d = 1; d < maxPanels + 1; d++)
					{
						BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
						BufferedImage imgIn = (BufferedImage)(Texture2D.Get("TestUIPanelInactive"));
						int i = (Main.ScreenWidth / 2) - (Width / 2);
						int j = ((Main.ScreenHeight / 2) - ((Height * maxPanels) / 2)) + (Height * (d - 1));
						if (d != panelSelected + 1)
						{
							img = imgIn;
						}
						UIElement.DrawPanel(graphics, img, i, j, Width, Height);
						
						String text = panelText[d - 1];
						int textX = i + (Width / 2) - (15 / 2);
						int textY = j + (Height / 2) - 8;
						graphics.drawString(text, textX, textY);
					}
				}
				break;
			case 1:
				for (int d = 1; d < maxPanels + 1; d++)
				{
					BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
					BufferedImage imgIn = (BufferedImage)(Texture2D.Get("TestUIPanelInactive"));
					int i = (Main.ScreenWidth / 2) - (Width / 2);
					int j = ((Main.ScreenHeight / 2) - ((Height * maxPanels) / 2)) + (Height * (d - 1));
					if (d != panelSelected + 1)
					{
						img = imgIn;
					}
					UIElement.DrawPanel(graphics, img, i, j, Width, Height);
					
					String text = panelText[d - 1];
					int textX = i + (Width / 2) - (15 / 2);
					int textY = j + (Height / 2) - 8;
					graphics.drawString(text, textX, textY);
				}
				break;
		}
	}
	
	@Override
	public void Update() {
		if (MainMenuType == -1)
		{
			CloseRequest();
			return;
		}
		PopulateText();
		switch (MainMenuType)
		{
			case 0:
				maxPanels = 3;
				break;
			case 1:
				maxPanels = 5;
				break;
		}
		
		if (Player.kSpace && KeyInputDelay <= 0)
		{
			uiActivated();
		}
		if (Player.kESC)
		{
			Back();
		}
		
		//Controls
		if (moveWait <= 0)
		{
			if (Player.kDown)
			{
				panelSelected++;
				moveWait = 10;
			}
			else if (Player.kUp)
			{
				panelSelected--;
				moveWait = 10;
			}
		}
		
		if (moveWait > 0)
			moveWait--;
		if (moveWait <= 0)
			moveWait = 0;
		
		if (KeyInputDelay > 0)
			KeyInputDelay--;
		if (KeyInputDelay <= 0)
			KeyInputDelay = 0;
		
		if (panelSelected > maxPanels - 1)
			panelSelected = 0;
		if (panelSelected < 0)
			panelSelected = maxPanels - 1;
		
		
	}
	
	private void uiActivated()
	{
		switch(MainMenuType)
		{
			case 0:
				switch (panelSelected)
				{
					case 0:
						Main.Instance.LoadGame();
						break;
					case 1:
						Main.Instance.Menu = 1;
						break;
					case 2:
						Game.CloseGame();
						break;
				}
				CloseRequest();
				Main.MenuUIActive = false;
				break;
			case 1:
				switch (panelSelected)
				{
					case 0:
						Main.Sound++;
						break;
					case 1:
						Main.Music++;
						break;
					case 2:
						Main.Instance.Menu = 2;
						break;
					case 3:
						Main.ResolutionType++;
						KeyInputDelay = 10;
						break;
					case 4:
						Back();
						break;
				}
				break;
		}
		
	}
	
	private void PopulateText()
	{
		switch(MainMenuType)
		{
			case 0:
				panelText[0] = "Play";
				panelText[1] = "Options";
				panelText[2] = "Exit";
				break;
			case 1:
				panelText[0] = "Sound: " + Main.Sound;
				panelText[1] = "Music: " + Main.Music;
				panelText[2] = "Contols";
				panelText[3] = "Resolution: " + Game.screenWidth + "x" + Game.screenHeight;
				panelText[4] = "Back";
				break;
		}
	}
	
	private void Back() {
		Main.Instance.Menu = 0;
		CloseRequest();
		Main.MenuUIActive = false;
	}
}
