package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;

import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class DebuggMenuUI extends UIElement {

	@Override
	public void SetStaticDefaults()
	{
		Width = 3 * 32;
		Height = 1 * 32;
		
		x = Main.ScreenWidth - Width;
		y = 0;
	}
	
	@Override
	public void Draw(Graphics g)
	{
		Image img = Texture2D.Get("TestUIPanel");
		UIElement.DrawPanel(g, img, x, y, Width, Height);
	}
}
