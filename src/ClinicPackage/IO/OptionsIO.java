package ClinicPackage.IO;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import ClinicPackage.Logging;
import ClinicPackage.Main;

public class OptionsIO {
	
	public static boolean IsStringNumeric(String string)
	{
		try {
			Integer.parseInt(string);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static void LoadSettings()
	{
		String optionsFile = FileIO.FolderPath + "/Options.txt";
		Path optionsPath = Paths.get(optionsFile);
		if (Files.exists(optionsPath))
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(optionsFile)));
				String line1 = reader.readLine();
				String line2 = reader.readLine();
				String line3 = reader.readLine();
				if (IsStringNumeric(line1) && IsStringNumeric(line2) && IsStringNumeric(line3))
				{
					Main.Sound = Integer.parseInt(line1);
					Main.Music = Integer.parseInt(line2);
					Main.ResolutionType = Integer.parseInt(line3);
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
