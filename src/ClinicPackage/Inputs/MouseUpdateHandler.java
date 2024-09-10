package ClinicPackage.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import ClinicPackage.Main;

public class MouseUpdateHandler implements MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent e) {
		MouseHandler.MouseHeldTimer++;
		if (MouseHandler.MouseHeldTimer > 30)
		{
			//Logging.Log("Mouse Dragged");
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Main.MouseWorldX = e.getX();
		Main.MouseWorldY = e.getY();
	}
}
