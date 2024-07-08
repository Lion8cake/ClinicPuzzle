package Lion8cake;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

//Sound system made by Lion8cake
public class Sound {
	private static String[] ValidExtentions = { ".wav", ".snd" };
	
	/**Plays the sound based on the string path inputed (file name), the path will extend off the Resources folder for this project.
	 * <p>Make sure all sound files are in the Resources source folder within your project, ('projecteName')\Resources
	 * <p>Supported image formats: .wav, .snd*/
	public static void Play(String texturePath) //Overload of Play(texturePath, loopCount)
	{ 
		Play(texturePath, 0);
	}
	
	/**Plays the sound based on the string path inputed (file name), the path will extend off the Resources folder for this project.
	 * <p>Make sure all sound files are in the Resources source folder within your project, ('projecteName')\Resources
	 * <p>Supported image formats: .wav, .snd*/
	public static void Play(String texturePath, Boolean loop)//Overload of Play(texturePath, loopCount)
	{
		Play(texturePath, loop ? Clip.LOOP_CONTINUOUSLY : 0);
	}
	
	/**Plays the sound based on the string path inputed (file name), the path will extend off the Resources folder for this project.
	 * <p>Make sure all sound files are in the Resources source folder within your project, ('projecteName')\Resources
	 * <p>Supported image formats: .wav, .snd*/
	public static void Play(String texturePath, int loopCount)
	{ 
		if (loopCount > 0)
		{
			loopCount -= 1; //We make all inputs that aren't continuous or 0 take away a loop to not make the inputed 3 loop 4 times
		}
		String TexturePath = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //Get the ClassLoader
    	try {
    		TexturePath = getTexturePath(texturePath, classLoader); //Call the getTexturePath method, returns String, we put the result of the method into the TexturePath String 
           	Clip clip;//Create a clip
    		try {
    			clip = AudioSystem.getClip(); //Get Clip
    			clip.open(GetFileBasedonType(TexturePath, classLoader)); //Open the file, we call the GetFileBasedonType method
    		    clip.start(); //Start the file
    		    clip.loop(loopCount);
    		} catch (LineUnavailableException e) {
    			e.printStackTrace();
    		}
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**Returns String with file extension if the file is found within the Resources source Folder, requires a ClassLoader to get the appropriate project's Resource**/
    private static String getTexturePath(String path, ClassLoader classLoader) throws IOException {
    	String path2 = null;
    	InputStream input = null;
        for (int fileType = 0; fileType < ValidExtentions.length; fileType++) //Loop through all available file extensions, multiple file extensions allows for more file flexibility
		{
			path2 = path + ValidExtentions[fileType]; //Combine the inputed file name 
			input = classLoader.getResourceAsStream(path2); //Get the stream, this will return null if the file doesn't exist
			if (input != null)
			{
				break; //Stop the loop when we have the appropriate file
			}
		}
        File file = new File("Resources\\" + path2).getAbsoluteFile(); //Check the Resources folder
        if (!file.exists())
		{
			throw new IllegalArgumentException("The Sound Asset/Media Asset named '" + path + "' could not be found. Maybe a spelling mistake or unsupported sound type?");
		}
		return path2; //Return the String path of the file
    }
    
    /**Returns AudioInputStream of the file inputed, the string for texturePath MUST contain a file extension and that file MUST exist. Use PlaySound.getTexturePath to get/find the file with just a string name**/
    private static AudioInputStream GetFileBasedonType(String texturePath, ClassLoader classLoader) throws IOException {
    	int index = texturePath.lastIndexOf('.'); //Get the file extension of the file, files cannot have . within their name
    	String fileExtension = null;
        if(index > 0) {
          fileExtension = "." + texturePath.substring(index + 1); //Splice the string and get the file extension as its own separate string
        }
    	AudioInputStream inputStream = null;
    	try {
    		if (fileExtension != null) //Make sure an extension is present just in-case the file doesn't have one
    		{
    			inputStream = AudioSystem.getAudioInputStream(classLoader.getResourceAsStream(texturePath)); //Get the AudioInputStream of the file
    		}
    		else
			{
				throw new IllegalArgumentException("An Illegal extention was entered and passed the file extention check, How did you get here?"); //Self explanatory
			}
	    }
    	catch(Exception ex) {
	    	throw new IllegalArgumentException("(" + fileExtension + " File Loader) An Error Occurred when getting '" + texturePath + "'!"); //Return an error for the file extension
	    }
    	return inputStream; //Return the AudioInputStream of the file found.
    }
}