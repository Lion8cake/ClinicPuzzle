package Lion8cake;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;

import ClinicPackage.Logging;

//Image System Made by Lion8cake
public class Texture2D {

	private static String[] ValidExtentions = { ".png", ".ico" }; //reads .Ico files that are just renamed .png files because I cannot be bothered making a whole .Ico reader
	
	public float drawScale = 2f;
	
	public String FontFileName = "";
	
	private static Hashtable<String, Image> ImageDictionary = new Hashtable<String, Image>();
	
	/**Named after Microsoft's Texture2D although it doesn't work the same way.
	 * <p>Gets the image based on the string path inputed, the path will extend off the Resources folder for this project.
	 * <p>Make sure all asset files are in the Resources source folder within your project, ('projecteName')\Resources
	 * <p>Returns Image
	 * <p>Supported image formats: .png*/
	public static Image Get(String texturePath)
	{
		Image value = null;
		value = ImageDictionary.get(texturePath);
		if (value == null)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //Get the ClassLoader
	    	try {
	    		String FilePath = readImageFile(texturePath, classLoader);
	    		value = ImageIO.read(classLoader.getResourceAsStream(FilePath));  //Call the readImageFile method, returns String, we put the result of the method into the TexturePath String 
	    		ImageDictionary.put(texturePath, value);
	    	}
	    	catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return value;
	}
	
	/**Gets the Icon used for the program*/
	public static Image GetIcon()
	{
    	return Get("app");
	}
	
