package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ClinicPackage.Main;
import ClinicPackage.Player;
import Lion8cake.Texture2D;

public class TextBoxUI extends UIElement {
	public TextBoxUI(String textBoxText)
	{
		Text = textBoxText;
	}
	
	public static int Width = 16 * 32;
	
	public static int Height = 4 * 32;
	
	public String Text = "";
	
	@Override
	public void SetStaticDefaults()
	{
		x = (Main.Instance.ScreenWidth - Width) / 2;
		y = Main.Instance.ScreenHeight - x - 64;
	}
	
	@Override
	public void Draw(Graphics graphics)
	{
		BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
		UIElement.DrawPanel(graphics, img, x, y, Width, Height);
		graphics.drawString(Text, x + 32, y + 32);
	}
	
	@Override
	public void Update()
	{
		Main.textBoxOpen = true;
		if (Player.kDown)
		{
			CloseUI(this);
		}
	}
}
