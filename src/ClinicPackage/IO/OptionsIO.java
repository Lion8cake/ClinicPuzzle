package ClinicPackage.IO;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import ClinicPackage.Logging;
import ClinicPackage.Main;
import ClinicPackage.Inputs.InputHandler;

public class OptionsIO {
	public static void LoadSettings()
	{
		String optionsFile = FileIO.FolderPath + "/Options.txt";
		Path optionsPath = Paths.get(optionsFile);
		if (Files.exists(optionsPath))
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(optionsFile)));
				String[] lines = new String[9];
				for (int i = 0; i < lines.length; i++)
				{
					lines[i] = reader.readLine();
				}
				if (Main.IsStringNumeric(lines))
				{
					Main.Sound = Integer.parseInt(lines[0]);
					Main.Music = Integer.parseInt(lines[1]);
					Main.ResolutionType = Integer.parseInt(lines[2]);
					InputHandler.UpKeyCode = Integer.parseInt(lines[3]);
					InputHandler.DownKeyCode = Integer.parseInt(lines[4]);
					InputHandler.LeftKeyCode = Integer.parseInt(lines[5]);
					InputHandler.RightKeyCode = Integer.parseInt(lines[6]);
					InputHandler.SpaceKeyCode = Integer.parseInt(lines[7]);
					InputHandler.ESCKeyCode = Integer.parseInt(lines[8]);
					Logging.Log("Settings loaded, Welcome Back!", Logging.LoggingType.File);
				}
				else
				{
					reader.close();
					Files.delete(optionsPath);
					LoadSettings();
					Logging.Log("WARNING!! Invalid text has been indentified within the " + optionsPath + " file! The Saved Options will be reset, please do not mess with this file!", Logging.LoggingType.File);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			FileIO.CreateFile(optionsPath, true);
			SaveSettings();
		}
	}
	
	public static void SaveSettings() {
		String optionsFile = FileIO.FolderPath + "/Options.txt";
		Path optionsPath = Paths.get(optionsFile);
		if (Files.exists(optionsPath))
		{
			try {
				FileChannel.open(optionsPath, StandardOpenOption.WRITE).truncate(0).close(); //Clear options File
				PrintWriter opt = new PrintWriter(new BufferedWriter(new FileWriter(optionsFile, true)));
				opt.println(Main.Sound);
				opt.println(Main.Music);
				opt.println(Main.ResolutionType);
				opt.println(InputHandler.UpKeyCode);
				opt.println(InputHandler.DownKeyCode);
				opt.println(InputHandler.LeftKeyCode);
				opt.println(InputHandler.RightKeyCode);
				opt.println(InputHandler.SpaceKeyCode);
				opt.println(InputHandler.ESCKeyCode);
				opt.close();
				Logging.Log("Settings Saved", Logging.LoggingType.File);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			FileIO.CreateFile(optionsPath, true);
			SaveSettings();
		}
	}
	
}
