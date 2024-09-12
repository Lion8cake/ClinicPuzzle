package ClinicPackage.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import ClinicPackage.Main;

public class MouseUpdateHandler implements MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent e) {
		Main.MouseWorld.x = e.getX();
		Main.MouseWorld.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Main.MouseWorld.x = e.getX();
		Main.MouseWorld.y = e.getY();
	}
}
