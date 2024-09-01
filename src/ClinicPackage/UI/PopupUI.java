package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;
import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class PopupUI extends UIElement {
	
	private boolean question = false;
	
	private String popUpText = "";
	
	private Supplier<Runnable> Delegate = null;
	
	public PopupUI(String text, Supplier<Runnable> d)
	{
		popUpText = text;
		Delegate = d;
	}
	
	@Override
	public void SetStaticDefaults() {
		Width = 32 * 12;
		Height = 32 * 7;
		uiSize();
		KeyInputDelay = 200;
	}
	
	@Override
	public void Draw(Graphics graphics) {
		BufferedImage img = (BufferedImage)(Texture2D.Get("TestUIPanel"));
		BufferedImage imgIn = (BufferedImage) (Texture2D.Get("TestUIPanelInactive"));
		UIElement.DrawPanel(graphics, imgIn, x, y, Width, Height);
		
		int ButWidth = 4 * 32;
		int ButHeight = 2 * 32;
		int ButY = y + 4 * 32;
		int ButX = x + Width / 2 + 32;
		int ButX2 = x + 32;
		UIElement.DrawPanel(graphics, question ? img : imgIn, ButX, ButY, ButWidth, ButHeight);
		UIElement.DrawPanel(graphics, question ? imgIn : img, ButX2, ButY, ButWidth, ButHeight);
		
		graphics.drawString("Yes", ButX + 32, ButY + 32);
		graphics.drawString("No", ButX2 + 32, ButY + 32);
		graphics.drawString(popUpText, x + 32, y + 36);
		Image test = Main.texture2D.DrawText("Hi this is a test text using texture 2D's new text drawing code, heres hope to all that works!", 20, 20);
		Texture2D.DrawStaticAsset(graphics, test, 20, 20, null, 2f, 2f);
	}
	
	@Override
	public void Update() {
		if (Key_Left)
			question = false;
		else if (Key_Right)
			question = true;
		
		if (Key_Accept && KeyInputDelay <= 0)
		{
			if (question)
			{
				if (Delegate != null)
				{
					Delegate.get();
				}
			}
			KeyInputDelay = 10;
			CloseRequest();
		}
	}	
	
	private void uiSize()
	{
		x = (Main.ScreenWidth - Width) / 2;
		y = (Main.ScreenHeight - Height) / 2;
	}
}
