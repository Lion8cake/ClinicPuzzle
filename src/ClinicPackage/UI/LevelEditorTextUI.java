package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
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
	
	private String[] typesPrompt = new String[LETextTypes.values().length];
	
	private String[] typesValue = new String[LETextTypes.values().length];
	
	private boolean smallText = false;
	
	public LevelEditorTextUI(LevelEditorUI parent, int type)
	{
		parentUI = parent;
		textType = LETextTypes.values()[type];
	}
	
	@Override
	public void SetStaticDefaults()
	{
		KeyboardInputs = true;
		if (textType == LETextTypes.roomWidth || textType == LETextTypes.roomHeight)
			smallText = true;
		if (smallText)
		{
			Width = 11 * 32;
			Height = 6 * 32;
		}
		else
		{
			Width = 13 * 32;
			Height = 8 * 32;
		}
		x = (Main.ScreenWidth - Width) / 2;
		y = (Main.ScreenHeight - Height) / 2;
		typesPrompt[LETextTypes.roomWidth.ordinal()] = "Input the number of tiles you want this room to be in width. /n(!This will remove all room data currently stored!)";
		typesPrompt[LETextTypes.roomHeight.ordinal()] = "Input the number of tiles you want this room to be in height /n(!This will remove all room data currently stored!)";
		typesPrompt[LETextTypes.ImportName.ordinal()] = "Input the name of the room inside of the /nResources/RoomLayoutData folder that you want to load. /n(Do not include file extention) /n(!This will remove all room data currently stored!)";
		typesPrompt[LETextTypes.ExportName.ordinal()] = "Input the name of the room you would like to save under (This will be generated in the /nResources/Roomlayout folder). /n(Do not include file extention) /n(!This will remove all room data currently stored!)";
		
		typesValue[LETextTypes.roomWidth.ordinal()] = "X: ";
		typesValue[LETextTypes.roomHeight.ordinal()] = "Y: ";
		typesValue[LETextTypes.ImportName.ordinal()] = "Import Name: ";
		typesValue[LETextTypes.ExportName.ordinal()] = "Save Name: ";
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		float textScale = 2f;
		String text = typesPrompt[textType.ordinal()];
		Image Text = Main.texture2D.DrawText(text, (int)((Width - 32) / textScale), (int)((Height - 32) / textScale), Color.WHITE);
		int textX = x + 16;
		int textY = y + 16;
		Image Textbg = Main.texture2D.DrawText(text, (int)((Width - 32) / textScale), (int)((Height - 32) / textScale), Color.BLACK);
		Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
		Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
		
		if (Main.IsTyping)
		{
			String text2 = typesValue[textType.ordinal()] + Main.TypedText;
			Image Text2 = Main.texture2D.DrawText(text2, -1, -1, Color.WHITE);
			int textX2 = (int)(x + (Width / 2) - (Text2.getWidth(null) * textScale / 2));
			int textY2 = (int)(textX + (Text.getWidth(null) * textScale)) + (int)(Height - 32 - (textX + (Text.getWidth(null) * textScale))) / 2 + (int)((Text2.getHeight(null) * textScale / 2));
			Image Textbg2 = Main.texture2D.DrawText(text2, -1, -1, Color.BLACK);
			Texture2D.DrawStaticAsset(g, Textbg2, textX2 + (int)(1 * textScale), textY2 + (int)(1 * textScale), null, textScale, textScale);
			Texture2D.DrawStaticAsset(g, Text2, textX2, textY2, null, textScale, textScale);
		}
	}
	
	@Override
	public void Update()
	{
		if (KeyInputDelay <= 0)
		{
			if (!Main.IsTyping)
			{
				Main.IsTyping = true;
				KeyInputDelay = 30;
			}
			if (Main.signaledFinishedText && Main.IsTyping)
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
				else if (textType == LETextTypes.ImportName)
				{
					File file = new File("Resources\\" + "RoomLayoutData\\" + Main.TypedText + ".rld").getAbsoluteFile();
					if (file.exists())
					{
						parentUI.importedSave = file;
					}
					else
						Logging.Log("Warning/!\\: Input file was not found, unable to load room");
				}
				else if (textType == LETextTypes.ExportName)
				{
					File file = new File("Resources\\" + "RoomLayoutData\\" + Main.TypedText + ".rld").getAbsoluteFile();
					parentUI.exportedSave = file.toPath();
				}
				parentUI.RefreshRoom = true;
				Main.IsTyping = false;
				parentUI.textFieldOpen = false;
				CloseRequest();
			}
		}
	}
}
