package ClinicPackage.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import ClinicPackage.Player;
import Lion8cake.Texture2D;

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
	
	public static int KeyInputDelay = 10;
	
	public boolean closeRequest = false;
	
	private static UIElement elementCloseRequest;
	
	/**Weight of the UI, the weight determines what priority it has within the drawing of UIs <br/>
		Unused
	 */
	public float Weight = 1f;
	
	protected boolean Key_Up = false;
	protected boolean Key_Down = false;
	protected boolean Key_Left = false;
	protected boolean Key_Right = false;
	protected boolean Key_Accept = false;
	protected boolean Key_Back = false;
	
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
				Texture2D.DrawStaticAsset(graphics, image, posX, posY, frame, scaleX * 2, scaleY * 2);
			}
		}
	}
	
	public void UIUpdate()
	{
		for(int i = 0; i < uiElements.size(); i++)
		{
			UIElement element = uiElements.get(i);
			
			boolean flag = element == uiElements.get(uiElements.size() - 1);
			element.Key_Up = flag ? Player.kUp : false;
			element.Key_Down = flag ? Player.kDown : false;
			element.Key_Left = flag ? Player.kLeft : false;
			element.Key_Right = flag ? Player.kRight : false;
			element.Key_Accept = flag ? Player.kSpace : false;
			element.Key_Back = flag ? Player.kESC : false;
			
			element.Update();
			
			if (KeyInputDelay > 0)
				KeyInputDelay--;
			if (KeyInputDelay <= 0)
				KeyInputDelay = 0;
			
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
		for(int i = 0; i < uiElements.size(); i++)
		{
			UIElement element = uiElements.get(i);
			element.Draw(g);
		}
	}
	
	public void CloseRequest()
	{
		this.closeRequest = true;
		KeyInputDelay = 10;
	}
	
	public UIElement()
	{
		ID = _idCounter++;
	}
	
	public void CloseUI(UIElement element)
	{
		uiElements.remove(uiElements.get(element.uiElementID));
		for (int i = element.uiElementID; i < uiElements.size(); i++)
		{
			UIElement eLement = uiElements.get(i);
			eLement.uiElementID--;
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
