package ClinicPackage.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ClinicPackage.Main;
import ClinicPackage.Player;

public class InputHandler implements KeyListener {

	//Key Codes
	public static int UpKeyCode = KeyEvent.VK_W;
	
	public static int DownKeyCode = KeyEvent.VK_S;
	
	public static int LeftKeyCode = KeyEvent.VK_A;
	
	public static int RightKeyCode = KeyEvent.VK_D;
	
	public static int SpaceKeyCode = KeyEvent.VK_SPACE;
	
	public static int ESCKeyCode = KeyEvent.VK_ESCAPE;
	
	
	private static boolean KeybindSetting = false;
	
	private static int KeybindSettingKey = 0;
	
	public static void SetKeybind(int keybindSet)
	{
		KeybindSetting = true;
		KeybindSettingKey = keybindSet;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (KeybindSetting)
		{
			switch (KeybindSettingKey)
			{
				case 0:
					UpKeyCode = e.getKeyCode();
					break;
				case 1:
					DownKeyCode = e.getKeyCode();
					break;
				case 2:
					LeftKeyCode = e.getKeyCode();
					break;
				case 3:
					RightKeyCode = e.getKeyCode();
					break;
				case 4:
					SpaceKeyCode = e.getKeyCode();
					break;
				case 5:
					ESCKeyCode = e.getKeyCode();
					break;
			}
			KeybindSetting = false;
			KeybindSettingKey = 0;
		}
		else
		{
			if (e.getKeyCode() == UpKeyCode)
			{
				Player.kUp = true;
			}
			else if (e.getKeyCode() == DownKeyCode)
			{
				Player.kDown = true;
			}
			else if (e.getKeyCode() == LeftKeyCode)
			{
				Player.kLeft = true;
			}
			else if (e.getKeyCode() == RightKeyCode)
			{
				Player.kRight = true;
			}
			if (e.getKeyCode() == SpaceKeyCode)
			{
				Player.kSpace = true;
			}
			if (e.getKeyCode() == ESCKeyCode)
			{
				Player.kESC = true;
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == UpKeyCode)
		{
			Player.kUp = false;
		}
		else if (e.getKeyCode() == DownKeyCode)
		{
			Player.kDown = false;
		}
		else if (e.getKeyCode() == LeftKeyCode)
		{
			Player.kLeft = false;
		}
		else if (e.getKeyCode() == RightKeyCode)
		{
			Player.kRight = false;
		}
		if (e.getKeyCode() == SpaceKeyCode)
		{
			Player.kSpace = false;
		}
		if (e.getKeyCode() == ESCKeyCode)
		{
			Player.kESC = false;
		}
	}
}
