package Lion8cake;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Sound system made by Lion8cake
public class Sound {
	private static String[] ValidExtentions = { ".wav", ".snd", "NotFound" };
	
	private static Hashtable<String, InputStream> SoundDictionary = new Hashtable<String, InputStream>();
	
	public int _VOLUME = 100;
	
	public Hashtable<String, Clip> activeClips = new Hashtable<String, Clip>();
	
	public void Play(String name)
	{
		UpdateClips(name);
		Clip clip = null;
		clip = activeClips.get(name);
		if (clip != null)
		{
			clip.start();
		}
	}
	
	public void Stop(String name)
	{
		UpdateClips(name);
		Clip clip = null;
		clip = activeClips.get(name);
		if (clip != null)
		{
			clip.stop();
			clip.close();
			clip.flush();
		}
	}
	
	public void Loop(String name, int loopCount)
	{
		UpdateClips(name);
		Clip clip = null;
		clip = activeClips.get(name);
		if (clip != null)
		{
			clip.loop(loopCount);
		}
	}
	
	/**Sets the volume of all sounds played through this instance of Sound. <br/>
	 * accepts a percentage between 0 and 100
	 * @param volume percentage. Only accpets 0 to 100.
	 * @throws Exception when outside of the percentage range,
	 */
	public void SetVolume(int volume) throws Exception
	{
		if (volume > 100 || volume < 0)
		{
			throw(new Exception("cannot increment past 0 or 100"));
		}
		
		Enumeration<String> keys = activeClips.keys();
		while (keys.hasMoreElements()) {
			String str = keys.nextElement();
			UpdateClips(str);
		}
		
		float trueVolume = 107 * ((float)volume / 100);
		_VOLUME = (int)trueVolume;
	}
	
	private void UpdateClips(String name)
	{
		if (_VOLUME <= 0)
		{
			Stop(name);
			return;
		}
		Clip clip = null;
		clip = activeClips.get(name);
		if (clip != null)
		{
			if (!clip.isActive() || !clip.isOpen())
			{
				clip.stop();
				clip.close();
				clip.flush();
				activeClips.remove(name);
				return;
			}
			FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	        int volume = _VOLUME;
	        float range = control.getMinimum();
	        float result = range * (1 - volume / 100.0f);
	        control.setValue(result);
		}
		else
		{
			System.out.println("sound under the name '" + name + "' was not found, did you forget to add it to the the hashtable (addClips method)");
		}
	}
	
	public void SetNonCachedClipVolume(Clip clip)
	{
		FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        int volume = _VOLUME;
        float range = control.getMinimum();
        float result = range * (1 - volume / 100.0f);
        control.setValue(result);
	}
	
	/**Gets the clip based on the string path inputed, the path will extend off the Resources folder for this project.
	 * <p>Make sure all asset files are in the Resources source folder within your project, ('projecteName')\Resources
	 * <p>Returns Clip
	 * <p>Supported image formats: .snd*/
	public Clip Get(String texturePath, boolean onlyOne)
	{
		Clip value = null;
		value = activeClips.get(texturePath);
		if (value == null)
		{
			InputStream value2 = null;
			AudioInputStream Ainput = null;
			value2 = SoundDictionary.get(texturePath);
	    	try {
	    		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //Get the ClassLoader
				String FilePath = readSoundFile(texturePath, classLoader);
	    		value2 = classLoader.getResourceAsStream(FilePath);
	    		SoundDictionary.put(texturePath, value2);
	    	}
	    	catch (IOException e) {
				e.printStackTrace();
			}
	    	BufferedInputStream bufferedAudioStream = new BufferedInputStream(value2);
			try {
				Ainput = AudioSystem.getAudioInputStream(bufferedAudioStream);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				value = AudioSystem.getClip();
				value.open(Ainput);
				if (onlyOne)
					activeClips.put(texturePath, value);
			} catch (LineUnavailableException | IOException e) {
				e.printStackTrace();
			}
		}
    	return value;
	}
	
	/**Returns String if it succeeds, else it will throw an error telling you to check that your image exists and is named correctly/has correct format**/
    private static String readSoundFile(String path, ClassLoader classLoader) throws IOException {
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
			if (fileType == 2)
			{
				throw new IOException("The sound named '" + path + "' could not be found. Maybe a spelling mistake or unsupported audio type?");
			}
		}
        return path2; //Return the String path of the file
    }
}