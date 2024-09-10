package ClinicPackage.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class DebuggMenuUI extends UIElement {

	private boolean active = false;
	
	public boolean ListOpen = false;
	
	@Override
	public void SetStaticDefaults()
	{
		KeyboardInputs = false;
		Width = 4 * 32;
		Height = 3 * 32;
		
		x = Main.ScreenWidth - Width - 16;
		y = Main.ScreenHeight - Height - 32;
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image img = Texture2D.Get("TestUIPanel");
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, active ? img : imgIn, x, y, Width, Height);
		
		float textScale = 2f;
		String text = "Debug Menu";
		Image Text = Main.texture2D.DrawText(text, -1, -1, Color.WHITE);
		int textX = (int)(x + (Width / 2) - (Text.getWidth(null) * textScale / 2));
		int textY = (int)(y + (Height / 2) - (Text.getHeight(null) * textScale / 2));
		Image Textbg = Main.texture2D.DrawText(text, -1, -1, Color.BLACK);
		Texture2D.DrawStaticAsset(g, Textbg, textX + (int)(1 * textScale), textY + (int)(1 * textScale), null, textScale, textScale);
		Texture2D.DrawStaticAsset(g, Text, textX, textY, null, textScale, textScale);
	}
	
	@Override 
	public void Update()
	{
		if (!Main.debugg)
		{
			CloseRequest();
			Main.DebugIconOpen = false;
			return;
		}
		active = false;
		if (Main.InsideRectangle(new Rectangle(x, y, Width, Height), Main.MouseWorld))
		{
			active = true;
			if (Main.MouseClicked && KeyInputDelay <= 0)
			{
				if (!ListOpen)
				{
					DebuggListUI list = new DebuggListUI(this);
					Main.UI.Apphend(list);
					ListOpen = true;
				}
			}
		}
	}
}
