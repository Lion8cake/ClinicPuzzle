package ClinicPackage.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import ClinicPackage.Logging;
import ClinicPackage.Main;

public class SaveFileIO {
	
	public static void FileSavesSettup()
	{
		for (int i = 0; i < 5; i++)
		{
			String savesFolder = "/Saves";
			String savesFile = FileIO.FolderPath + savesFolder + "/Save" + i + ".sav";
			Path savesPath = Paths.get(savesFile);
			if (Files.exists(savesPath))
			{
				Main.Instance.savefileExists[i] = true;
			}
			else
			{
				Main.Instance.savefileExists[i] = false;
			}
		}
	}
	
	public static void CreateSaves(int save)
	{
		String savesFile = FileIO.FolderPath + "/" + FileIO.SavesFolderName + "/Save" + save + ".sav";
		Path savesPath = Paths.get(savesFile);
		try {
			Files.createFile(savesPath);
			SaveSaves(save);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SaveFileIO.FileSavesSettup();
	}
	
	public static void LoadSaves(int save)
	{
		String savesFile = FileIO.FolderPath + "/" + FileIO.SavesFolderName + "/Save" + save + ".sav";
		Path savesPath = Paths.get(savesFile);
		if (Files.exists(savesPath))
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(savesFile)));
				String[] lines = new String[2];
				for (int i = 0; i < lines.length; i++)
				{
					lines[i] = reader.readLine();
				}
				if (Main.IsStringNumeric(lines))
				{
					Main.player[Main.myPlayer].x = Integer.parseInt(lines[0]);
					Main.player[Main.myPlayer].y = Integer.parseInt(lines[1]);
					Logging.Log("Save" + save + " Loaded!", Logging.LoggingType.File);
				}
				else
				{
					reader.close();
					Files.delete(savesPath);
					LoadSaves(save);
					Logging.Log("Save that is trying to be opened has been found to be corrupted. The file at: " + savesPath + " had invalid characters inside of it causing an error while loading.", Logging.LoggingType.File);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			CreateSaves(save);
			SaveSaves(save);
		}
	}
	
	public static void SaveSaves(int save) //Funny Name
	{
		String savesFile = FileIO.FolderPath + "/" + FileIO.SavesFolderName + "/Save" + save + ".sav";
		Path savesPath = Paths.get(savesFile);
		if (Files.exists(savesPath))
		{
			try {
				FileChannel.open(savesPath, StandardOpenOption.WRITE).truncate(0).close(); //Clear options File
				PrintWriter opt = new PrintWriter(new BufferedWriter(new FileWriter(savesFile, true)));
				opt.println(Main.player[Main.myPlayer].x);
				opt.println(Main.player[Main.myPlayer].y);
				opt.close();
				Logging.Log("Saving Game Progress", Logging.LoggingType.File);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			CreateSaves(save);
			SaveSaves(save);
		}
	}
}
