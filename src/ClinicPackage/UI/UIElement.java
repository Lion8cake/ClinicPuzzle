package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import ClinicPackage.Main;

public class UIElement {
	public String UIName = this.getClass().getName();
	
	private static List<UIElement> uiElements = new ArrayList<UIElement>();
	
	public int uiElementID = -1;
	
	private static int _idCounter = 0;
	
	protected int ID;
	
	public int x = 20;
	
	public int y = 20;
	
	public int Width = 20;
	
	public int Height = 20;
	
	public boolean closeRequest = false;
	
	private static UIElement elementCloseRequest;
	
	/**Weight of the UI, the weight determines what priority it has within the drawing of UIs
	 */
	public float Weight = 1f;
	
	public static void DrawPanel(Graphics graphics, Image image, int x, int y, int width, int height) 
	{
		Rectangle frame = new Rectangle(0, 0, 16, 16);
		for (int i  = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				frame.x = 16 * i;
				frame.y = 16 * j;
				frame.width = 16;
				frame.height = 16;
				int posX = i == 0 ? x : i == 1 ? x + 32 : x + width - 32;
				int posY = j == 0 ? y : j == 1 ? y + 32 : y + height - 32;
				float scaleX = i == 1 ? (width / 32) - 2 : 1f;
				float scaleY = j == 1 ? (height / 32) - 2 : 1f;
				Main.texture2D.drawScale = 2f;
				Main.texture2D.DrawAsset(graphics, image, posX, posY, frame, scaleX, scaleY);
			}
		}
	}
	
	public void UIUpdate()
	{
		for(UIElement element : uiElements)
		{
			element.Update();
			if (element.closeRequest)
				elementCloseRequest = element;
		}
		if (elementCloseRequest != null)
		{
			elementCloseRequest.CloseUI(elementCloseRequest);
			elementCloseRequest = null;
		}
	}
	
	public void UIDraw(Graphics g)
	{
		for(UIElement element : uiElements)
		{
			element.Draw(g);
		}
	}
	
	public void CloseRequest()
	{
		this.closeRequest = true;
	}
	
	public UIElement()
	{
		ID = _idCounter++;
	}
	
	public void CloseUI(UIElement element)
	{
		int size = uiElements.size();
		uiElements.remove(element.uiElementID);
		if (size != uiElements.size())
		{
			for(UIElement elements : uiElements)
			{
				elements.uiElementID = elements.uiElementID - 1;
			}
		}
	}
	
	public void Apphend(UIElement element)
	{
		uiElements.add(element);
		element.uiElementID = (uiElements.size() - 1);
		element.SetStaticDefaults();
	}
	
	//Override
	public void Update() {
		
	}
	
	public void Draw(Graphics graphics) {
		
	}
	
	public void SetStaticDefaults() {
		
	}
}
