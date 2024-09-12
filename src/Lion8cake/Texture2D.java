package Lion8cake;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;

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
	
	public static BufferedImage ColoredRect(int width, int height, Color color)
	{
		String key = width + height + color.getRGB() + "Rectangle";
		Image rect = ImageDictionary.get(key);
		if (rect == null)
		{
			rect = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = (Graphics)(rect.getGraphics());
			g.setColor(color);
			g.fillRect(0, 0, width, height);
			g.dispose();
			ImageDictionary.put(key, rect);
		}
		return (BufferedImage)rect;
	}
	
	public Image DrawText(String text, int width, int height)
	{
		Color color = Color.WHITE;
		return DrawText(text, width, height, color);
	}
	
	public Image DrawText(String text, int width, int height, Color textColor)
	{
		BufferedImage TextImage = null;
		BufferedImage[] Characters = new BufferedImage[128]; //ASCII
		boolean noWidthDefined = width == -1;
		boolean noHeightDefined = height == -1;
		int textDrawnLength = 0;
		int textDrawnHeight = 0;
		String savedtempTextImage = text + textColor + "_Image";
		try {
			BufferedImage textImg = (BufferedImage)GetTextTexture();
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
						fontLayout[d] = new String[] { "I", "i", "l", "!", "|", "." };
						break;
				}
			}
			float TotalCount = 95;
			float CurrentProgress = 0;
			float PrintProgress;
			for (int j = 0; j < fontLayout.length; j++)
			{
				for (int i = 0; i < fontLayout[j].length; i++)
				{
					String texture = "Texture2D_Text_" + FontFileName + "_" + fontLayout[j][i];
					Characters[fontLayout[j][i].charAt(0)] = (BufferedImage)ImageDictionary.get(texture);
					if (Characters[fontLayout[j][i].charAt(0)] == null)
					{
						CurrentProgress++;
						PrintProgress = (CurrentProgress / TotalCount) * 100;
						System.out.println("Constructing Text Textures: " + PrintProgress + "%");
						int pixelWidth = 7 - j;
						Characters[fontLayout[j][i].charAt(0)] = textImg.getSubimage(i * (pixelWidth), j * 9, pixelWidth, 9);
						ImageDictionary.put(texture, (Image)Characters[fontLayout[j][i].charAt(0)]);
					}
				}
			}
			
			TextImage = (BufferedImage)ImageDictionary.get(savedtempTextImage);
			if (TextImage == null)
			{
				TextImage = new BufferedImage(noWidthDefined ? 511 : width, noHeightDefined ? 511 : height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = (Graphics2D)TextImage.getGraphics();
				int k = 0;
				int l = 0;
				char[] texts = new char[text.length()];
				for (int s = 0; s < text.length(); s++)
				{
					texts[s] = text.charAt(s);
				}
				for (int o = 0; o < text.length(); o++)
				{
					//Word Warapping
					int wordWidth = 0;
					if  (texts[o] == ' ' && o != text.length() - 1)
					{
						wordWidth = 0;
						int futureChar = 1;
						boolean endofWord = false;
						while (!endofWord)
						{
							futureChar++;
							if (!(o + futureChar == text.length()))
							{
								wordWidth += Characters[texts[o + futureChar]].getWidth();
								if (texts[o + futureChar] == ' ')
								{
									endofWord = true;
								}
							}
							else
								endofWord = true;
						}
					}
					if (texts[o] == '/' && o != text.length() - 1 && texts[o + 1] == 'n')
					{
						k = 0;
						l += 12;
						
					}
					//Color
					for (int x = 0; x < Characters[texts[o]].getWidth(); x++)
					{
						for (int y = 0; y < Characters[texts[o]].getHeight(); y++)
						{
							if (Characters[texts[o]].getRGB(x, y) != 0)
							{
								Characters[texts[o]].setRGB(x, y, textColor.getRGB());
							}
						}
					}
					//Character Wrapping
					if ((!noHeightDefined && l < height) || noHeightDefined)
					{
						if (!noWidthDefined && (k + wordWidth > width))
						{
							k = 0;
							l += 12;
						}
					}
					//Image Drawing
					if (!(k == 0 && texts[o] == ' ') && !(k == 0 && texts[o] == '/' && texts[o + 1] == 'n') && !(k == 0 && o - 1 != -1 && texts[o - 1] == '/' && texts[o] == 'n'))
					{
						g.drawImage(Characters[texts[o]], k, l, null);
						k += Characters[texts[o]].getWidth(null) + (texts[o] == ' ' || (o < text.length() - 1 && texts[o + 1] == ' ') ? 0 : 1);
					}
				}
				g.dispose();
				textDrawnLength = k;
				textDrawnHeight = l + 12;
				BufferedImage tempTextImage = null;
				if (noWidthDefined || noHeightDefined)
				{
					tempTextImage = new BufferedImage(noWidthDefined ? textDrawnLength : TextImage.getWidth(), noHeightDefined ? textDrawnHeight : TextImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = (Graphics2D)tempTextImage.getGraphics();
					g2.drawImage(TextImage, 0, 0, null);
					g2.dispose();
				}
				else
					tempTextImage = TextImage;
				ImageDictionary.put(savedtempTextImage, (Image)tempTextImage);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return TextImage;
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
