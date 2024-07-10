package ClinicPackage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import ClinicPackage.IO.FileIO;
import Lion8cake.TimeFormatting;

public class Logging {
	
	public enum LoggingType {
		Base,
		File
	}
	
	public static String LoggingTypeToString(LoggingType type)
	{
		String returnString = "";
		switch (type) {
			case Base:
				returnString = "";
				break;
			case File:
				returnString = "File System: ";
				break;
		}
		return returnString;
	}
	
	public static void Log(String loggedText, LoggingType loggingType) {
	    Path logs = Paths.get(FileIO.FolderPath + "/" + FileIO.LogsFolderName);
	    if (!Files.exists(logs))
	    	FileIO.CreateDirectory(logs, false);

	    try {  
	    	 String text = (String)(TimeFormatting.TimeLogFormat() + LoggingTypeToString(loggingType) + loggedText);
	    	 String logFile = FileIO.FolderPath + "/" + FileIO.LogsFolderName + "/Runtime.log";
	    	 
	    	 PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
			 log.println(text);
			 System.out.println(text);
			 log.close();
		} 
	    catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	public static void InitiateLogs()
	{
		Path logs = Paths.get(FileIO.FolderPath + "/" + FileIO.LogsFolderName);
	    if (!Files.exists(logs))
	    	FileIO.CreateDirectory(logs, false);

	    try {  
			String logFile = FileIO.FolderPath + "/" + FileIO.LogsFolderName + "/Runtime.log";
			String logFileArchived = FileIO.FolderPath + "/" + FileIO.LogsFolderName + "/" + FileIO.BackupLogsFolderName + "/Runtime_" + TimeFormatting.TimeFormat() + ".log";
			if (Files.exists(Paths.get(logFile)))
			{
				Files.move(Paths.get(logFile), Paths.get(logFileArchived), StandardCopyOption.REPLACE_EXISTING);
			}
			Files.createFile(Paths.get(logFile));
		} 
	    catch (IOException e) {  
	        e.printStackTrace();  
	    } 
	    Log("Game Started", Logging.LoggingType.Base);
	}
}
