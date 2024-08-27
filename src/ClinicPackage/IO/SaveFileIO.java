package ClinicPackage.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		SaveFileIO.FileSavesSettup();
	}
}
