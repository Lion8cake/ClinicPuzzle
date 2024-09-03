package ClinicPackage.UI;

import java.awt.Color;
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
		KeyInputDelay = 20;
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
		UIElement.DrawPanel(graphics, !question ? imgIn : img, ButX, ButY, ButWidth, ButHeight);
		UIElement.DrawPanel(graphics, question ? imgIn : img, ButX2, ButY, ButWidth, ButHeight);
		
		//graphics.drawString(popUpText, x + 32, y + 36);
		float textScale = 3f;
		Image popupText = Main.texture2D.DrawText(popUpText, (int)((Width - (64 * textScale) / textScale) / textScale), -1);
		Image yesTextbg = Main.texture2D.DrawText("Yes", -1, -1, Color.black);
		Image noTextbg =  Main.texture2D.DrawText("No", -1, -1, Color.black);
		Image yesText = Main.texture2D.DrawText("Yes", -1, -1, !question ? Color.WHITE : Color.GRAY);
		Image noText =  Main.texture2D.DrawText("No", -1, -1, question ? Color.WHITE : Color.GRAY);
		
		Texture2D.DrawStaticAsset(graphics, popupText, x + 32, y + 16, null, textScale, textScale);
		if (question)
			Texture2D.DrawStaticAsset(graphics, yesTextbg, ButX + (int)((ButWidth / 2) - (yesTextbg.getWidth(null) * textScale / 2)) + (int)(1 * textScale), ButY + ((ButY / 2) - (int)(yesTextbg.getHeight(null) * textScale / 2)) + (int)(1 * textScale), null, textScale, textScale);
		else
			Texture2D.DrawStaticAsset(graphics, noTextbg, ButX2 + (int)((ButWidth / 2) - (noTextbg.getWidth(null) * textScale / 2)) + (int)(1 * textScale), ButY + (int)((ButY / 2) - (noText.getHeight(null) * textScale / 2)) + (int)(1 * textScale), null, textScale, textScale);
		Texture2D.DrawStaticAsset(graphics, yesText, ButX + (int)((ButWidth / 2) - (yesText.getWidth(null) * textScale / 2)), ButY + (int)((ButY / 2) - (yesText.getHeight(null) * textScale / 2)), null, textScale, textScale);
		Texture2D.DrawStaticAsset(graphics, noText, ButX2 + (int)((ButWidth / 2) - (noText.getWidth(null) / 2)), ButY + 16, null, textScale, textScale);
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
