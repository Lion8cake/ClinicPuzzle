package ClinicPackage.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import ClinicPackage.Main;

public class MouseHandler implements MouseListener {

	public static int MouseHeldTimer = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Main.MouseClicked = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (MouseHeldTimer > 30)
		{
			//Logging.Log("Mouse Drag Released");
		}
		MouseHeldTimer = 0;
		Main.MouseClicked = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
