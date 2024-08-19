package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ClinicPackage.Main;
import ClinicPackage.Player;
import Lion8cake.Texture2D;

public class TextBoxUI extends UIElement {
	public TextBoxUI(Player playerInteracting, String[] textBoxText, int[] personSettings, int[] emotionSettings, String[] peopleInConvo)
	{
		for (int i = 0; i < 4; i++)
			PeopleInConvo[i] = peopleInConvo[i];
		for (int j = 0; j < textBoxText.length; j++)
			Text[j] = textBoxText[j];
		for (int k = 0; k < textBoxText.length; k++)
			PersonTalking[k] = personSettings[k];
		for (int l = 0; l < textBoxText.length; l++)
			currentEmotion[l] = emotionSettings[l];
		maxDialog = textBoxText.length;
		myPlayer = playerInteracting;
	}
	
	Player myPlayer;
	
	public String[] Text = new String[Byte.MAX_VALUE];
	
	public String[] PeopleInConvo = {"", "", "", ""};
	
	public int[] PersonTalking = new int[Byte.MAX_VALUE];
	
	public int[] currentEmotion = new int[Byte.MAX_VALUE];
	
	public int currentDialogNum = 0;
	
	public int maxDialog;
	
	private int KeyInputProtection = 30;
	
	@Override
	public void SetStaticDefaults()
	{
		Width = 20 * 32;
		Height = 8 * 32;
		uiSize();
	}
	
	@Override
	public void Draw(Graphics graphics)
	{
		BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
		BufferedImage img2 = (BufferedImage)(Texture2D.Get("TestUIPanelInactive"));
		int iconSquareSize = 160;
		int iconOutwardsDist = 48;
		int RLOutwardsDist = iconSquareSize - iconOutwardsDist;
		int largeIconSS = 192;
		int largeIconOD = 56;
		int largeRLOD = largeIconSS - largeIconOD;
		for (int i = 0; i < 4; i++)
		{
			if (PeopleInConvo[i] != "")
			{
				UIElement.DrawPanel(graphics, img2, x - iconOutwardsDist, y - iconOutwardsDist, iconSquareSize, iconSquareSize); //Top Left
				UIElement.DrawPanel(graphics, img2, x + Width - RLOutwardsDist, y - iconOutwardsDist, iconSquareSize, iconSquareSize); //Top Right
				UIElement.DrawPanel(graphics, img2, x - iconOutwardsDist, y + Height - RLOutwardsDist, iconSquareSize, iconSquareSize); //Bottom Left
				UIElement.DrawPanel(graphics, img2, x + Width - RLOutwardsDist, y + Height - RLOutwardsDist, iconSquareSize, iconSquareSize); //Bottom Right
				break;
			}
		}
		if (PeopleInConvo[0] != "")
			UIElement.DrawPanel(graphics, img, x - iconOutwardsDist, y - iconOutwardsDist, iconSquareSize, iconSquareSize); //Top Left
		if (PeopleInConvo[1] != "")
			UIElement.DrawPanel(graphics, img, x + Width - RLOutwardsDist, y - iconOutwardsDist, iconSquareSize, iconSquareSize); //Top Right
		if (PeopleInConvo[2] != "")
			UIElement.DrawPanel(graphics, img, x - iconOutwardsDist, y + Height - RLOutwardsDist, iconSquareSize, iconSquareSize); //Bottom Left
		if (PeopleInConvo[3] != "")
			UIElement.DrawPanel(graphics, img, x + Width - RLOutwardsDist, y + Height - RLOutwardsDist, iconSquareSize, iconSquareSize); //Bottom Right
		UIElement.DrawPanel(graphics, img, x, y, Width, Height);
		
		String EmotionExtention = "";
		switch (currentEmotion[currentDialogNum])
		{
			case 1:
				EmotionExtention = "Think";
				break;
			case 2:
				EmotionExtention = "Shrug";
				break;
		}
		switch (PersonTalking[currentDialogNum])
		{
			case 0:
				UIElement.DrawPanel(graphics, img, x - largeIconOD, y - largeIconOD, largeIconSS, largeIconSS); //Top Left
				Main.texture2D.DrawAsset(graphics, Texture2D.Get(PeopleInConvo[PersonTalking[currentDialogNum]] + EmotionExtention), x - largeIconOD, y - largeIconOD, 1.25f);
				break;
			case 1:
				UIElement.DrawPanel(graphics, img, x + Width - largeRLOD, y - largeIconOD, largeIconSS, largeIconSS); //Top Right
				Main.texture2D.DrawAsset(graphics, Texture2D.Get(PeopleInConvo[PersonTalking[currentDialogNum]] + EmotionExtention), x + Width - largeRLOD, y - largeIconOD, 1.25f);
				break;
			case 2:
				UIElement.DrawPanel(graphics, img, x - largeIconOD, y + Height - largeRLOD, largeIconSS, largeIconSS); //Bottom Left
				Main.texture2D.DrawAsset(graphics, Texture2D.Get(PeopleInConvo[PersonTalking[currentDialogNum]] + EmotionExtention), x - largeIconOD, y + Height - largeRLOD, 1.25f);
				break;
			case 3:
				UIElement.DrawPanel(graphics, img, x + Width - largeRLOD, y + Height - largeRLOD, largeIconSS, largeIconSS); //Bottom Right
				Main.texture2D.DrawAsset(graphics, Texture2D.Get(PeopleInConvo[PersonTalking[currentDialogNum]] + EmotionExtention), x + Width - largeRLOD, y + Height - largeRLOD, 1.25f);
				break;
		}

		graphics.drawString(Text[currentDialogNum], x + 136, y + 32);
		
	}
	
	@Override
	public void Update()
	{
		Main.textBoxOpen = true;
		if (Player.kSpace && KeyInputProtection <= 0)
		{
			KeyInputProtection = 30;
			if (currentDialogNum < maxDialog - 1)
				currentDialogNum++;
			else
			{
				CloseRequest();
				myPlayer.InteractionCooldown = 30;
			}
		}
		if (KeyInputProtection > 0)
			KeyInputProtection--;
		if (Main.ScreenSizeChange)
		{
			uiSize();
		}
	}
	
	private void uiSize()
	{
		x = (Main.ScreenWidth - Width) / 2;
		y = Main.ScreenHeight - x - Height + 98;
	}
}
