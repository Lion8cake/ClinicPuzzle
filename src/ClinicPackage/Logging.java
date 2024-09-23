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


//TODO: Add miliseconds to the log text (not file name)
//TODO: Add support for NON-logging system outputs (ie. System.out/System.err and error/exceptions)
public class Logging {
	
	public enum LoggingType { //This enum is an easy way for logs to specify what type they are
		Base, //Base, meaning no type just the normal logging string
		File //File, meaning that it will log the string as a part of a file change
	}
	
	public static String LoggingTypeToString(LoggingType type) //This changes the enum into a readable string for the logging output
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
	
	/**Writes the string both to the log file and to the outputted system.out
	 * @param loggedText The string of the log to be outputted
	 * @param loggingType The Type the log is based off
	 */
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
	
	/**Writes the string both to the log file and to the outputted system.out <br/>
	 * Defaults the logging type to Base (which is nothing)
	 * @param loggedText The string of the log to be outputted
	 */
	public static void Log(String loggedText)
	{
		Log(loggedText, LoggingType.Base); //defualts the logging type to base
	}
	
	/**Initiates the logs, checks if all file resources are in place, if not then it generates them <br/>
	 * Moves already existing logs to the archived logs folder
	 */
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
