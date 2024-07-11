package ClinicPackage.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ClinicPackage.Main;

public class InputHandler implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			Main.kUp = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			Main.kDown = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			Main.kLeft = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			Main.kRight = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			Main.kUp = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			Main.kDown = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			Main.kLeft = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			Main.kRight = false;
		}
	}
}
