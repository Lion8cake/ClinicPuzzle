package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;

import ClinicPackage.Main;
import Lion8cake.Texture2D;

public class LevelEditorUI extends UIElement {

	public int panelSelected = 0;

	public int maxPanels = 7;
	
	private int[] panelX = new int[maxPanels];
	
	private int[] panelY = new int[maxPanels];
	
	private int[] panelWidth = new int[maxPanels];
	
	private int[] panelHeight = new int[maxPanels];
	
	private String[] panelText = new String[maxPanels];
	
	@Override
	public void SetStaticDefaults() {
		Width = Main.ScreenWidth - 32;
		Height = Main.ScreenHeight - 32;
		x = 8;
		y = 8;
	}
	
	@Override
	public void Draw(Graphics g)
	{
		//Image img = Texture2D.Get("TestUIPanel");
		Image imgIn = Texture2D.Get("TestUIPanelInactive");
		UIElement.DrawPanel(g, imgIn, x, y, Width, Height);
		
		int RPBwidth = 8 * 32;
		int RPBheight = Height - 32;
		int RPBx = x + (Width - RPBwidth) - 16;
		int RPBy = y + 16;
		UIElement.DrawPanel(g, imgIn, RPBx, RPBy, RPBwidth, RPBheight);
	}
	
	@Override
	public void Update()
	{
		if (Key_Back)
		{
			Main.LevelEditorOpen = false;
			Main.Instance.BackToMainMenu();
			CloseRequest();
		}
	}
}
