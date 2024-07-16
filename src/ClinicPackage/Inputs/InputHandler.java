package ClinicPackage.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ClinicPackage.Main;
import ClinicPackage.Player;

public class InputHandler implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			Player.kUp = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			Player.kDown = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			Player.kLeft = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			Player.kRight = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS)
		{
			Main.Zoom(0.1f);
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS)
		{
			Main.Zoom(-0.1f);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			Player.kUp = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			Player.kDown = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_A)
		{
			Player.kLeft = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			Player.kRight = false;
		}
	}
}
