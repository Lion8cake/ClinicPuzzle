package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
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
		Rectangle frame = new Rectangle(0, 0, 16, 16);
		
		//Do UI drawing stuff to create a dynamic box depending on the game window size
		for (int i  = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				frame.x = 16 * i;
				frame.y = 16 * j;
				frame.width = 16;
				frame.height = 16;
				Main.texture2D.DrawAsset(graphics, img, x + (i * 32), y + (j * 32), frame, 1f);
			}
		}
	}
	
	@Override
	public void Update()
	{
		
	}
}
