package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import ClinicPackage.Logging;
import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class LevelEditorTextUI extends UIElement {

	public enum LETextTypes
	{
		roomWidth,
		roomHeight,
		ImportName,
		ExportName
	}
	
	private LevelEditorUI parentUI = null;
	
	private LETextTypes textType = null;
	
	public LevelEditorTextUI(LevelEditorUI parent, int type)
	{
		parentUI = parent;
		textType = LETextTypes.values()[type];
	}
	
	@Override
	public void SetStaticDefaults()
	{
		//KeyboardInputs = true;
		Width = 8 * 32;
		Height = 6 * 32;
		x = (Main.ScreenWidth - Width) / 2;
		y = (Main.ScreenHeight - Height) / 2;
		
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		if (Main.IsTyping)
		{
			float textScale = 2f;
			String text = "value: " + Main.TypedText;
			Image Text = Main.texture2D.DrawText(text, -1, -1, Color.WHITE);
			int textX = (int)(x + (Width / 2) - (Text.getWidth(null) * textScale / 2));
			int textY = (int)(y + (Height / 2) - (Text.getHeight(null) * textScale / 2));
			Image Textbg = Main.texture2D.DrawText(text, -1, -1, Color.BLACK);
			Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
			Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
		}
	}
	
	@Override
	public void Update()
	{
		if (KeyInputDelay <= 0)
		{
			if (!Main.IsTyping && Main.MouseClicked)
			{
				Main.IsTyping = true;
				KeyInputDelay = 30;
			}
			else if (Key_Accept)
			{
				if (textType == LETextTypes.roomWidth)
				{
					if (Main.StringIsDigit(Main.TypedText))
						parentUI.roomWidth = Integer.parseInt(Main.TypedText);
					else
						Logging.Log("Warning/!\\: Input was not entirely a integer, unable to set width!");
				}
				else if (textType == LETextTypes.roomHeight)
				{
					if (Main.StringIsDigit(Main.TypedText))
						parentUI.roomHeight = Integer.parseInt(Main.TypedText);
					else
						Logging.Log("Warning/!\\: Input was not entirely a integer, unable to set height!");
				}
				Main.IsTyping = false;
				parentUI.textFieldOpen = false;
				CloseRequest();
			}
		}
	}
}
