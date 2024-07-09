package Lion8cake;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ClinicPackage.Main;
import ClinicPackage.IO.FileIO;

public class Logging {
	
	final public class LoggingType {
		final private static String[] LoggingTypeString = { "", "File System: " };
		
		final public static int Base = 0;
		
		final public static int File = 1;
	}
	
	public static void Log(String loggedText, int loggingType) {
	    Path logs = Paths.get(FileIO.FolderPath + "/" + FileIO.LogsFolderName);
	    if (!Files.exists(logs))
	    	FileIO.CreateDirectory(logs, false);

	    try {  
	    	 String text = (String)(LoggingType.LoggingTypeString[loggingType] + loggedText);
	    	 Path logFile = Paths.get(FileIO.FolderPath + "/" + FileIO.LogsFolderName + "/Runtime.log");
	    	 System.out.println(text);
	    	 Files.writeString(logFile, text + "\n", StandardOpenOption.WRITE);
	    } 
	    catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
}
