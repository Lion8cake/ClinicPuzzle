package ClinicPackage.UI;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIElement {
	public String UIName = this.getClass().getName();
	
	public List<UIElement> uiElements = new ArrayList<UIElement>();
	
	private static int _idCounter = 0;
	
	protected int ID;
	
	public int x = 20;
	
	public int y = 20;
	
	/**Weight of the UI, the weight determines what priority it has within the drawing of UIs
	 */
	public float Weight = 1f;
	
	public void UIUpdate()
	{
		for(UIElement element : uiElements)
		{
			element.Update();
		}
	}
	
	public void UIDraw(Graphics g)
	{
		for(UIElement element : uiElements)
		{
			element.Draw(g);
		}
	}
	
	public UIElement()
	{
		ID = _idCounter++;
	}
	
	public void CloseUI(UIElement element)
	{
		uiElements.remove(element);
	}
	
	public void Apphend(UIElement element)
	{
		uiElements.add(element);
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