	/**Returns ImageIO.read if succeeds, else it will throw an error telling you to check that your image exists and is named correctly/has correct format**/
    private static String readImageFile(String path, ClassLoader classLoader) throws IOException {
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
            throw new IOException("The Image/Texture named '" + path + "' could not be found. Maybe a spelling mistake or unsupported image type?");
        }
        return path2; //Return the String path of the file
    }

	/** Draws an image with the set parameters. Uses the position, frame, scale and more for the appopriate drawn position of the sprite.
	 * Better than graphics.drawImage due to the parameters and their compact customisation.
	 * Overload of DrawAsset(Graphics, Image, int, int, Rectangle, float, float)
	 * @param graphics The graphics of the image, used to draw the image with java's inbuilt texture code.
	 * @param image The image drawn
	 * @param x The x Position of the graphic on screen
	 * @param y The y Position of the graphic on screen
	 * @param Scale The scale of the image
	 */
	public void DrawAsset(Graphics graphics, Image image, int x, int y, float Scale)
	{
		Rectangle frame = null;
		DrawAsset(graphics, image, x, y, frame, Scale, Scale);
	}
	
	/** Draws an image with the set parameters. Uses the position, frame, scale and more for the appopriate drawn position of the sprite.
	 * Better than graphics.drawImage due to the parameters and their compact customisation.
	 * Overload of DrawAsset(Graphics, Image, int, int, Rectangle, float, float)
	 * @param graphics The graphics of the image, used to draw the image with java's inbuilt texture code.
	 * @param image The image drawn
	 * @param x The x Position of the graphic on screen
	 * @param y The y Position of the graphic on screen
	 * @param ScaleX The x scale of the image
	 * @param ScaleY The y scale of the image
	 */
	public void DrawAsset(Graphics graphics, Image image, int x, int y, float ScaleX, float ScaleY)
	{
		Rectangle frame = null;
		DrawAsset(graphics, image, x, y, frame, ScaleX, ScaleY);
	}
	
	/** Draws an image with the set parameters. Uses the position, frame, scale and more for the appopriate drawn position of the sprite.
	 * Better than graphics.drawImage due to the parameters and their compact customisation.
	 * Overload of DrawAsset(Graphics, Image, int, int, Rectangle, float, float)
	 * @param graphics The graphics of the image, used to draw the image with java's inbuilt texture code.
	 * @param image The image drawn
	 * @param x The x Position of the graphic on screen
	 * @param y The y Position of the graphic on screen
	 * @param frame The frame rectangle used to get the current frame
	 * @param Scale The scale of the image
	 */
	public void DrawAsset(Graphics graphics, Image image, int x, int y, Rectangle frame, float Scale)
	{
		DrawAsset(graphics, image, x, y, frame, Scale, Scale);
	}
	
	/** Draws an image with the set parameters. Uses the position, frame, scale and more for the appopriate drawn position of the sprite.
	 * Better than graphics.drawImage due to the parameters and their compact customisation.
	 * @param graphics The graphics of the image, used to draw the image with java's inbuilt texture code.
	 * @param image The image drawn
	 * @param x The x Position of the graphic on screen
	 * @param y The y Position of the graphic on screen
	 * @param frame The frame rectangle used to get the current frame
	 * @param ScaleX The x scale of the image
	 * @param ScaleY The y scale of the image
	 */
	public void DrawAsset(Graphics graphics, Image image, int x, int y, Rectangle frame, float ScaleX, float ScaleY)
	{
		BufferedImage img = (BufferedImage)(image);
		int sizeX = (int)(image.getWidth(null) * drawScale * ScaleX);
		int sizeY = (int)(image.getHeight(null) * drawScale * ScaleY);
		if (frame != null)
		{
			sizeX = (int)(frame.width * drawScale * ScaleX);
			sizeY = (int)(frame.height * drawScale * ScaleY);
			img = img.getSubimage(frame.x, frame.y, frame.width, frame.height);
		}
		graphics.drawImage(img, x, y, sizeX, sizeY, null);
	}
	
	/** Draws an image without the use of the draw scale. This is useful for User Interfaces and panels.
	 * Uses the position, frame, scale and more for the appopriate drawn position of the sprite.
	 * Better than graphics.drawImage due to the parameters and their compact customisation.
	 * @param graphics The graphics of the image, used to draw the image with java's inbuilt texture code.
	 * @param image The image drawn
	 * @param x The x Position of the graphic on screen
	 * @param y The y Position of the graphic on screen
	 * @param frame The frame rectangle used to get the current frame
	 * @param ScaleX The x scale of the image
	 * @param ScaleY The y scale of the image
	 */
	public static void DrawStaticAsset(Graphics graphics, Image image, int x, int y, Rectangle frame, float ScaleX, float ScaleY)
	{
		BufferedImage img = (BufferedImage)(image);
		int sizeX = (int)(image.getWidth(null) * ScaleX);
		int sizeY = (int)(image.getHeight(null) * ScaleY);
		if (frame != null)
		{
			sizeX = (int)(frame.width * ScaleX);
			sizeY = (int)(frame.height * ScaleY);
			img = img.getSubimage(frame.x, frame.y, frame.width, frame.height);
		}
		graphics.drawImage(img, x, y, sizeX, sizeY, null);
	}
	
	public Image DrawText(String text, int x, int y)
	{
		BufferedImage testTextImage = new BufferedImage(32767, 32767, BufferedImage.TYPE_INT_ARGB);
		Image[] Characters = new Image[128]; //ASCII
		try {
			Image textImg = GetTextTexture();
			String[][] fontLayout = new String[7][];
			for (int d = 0; d < fontLayout.length; d++)
			{
				switch (d)
				{
					case 0:
						fontLayout[d] = new String[] { "A", "G", "M", "O", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z", "m", "w", "4", "@", "#", "%", "&", "_", "=", "+", " " };
						break;
					case 1:
						fontLayout[d] = new String[] { "B", "C", "D", "H", "N", "q", "z", "2", "3", "5", "6", "7", "8", "9", "0", "?", "~" };
						break;
					case 2:
						fontLayout[d] = new String[] { "E", "F", "J", "K", "L", "P", "R", "a", "b", "c", "d", "e", "g", "o", "p", "s", "u", "v", "x", "y", "$", "$2", "^", "*", "-", "\\", "/" };
						break;
					case 3:
						fontLayout[d] = new String[] { "h", "j", "k", "n", "r", "{", "}", "\"", "<", ">" };
						break;
					case 4:
						fontLayout[d] = new String[] { "f", "t", "1", "(", ")" };
						break;
					case 5:
						fontLayout[d] = new String[] { "[", "]", ":", ";", "'", "," };
						break;
					case 6:
						fontLayout[d] = new String[] { "I", "i", "l", "!", "." };
						break;
				}
			}
			float TotalCount = 94;
			float CurrentProgress = 0;
			float PrintProgress;
			for (int j = 0; j < fontLayout.length; j++)
			{
				for (int i = 0; i < fontLayout[j].length; i++)
				{
					String texture = "Texture2D_Text_" + FontFileName + "_" + fontLayout[j][i];
					Characters[fontLayout[j][i].charAt(0)] = ImageDictionary.get(texture);
					if (Characters[fontLayout[j][i].charAt(0)] == null)
					{
						CurrentProgress++;
						PrintProgress = (CurrentProgress / TotalCount) * 100;
						System.out.println("Constructing Text Textures: " + PrintProgress + "%");
						int pixelWidth = 6 - j + 1;
						Logging.Log((int)(fontLayout[j][i].charAt(0)) + "|" + fontLayout[j][i].charAt(0));
						Logging.Log((i * pixelWidth) + "|" + j * 9 + "|" + pixelWidth + "|" + 9);
						Characters[fontLayout[j][i].charAt(0)] = ((BufferedImage)textImg).getSubimage(i * pixelWidth, j * 9, pixelWidth, 9);
						ImageDictionary.put(texture, textImg);
					}
				}
			}
			
			char[] texts = new char[text.length()];
			Graphics2D g = (Graphics2D)testTextImage.getGraphics();
			int k = x;
			int l = y;
			/*for (int o = 0; o < text.length(); o++)
			{
				texts[o] = text.charAt(o);
				Logging.Log((int)(texts[o]) + "|" + texts[o] + "|" + Characters[texts[o]]);
				g.drawImage(Characters[texts[o]], k, l, null);
				k += Characters[texts[o]].getWidth(null);
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Characters['A'];
	}
	
	private Image GetTextTexture() throws IOException
	{
		if (FontFileName == "")
		{
			throw new IOException("The Texture does not contain a font to draw, did you forget to set a font file to Texture2D.FontFileName?");
		}
		Image FontImage = Get(FontFileName);
		return FontImage;
	}
}
