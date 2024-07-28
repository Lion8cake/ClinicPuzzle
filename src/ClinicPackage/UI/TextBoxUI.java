package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class TextBoxUI extends UIElement {
	public TextBoxUI(String textBoxText)
	{
		
	}
	
	public static int Width = 16 * 32;
	
	public static int Height = 8 * 32;
	
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
		
		//Do UI drawing stuff to create a dynamic box depending on the game window size
		Texture2D.DrawAsset(graphics, img, x, y, 5, 1.5f);
	}
	
	@Override
	public void Update()
	{
		
	}
}
