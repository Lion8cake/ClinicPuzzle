package ClinicPackage.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ClinicPackage.Logging;
import ClinicPackage.Main;

public class FileIO {
	
	final public static String FolderPath = (String)("C:/Users/" + System.getProperty("user.name") + "/AppData/Roaming/" + Main.GameName);
	
	final public static String LogsFolderName = (String)(Main.GameName + "-logs");
	
	final public static String BackupLogsFolderName = "Archived";
	
	public static void CheckFolderspace()
	{
		Path path = Paths.get(FolderPath);
		Path logs = Paths.get(FolderPath + "/" + LogsFolderName);
		Path backupLogs = Paths.get(FolderPath + "/" + LogsFolderName + "/" + BackupLogsFolderName);
		//Main Path
		if (Files.exists(path))
			Logging.Log("Found File Path, Game will save to: " + path, Logging.LoggingType.File);
		else
			CreateDirectory(path, true);
		//Logs Path
		if (!Files.exists(logs))
			CreateDirectory(logs, false);
		//Logs Archive Path
		if (!Files.exists(backupLogs))
			CreateDirectory(backupLogs, false);
	}
	
	public static void CreateDirectory(Path path, boolean isCoreFolders)
	{
		try
		{
			Files.createDirectory(path);
			String CoreFolderLog = (String)("File Path not found, either your game has been saved to another location or its your first time (in this case, WELCOME!!!)."
					+ "\n Crucial folder generated at: " + path);
			String Log = (String)("File Path not found, a non-crucial folder was found missing and has been regenerated for you."
					+ "\n Folder generated at: " + path);
			Logging.Log(isCoreFolders ? CoreFolderLog : Log, Logging.LoggingType.File);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void CreateFile(Path path, boolean isCoreFile)
	{
		try
		{
			Files.createFile(path);
			String CoreFileLog = (String)("File not found, either your game's files has been saved to another location or its your first time (in this case, WELCOME!!!)."
					+ "\n Crucial file generated at: " + path);
			String Log = (String)("File not found, a non-crucial game file was found missing and has been regenerated for you."
					+ "\n File generated at: " + path);
			Logging.Log(isCoreFile ? CoreFileLog : Log, Logging.LoggingType.File);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
