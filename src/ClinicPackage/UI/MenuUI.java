package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.image.*;
import ClinicPackage.Game;
import ClinicPackage.Logging;
import ClinicPackage.Main;
import ClinicPackage.Player;
import ClinicPackage.IO.OptionsIO;
import ClinicPackage.Inputs.InputHandler;
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

	private BufferedImage blurredImage = null;
	
	private boolean changeres = false;
	
	private int saveScrollType = 0;
	
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
		KeyInputDelay = 20;
	}
	
	@Override
	public void Draw(Graphics graphics) {
		if (Main.Instance.InGame)
		{
			if (blurredImage == null || changeres)
			{
				//Blurr Drawcode
				BufferedImage background = new BufferedImage(Main.ScreenWidth, Main.ScreenHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics g2 = background.getGraphics();
				g2.setColor(new Color(0, 0, 0));
				g2.fillRect(0, 0, Main.ScreenWidth, Main.ScreenHeight);
				g2.dispose();
				
				BufferedImage bimg = new BufferedImage(Main.ScreenWidth, Main.ScreenHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics g = bimg.getGraphics();
				g.drawImage(background, 0, 0, null);
				Main.Instance.DrawGame(g);
				g.dispose();
				
				BufferedImage bimg2 = Main.Instance.BlurrImage(bimg);
				blurredImage = bimg2;
			}
			graphics.drawImage(blurredImage, 0, 0, null);
		}
		switch (MainMenuType)
		{
			case 0:
			{
				DefaultUIDrawing(graphics);
				break;
			}
			case 1:
			{
				DefaultUIDrawing(graphics);
				break;
			}
			case 2:
			{
				DefaultUIDrawing(graphics);
				break;
			}
			case 3:
			{
				BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
				BufferedImage imgIn = (BufferedImage)(Texture2D.Get("TestUIPanelInactive"));
				int iBWidth = 0;
				int iBHeight = 0;
				while (((iBWidth + 1) * 32) + 16 < (Main.ScreenWidth - 30))
				{
					iBWidth++;
				}
				while (((iBHeight + 1) * 32) + 16 < (Main.ScreenHeight - 30))
				{
					iBHeight++;
				}
				int k = (Main.ScreenWidth - (iBWidth * 32)) / 3;
				int l = (Main.ScreenHeight - (iBHeight * 32)) / 3;
				int backWidth = (iBWidth * 32);
				int backHeight = (iBHeight * 32);
				UIElement.DrawPanel(graphics, imgIn, k, l, backWidth, backHeight);
				for (int d = 1; d < maxPanels + 1; d++)
				{
					int butWidth = ((backWidth - 64) / 2);
					int butHeight2 = 0;
					while ((butHeight2 + 1) * 32 < ((backHeight - Height) / (maxPanels / 2)))
					{
						butHeight2++;
					}
					int butHeight = butHeight2 * 32;
					int butX = k + 32 + (d % 2 == 1 ? 0 : butWidth);
					int butY = l + 16 + (butHeight * ((d - 1) / 2));
					if (d < 7)
					{
						BufferedImage buttonImg = d != panelSelected + 1 ? imgIn : img;
						UIElement.DrawPanel(graphics, buttonImg, butX, butY, butWidth, butHeight);
						
						//Text
						String text = panelText[d - 1];
						int textX = butX + (Width / 2) - (15 / 2);
						int textY = butY + (Height / 2) - 8;
						graphics.drawString(text, textX, textY);
					}
					else
					{
						int i = (Main.ScreenWidth / 2) - (Width / 2);
						int j = butY;
						BufferedImage buttonImg = d != panelSelected + 1 ? imgIn : img;
						UIElement.DrawPanel(graphics, buttonImg, i, j, Width, Height);
						
						//Text
						String text = panelText[d - 1];
						int textX = i + (Width / 2) - (15 / 2);
						int textY = j + (Height / 2) - 8;
						graphics.drawString(text, textX, textY);
					}
				}
				break;
			}
			case 4:
			{
				int savMaxWidth = 0;
				int savMaxHeight = 0;
				while (((savMaxWidth + 1) * 32) + 64 < (Main.ScreenWidth - 64))
				{
					savMaxWidth++;
				}
				while (((savMaxHeight + 1) * 32) + 128 < (Main.ScreenHeight - 32))
				{
					savMaxHeight++;
				}
				for (int d = 1; d < maxPanels + 1; d++)
				{
					BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
					BufferedImage imgIn = (BufferedImage)(Texture2D.Get("TestUIPanelInactive"));
					int[] saveArray = new int[] {0, 1, 2};
					for (int o = 0; o < saveArray.length; o++)
					{
						saveArray[o] = saveArray[o] + saveScrollType;
					}
					for (int playPan = 0; playPan < 3; playPan++)
					{
						int savWidth = (savMaxWidth / 3 * 32);
						int k = (Main.ScreenWidth - (savMaxWidth * 32)) / 2 + ((savMaxWidth / 3 * 32) * playPan);
						int l = (Main.ScreenHeight - (savMaxHeight * 32)) / 2 - 64;
						BufferedImage savPnlImg = saveArray[playPan] == panelSelected ? img : imgIn;
						UIElement.DrawPanel(graphics, savPnlImg, k, l, savWidth, savMaxHeight * 32);
						if (d >= 6 && playPan == 0)
						{
							BufferedImage pnlImg = d != panelSelected + 1 ? imgIn : img;
							int i = (Main.ScreenWidth / 2) - (Width / 2);
							int j = l + savMaxHeight * 32;
							UIElement.DrawPanel(graphics, pnlImg, i, j, Width, Height);
							
							String text = panelText[d - 1];
							int textX = i + (Width / 2) - (15 / 2);
							int textY = j + (Height / 2) - 8;
							graphics.drawString(text, textX, textY);
						}
					}
				}
				break;
			}
		}
	}
	
	private void DefaultUIDrawing(Graphics graphics)
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
			case 2:
				maxPanels = 3;
				break;
			case 3:
				maxPanels = 7; //Amount of Key input settings + back
				break;
			case 4:
				maxPanels = 6; //5 saves
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
		
		if (KeyInputDelay <= 19 && KeyInputDelay > 15 && Main.Instance.InGame)
		{
			blurredImage = null;
		}
		
		if (MainMenuType == 4)
		{
			switch (panelSelected)
			{
				case 0:
				case 1:
					saveScrollType = 0;
					break;
				case 2:
					saveScrollType = 1;
					break;
				case 3:
				case 4:
					saveScrollType = 2;
					break;
			}
		}
		
		//Controls
		if (moveWait <= 0)
		{
			if (MainMenuType != 4)
			{
				if (Player.kDown)
					panelSelected++;
				else if (Player.kUp)
					panelSelected--;
			}
			else
			{
				if (Player.kDown)
					panelSelected = 5;
				else if (Player.kUp)
					panelSelected = 0;
				else if (Player.kLeft)
				{
					if (panelSelected > 0)
						panelSelected--;
					else
						panelSelected = 4;
				}
				else if (Player.kRight)
				{
					if (panelSelected < 4)
						panelSelected++;
					else
						panelSelected = 0;
				}
			}
			moveWait = 10;
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
						Main.Instance.Menu = 4;
						break;
					case 1:
						Main.Instance.Menu = 1;
						break;
					case 2:
						Game.CloseGame();
						break;
				}
				Main.MenuUIActive = false;
				CloseRequest();
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
						CloseRequest();
						Main.Instance.ChangeMainMenu(3); //Controls
						break;
					case 3:
						Main.ResolutionType++;
						KeyInputDelay = 20;
						break;
					case 4:
						Back();
						break;
				}
				break;
			case 2:
				switch (panelSelected)
				{
					case 0:
						Back();
						break;
					case 1:
						Main.Instance.Menu = 1;
						CloseRequest();
						Main.MenuUIActive = false;
						break;
					case 2:
						Main.Instance.BackToMainMenu();
						CloseRequest();
						break;
				}
				break;
			case 3:
				switch (panelSelected)
				{
					case 0://W
					case 1://S
					case 2://A
					case 3://D
					case 4://Space
					case 5://Esc
						InputHandler.SetKeybind(panelSelected);
						break;
					case 6:
						Back();
						break;
				}
				break;
			case 4:
				switch (panelSelected)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						Main.Instance.LoadGame();
						Main.MenuUIActive = false;
						CloseRequest();
						break;
					case 5:
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
			case 2:
				panelText[0] = "Return";
				panelText[1] = "Options";
				panelText[2] = "Main Menu";
				break;
			case 3:
				panelText[0] = "Up: " + KeyEvent.getKeyText(InputHandler.UpKeyCode);
				panelText[1] = "Down: " + KeyEvent.getKeyText(InputHandler.DownKeyCode);
				panelText[2] = "Left: " + KeyEvent.getKeyText(InputHandler.LeftKeyCode);
				panelText[3] = "Right: " + KeyEvent.getKeyText(InputHandler.RightKeyCode);
				panelText[4] = "Interact/Accept: " + KeyEvent.getKeyText(InputHandler.SpaceKeyCode);
				panelText[5] = "Return/Back: " + KeyEvent.getKeyText(InputHandler.ESCKeyCode);
				panelText[6] = "Back";
				break;
			case 4:
				panelText[0] = "Save1";
				panelText[1] = "Save2";
				panelText[2] = "Save3";
				panelText[3] = "Save4";
				panelText[4] = "Save5";
				panelText[5] = "Back";
				break;
		}
	}
	
	private void Back() {
		switch (MainMenuType)
		{
			case 1:
				if (Main.Instance.InMainMenu)
				{
					CloseRequest();
					OptionsIO.SaveSettings();
					Main.Instance.ChangeMainMenu(0);
				}
				else
				{
					CloseRequest();
					OptionsIO.SaveSettings();
					Main.Instance.ChangeMainMenu(2);
				}
				break;
			case 2:
				CloseRequest();
				Main.Instance.ChangeMainMenu(-1);
				break;
			case 3:
				CloseRequest();
				Main.Instance.ChangeMainMenu(1);
				break;
			case 4:
				CloseRequest();
				Main.Instance.ChangeMainMenu(0);
				break;
		}
	}
}
