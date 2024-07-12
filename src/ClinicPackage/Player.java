package ClinicPackage;

public class Player extends Entity {

	public static boolean kUp = false;
	
	public static boolean kDown = false;
	
	public static boolean kLeft = false;
	
	public static boolean kRight = false;
	
	public Player()
	{
		SetStaticDefaults();
		x *= 32;
		y *= 32;
	}
	
	public void SetStaticDefaults()
	{
		x = 7;
		y = 5;
		velocityY = 4;
		velocityX = 4;
	}
	
	public void UpdateSpeeds()
	{
		//Logging.Log("Key Up is pressed: " + Player.kUp, LoggingType.Base);
		if (Player.kUp)
		{
			y -= velocityY;
		}
		if (Player.kDown)
		{
			y += velocityY;
		}
		if (Player.kLeft)
		{
			x -= velocityX;
		}
		if (Player.kRight)
		{
			x += velocityX;
		}
	}
	
	public void Update()
	{
		UpdateSpeeds();
	}
}
